package pl.blazej.balloons;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;

/**
 * Handles everything that happens on the map during the game
 */
public class Map extends JFrame implements KeyListener {
    private double indirectx, indirecty, length;
    private double totShift = 10.0D;
    private double xShift, yShift, xShift2, yShift2;
    private boolean stop = false;
    private Timer tim;
    private Util util = new Util();
    private Vector<Balloon> bullets = new Vector<>();
    private Vector<Balloon> displayedBalloons;
    private int score = 0;
    private int counter = 0;
    private int counter2 = 1;
    private int release = 0;
    private boolean pause = false;
    private Balloon bullet, secondBullet;
    private Image purpleBullet, redBullet, greenBullet, blueBullet;
    private int width, height, mode;


    /**
     * The constructor creates a game window based on a text file loaded in the Util class.
     * Creates a bullet in the center of the map at the bottom and a second bullet at the bottom right.
     * The player shoots with the first bullet, the second will then take the place of the first.
     * The balloons are in one container and the bullets in the other
     * @param pathToMapFile Path to text file from which the map is loaded.
     * @throws IOException When no text file is found.
     */
    Map(String pathToMapFile) throws IOException {
        this.height = util.getMapHeight(pathToMapFile);
        this.width = util.getMapWidth(pathToMapFile);

        this.mode = util.getMapMode(pathToMapFile);
        this.displayedBalloons = util.getBalloons(pathToMapFile);
        setSize(this.width * 60, this.height * 60);
        setTitle("Bubble Hit");
        setLocationRelativeTo(null);
        addKeyListener(this);
        final Game game = new Game();
        this.bullet = new Balloon(util.getColour(6), this.width * 30 - 30, (this.height - 2) * 60);
        this.secondBullet = new Balloon(util.getColour(6), this.width * 60 - 60, (this.height -1 ) * 60);
        this.bullets.add(this.secondBullet);
        this.bullets.add(this.bullet);

        InputStream stream = getClass().getResourceAsStream("/img/purple.png");
        purpleBullet = (new ImageIcon(ImageIO.read(stream))).getImage();
        stream = getClass().getResourceAsStream("/img/red.png");
        redBullet = (new ImageIcon(ImageIO.read(stream))).getImage();
        stream = getClass().getResourceAsStream("/img/green.png");
        greenBullet = (new ImageIcon(ImageIO.read(stream))).getImage();
        stream = getClass().getResourceAsStream("/img/blue.png");
        blueBullet = (new ImageIcon(ImageIO.read(stream))).getImage();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        /**
         * Animates balloons on the map.
         * Checks whether the game is over or whether the balloon coordinates should be lowered.
         */
        class TimeListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                if (!Map.this.stop) {
                    Map.this.movement();
                    game.checkStatus(Map.this.displayedBalloons);
                    Map.this.repaint();
                }
                if (!game.checkStatus(Map.this.displayedBalloons)) {
                    game.ending(Map.this.score, util.getMapMode(pathToMapFile));
                    Map.this.tim.stop();
                    Map.this.dispose();
                }
                if (Map.this.counter == 5)
                    if (Map.this.mode == 0) {
                        game.descendBallons(Map.this.displayedBalloons, util.getDESCENDARC());
                        Map.this.counter = 0;
                        Map.this.displayedBalloons.addAll(game.addBallons());
                    } else {
                        game.descendBallons(Map.this.displayedBalloons, util.getDESCENDFAB());
                    }
                if (Map.this.mode == 1 && Map.this.displayedBalloons.isEmpty()) {
                    Map.this.tim.stop();
                    game.nextLevel();
                    Map.this.dispose();
                }
            }
        }
        ActionListener listener = new TimeListener();
        int time = 5;
        this.tim = new Timer(time, listener);
        this.tim.start();
        mouseListenerMap();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Map.this.dispose();
                MainMenu mainMenu = new MainMenu();
            }
        });
    }

    private void mouseListenerMap() {
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (Map.this.release == 0 && !Map.this.pause) {
                    Location clickCoordinates = new Location(e.getX(), e.getY());
                    double s1 = Map.this.getWidth();
                    double s2 = Map.this.width * 60;
                    double wsps = s1 / s2;
                    double s3 = Map.this.bullets.lastElement().getxCoordinate() * wsps;
                    int su3 = (int)s3;
                    double s4 = clickCoordinates.getxCoordinate();
                    int su4 = (int)s4;
                    Map.this.xShift2 = su4 - su3;
                    double w1 = Map.this.getHeight();
                    double w2 = Map.this.height * 60;
                    double wspw = w1 / w2;
                    double w3 = Map.this.bullets.lastElement().getyCoordinate() * wspw;
                    int wu3 = (int)w3;
                    double w4 = clickCoordinates.getyCoordinate();
                    int wu4 = (int)w4;
                    Map.this.yShift2 = wu4 - wu3;
                    Map.this.length = Math.sqrt(Math.pow(Map.this.yShift2, 2.0D) + Math.pow(Map.this.xShift2, 2.0D));
                    Map.this.indirectx = Map.this.xShift2 / Map.this.length;
                    Map.this.indirecty = Map.this.yShift2 / Map.this.length;
                    Map.this.xShift = Math.abs(Map.this.totShift * 1.0D / wsps * Map.this.indirectx);
                    Map.this.yShift = Math.abs(Map.this.totShift * 1.0D / wspw * Map.this.indirecty);
                    Map.this.release = 1;
                    Map.this.stop = false;
                    if (clickCoordinates.getyCoordinate() > Map.this.bullets.lastElement().getyCoordinate()) {
                        Map.this.xShift = 0.0D;
                        Map.this.yShift = 0.0D;
                    }
                }
            }
        });
    }

    /**
     * Changes the coordinates of the bullet on the map from its launch to its stop.
     * If a bullet hits a balloon or the top wall, it is stopped, evened, removed from the missile container and placed in the balloon container.
     * The second bullet then becomes the first, and a new one is drawn in place of the second.
     * If the bullet hits a group of two or more balloons of the same color, they disappear from the map.
     */
    private void movement() {
        int a = (this.width - 1) * 60;
        if (this.bullets.lastElement().getxCoordinate() >= 0 && (this.bullets.lastElement()).getxCoordinate() <= a) {
            if (this.xShift2 < 0)
                this.bullets.lastElement().setxCoordinate((int)((this.bullets.lastElement()).getxCoordinate() - this.xShift));
            if (this.xShift2 > 0)
                this.bullets.lastElement().setxCoordinate((int)((this.bullets.lastElement()).getxCoordinate() + this.xShift));
        }
        if (this.bullets.lastElement().getxCoordinate() <= 0) {
            this.bullets.lastElement().setxCoordinate(0);
            this.xShift = -1.0D * this.xShift;
        }
        if (this.bullets.lastElement().getxCoordinate() >= a) {
            this.bullets.lastElement().setxCoordinate(a);
            this.xShift = -1.0D * this.xShift;
        }
        if (this.bullets.lastElement().getyCoordinate() >= 60 && this.bullets.lastElement().getyCoordinate() <= (this.height - 1) * 60) {
            if (this.yShift2 < 0) {
                this.bullets.lastElement().setyCoordinate((int)((this.bullets.lastElement()).getyCoordinate() - this.yShift));
                if (this.yShift2 > 0)
                    this.bullets.lastElement().setyCoordinate((int)((this.bullets.lastElement()).getyCoordinate() + this.yShift));
            }
            if (this.bullets.lastElement().getyCoordinate() <= 60 * this.counter2) {
                this.bullets.lastElement().setyCoordinate(60 * this.counter2);
                int n = 0;
                while (this.bullets.lastElement().getxCoordinate() > n * 60 + 30)
                    n++;
                int dx = n * 60;
                this.bullets.lastElement().setxCoordinate(dx);
                this.stop = true;
                this.bullets.lastElement().setxCoordinate(this.bullets.lastElement().getxCoordinate() / 60);
                this.bullets.lastElement().setyCoordinate(this.bullets.lastElement().getyCoordinate() / 60);
                this.displayedBalloons.add(this.bullets.lastElement());
                int variableq;
                int variablez = 0;
                int variablen = 0;
                int variablec = 0;
                int variablezo = 0;
                for (Balloon displayedBalloon : this.displayedBalloons) {
                    if (displayedBalloon.colour == Colour.GREEN)
                        variablez = 10;
                    if (displayedBalloon.colour == Colour.RED)
                        variablen = 100;
                    if (displayedBalloon.colour == Colour.BLUE)
                        variablec = 1000;
                    if (displayedBalloon.colour == Colour.PURPLE)
                        variablezo = 10000;
                }
                variableq = variablez + variablen + variablec + variablezo;
                this.bullet = new Balloon(util.getColour(variableq), this.width * 60 - 60, (this.height - 1) * 60);
                this.secondBullet = this.bullets.firstElement();
                this.secondBullet.setxCoordinate(this.width * 30 - 30);
                this.secondBullet.setyCoordinate((this.height - 2) * 60);
                this.bullets.clear();
                this.bullets.add(this.bullet);
                this.bullets.add(this.secondBullet);
                this.counter++;
                disappearing();
                this.release = 0;
            }
            if (this.bullets.lastElement().getyCoordinate() >= (this.height - 1) * 60) {
                this.bullets.lastElement().setyCoordinate((this.height - 1) * 60);
                this.yShift = -1.0D * this.yShift;
            }
        }
        if (!isClear(this.displayedBalloons)) {
            int n = 0;
            while (this.bullets.lastElement().getxCoordinate() > n * 60 + 30)
                n++;
            int dx = n * 60;
            this.bullets.lastElement().setxCoordinate(dx);
            int m = 0;
            while ((this.bullets.lastElement()).getyCoordinate() > m * 60 + 30)
                m++;
            int dy = m * 60;
            this.bullets.lastElement().setyCoordinate(dy);
            this.stop = true;
            this.bullets.lastElement().setxCoordinate(this.bullets.lastElement().getxCoordinate() / 60);
            this.bullets.lastElement().setyCoordinate(this.bullets.lastElement().getyCoordinate() / 60);
            this.displayedBalloons.add(this.bullets.lastElement());
            int variableq;
            int variablez = 0;
            int variablen = 0;
            int variablec = 0;
            int variablezo = 0;
            for (Balloon displayedBalloon : this.displayedBalloons) {
                if (displayedBalloon.colour == Colour.GREEN)
                    variablez = 10;
                if (displayedBalloon.colour == Colour.RED)
                    variablen = 100;
                if (displayedBalloon.colour == Colour.BLUE)
                    variablec = 1000;
                if (displayedBalloon.colour == Colour.PURPLE)
                    variablezo = 10000;
            }
            variableq = variablez + variablen + variablec + variablezo;
            this.bullet = new Balloon(util.getColour(variableq), this.width * 60 - 60, (this.height - 1) * 60);
            this.secondBullet = this.bullets.firstElement();
            this.secondBullet.setxCoordinate(this.width * 30 - 30);
            this.secondBullet.setyCoordinate((this.height - 2) * 60);
            this.bullets.clear();
            this.bullets.add(this.bullet);
            this.bullets.add(this.secondBullet);
            this.counter++;
            disappearing();
            this.release = 0;
        }
    }

    /**
     * Checks whether the bullet hit any balloon placed on the map.
     * @param Balloons Container of balloons displayed on the map.
     * @return false if a cartridge is encountered a balloon in its path, true otherwise.
     */
    private boolean isClear(Vector<Balloon> Balloons) {
        for (Balloon b : Balloons) {
            if (Math.sqrt(Math.pow((b.getxCoordinate() * 60 - (this.bullets.lastElement()).getxCoordinate()), 2.0D) + Math.pow(Math.abs(b.getyCoordinate() * 60 - (this.bullets.lastElement()).getyCoordinate()), 2.0D)) <= 52.0D)
                return false;
        }
        return true;
    }

    public void keyPressed(KeyEvent evt) {}

    public void keyReleased(KeyEvent evt) {}

    public void keyTyped(KeyEvent evt) {
        char c = evt.getKeyChar();
        this.pause = c == 'p';
    }

    /**
     * Responsible for the disappearance of the balloons if the bullet hits a group of two or more balloons of the same color.
     */
    private void disappearing() {
        int[] indexArray = new int[150];
        for (int p = 0; p < 150; p++)
            indexArray[p] = -1;
        int counter3 = 0;
        for (int i1 = 0; i1 < this.displayedBalloons.size(); i1++) {
            int polX1 = (this.displayedBalloons.get(i1)).getxCoordinate();
            int polY1 = (this.displayedBalloons.get(i1)).getyCoordinate();
            int polX2 = (this.displayedBalloons.lastElement()).getxCoordinate();
            int polY2 = (this.displayedBalloons.lastElement()).getyCoordinate();
            int odlX = polX1 - polX2;
            int odlY = polY1 - polY2;
            double odlXY = Math.sqrt(Math.pow(odlX, 2.0D) + Math.pow(odlY, 2.0D));
            if (odlXY == 1.0D && this.displayedBalloons.lastElement().colour == (this.displayedBalloons.get(i1)).colour) {
                indexArray[i1] = i1;
                indexArray[this.displayedBalloons.size() - 1] = this.displayedBalloons.size() - 1;
                for (int i3 = 0; i3 < 20; i3++) {
                    for (int i2 = 0; i2 < this.displayedBalloons.size(); i2++) {
                        if (indexArray[i2] != -1)
                            for (i1 = 0; i1 < this.displayedBalloons.size() - 1; i1++) {
                                polX1 = this.displayedBalloons.get(i1).getxCoordinate();
                                polY1 = this.displayedBalloons.get(i1).getyCoordinate();
                                polX2 = this.displayedBalloons.get(i2).getxCoordinate();
                                polY2 = this.displayedBalloons.get(i2).getyCoordinate();
                                odlX = polX1 - polX2;
                                odlY = polY1 - polY2;
                                odlXY = Math.sqrt(Math.pow(odlX, 2.0D) + Math.pow(odlY, 2.0D));
                                if (odlXY == 1.0D && this.displayedBalloons.get(i1).colour == this.displayedBalloons.get(i2).colour)
                                    indexArray[i1] = i1;
                            }
                    }
                }
            }
        }
        for (int h = this.displayedBalloons.size() - 1; h >= 0; h--) {
            if (indexArray[h] != -1)
                counter3++;
        }
        int scoreCounter = 0;
        if (counter3 > 2)
            for (int i = this.displayedBalloons.size() - 1; i >= 0; i--) {
                if (indexArray[i] != -1)
                    scoreCounter++;
            }
        if (counter3 > 2)
            for (int i = this.displayedBalloons.size() - 1; i >= 0; i--) {
                if (indexArray[i] != -1)
                    this.displayedBalloons.remove(indexArray[i]);
            }
        this.score += scoreCounter;
    }

    /**
     * Draws all ballons and bullets in containers in the correct places on the map.
     * @param g
     */
    private void paintComponent(Graphics g){
        super.paintComponents(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.width * 60, this.height * 60);
        g.setColor(Color.CYAN);
        if (this.counter == 5 && this.mode == 1 && util.getDESCENDFAB() != 0) {
            this.counter2++;
            this.counter = 0;
        }
        g.fillRect(0, 0, this.width * 60, 60 * this.counter2);
        g.fillRect(0, this.height * 60 - 60, this.width * 60, 60);
        g.setColor(Color.BLACK);
        for (int kr = 0; kr < this.height * 60; kr += 20)
            g.fillRect(kr, (this.height - 2) * 60 - 2, 10, 1);

        for (Balloon db : this.displayedBalloons) {
            switch (db.getColour()) {
                case PURPLE:
                    db.setBallonImage(purpleBullet);
                    break;
                case RED:
                    db.setBallonImage(redBullet);
                    break;
                case GREEN:
                    db.setBallonImage(greenBullet);
                    break;
                case BLUE:
                    db.setBallonImage(blueBullet);
                    break;
                default:
                    break;
            }
            if (g.getColor() != Color.WHITE)
                g.drawImage(db.getBallonImage(), db.getxCoordinate() * 60, db.getyCoordinate() * 60, null);
        }
        for (Balloon p : this.bullets) {
            switch (p.getColour()) {
                case PURPLE:
                    p.setBallonImage(purpleBullet);
                    break;
                case RED:
                    p.setBallonImage(redBullet);
                    break;
                case GREEN:
                    p.setBallonImage(greenBullet);
                    break;
                case BLUE:
                    p.setBallonImage(blueBullet);
                    break;
                default:
                    g.setColor(Color.WHITE);
                    break;
            }
            if (g.getColor() != Color.WHITE)
                g.drawImage(p.getBallonImage(), p.getxCoordinate(), p.getyCoordinate(), null);
        }
        g.dispose();
        setFocusable(true);
    }

    /**
     * Responsible for double buffering and scaling of graphic components
     * @param g
     */
    public void paint(Graphics g) {
        BufferedImage dbImage = new BufferedImage(this.width * 60, this.height * 60, 2);
        Graphics dbg = dbImage.getGraphics();
        paintComponent(dbg);
        BufferedImage scaled = new BufferedImage(getWidth(), getHeight(), 2);
        Graphics2D gg = scaled.createGraphics();
        gg.drawImage(dbImage, 0, 0, getWidth(), getHeight(), null);
        g.drawImage(scaled, 0, 0, this);
    }
}