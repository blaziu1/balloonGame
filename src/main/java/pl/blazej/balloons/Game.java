package pl.blazej.balloons;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

class Game {
    private static int map_num = 0;
    private int NUM_OF_FILES = 2;

    void descendBallons(Vector<Balloon> Balloons, int descend) {
        for (Balloon b : Balloons)
            b.setyCoordinate(b.getyCoordinate() + descend);
    }

    Vector<Balloon> addBallons() {
        //MapLoad load = new MapLoad();
        Vector<Balloon> balloons = new Vector();
        Util util = new Util();
        try {
            return util.getBalloons("/maps/addedBalloons.txt");
            //load.loadFile("/maps/addedBalloons.txt");
        } catch (IOException error) {
            System.out.println("ERROR: IOException /maps/addedBalloons.txt");
            return balloons;
        }
    }

    boolean checkStatus(Vector<Balloon> Balloons) {
        for (Balloon b : Balloons) {
            if (b.getyCoordinate() >= 13)
                return false;
        }
        return true;
    }

    void nextLevel() {
        /*File[] files = new File[this.NUM_OF_FILES];
        files[0] = new File("files/secondMap.txt");
        files[1] = new File("files/thirdMap.txt");*/
        String[] filenames = {"/maps/secondMap.txt", "/maps/thirdMap.txt"};
        try {
            Map map = new Map(filenames[map_num]);
            map_num++;
            //EventQueue.invokeLater(() -> map.setVisible(true));
            map.setVisible(true);
        } catch (IOException error) {
            System.out.println("IOException /maps/secondMap.txt");
        } catch (ArrayIndexOutOfBoundsException err) {
            String[] options = { "Yay!" };
            JPanel panel = new JPanel();
            JLabel lbl = new JLabel("You have finished the level mode");
            panel.add(lbl);
            int selectedOption = JOptionPane.showOptionDialog(null, panel, "Game completed", 1, 1, null, (Object[])options, options[0]);
            if (selectedOption == 0) {
                MainMenu menu = new MainMenu();
                map_num = 0;
            }
        }
    }

    void ending(int score, int mode) {
        String[] options = { "OK" };
        if (mode == 0) {
            JPanel panel = new JPanel();
            JLabel lbl = new JLabel("Your score: " + score + ". Enter your name: ");
            JTextField txt = new JTextField(10);
            panel.add(lbl);
            panel.add(txt);
            int selectedOption = JOptionPane.showOptionDialog(null, panel,"You lose", JOptionPane.NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options , options[0]);
            if (selectedOption == 0) {
                String nick = txt.getText();
                if (nick.contains(" ")) {
                    String[] divided2 = nick.split("\\s+");
                    nick = String.join("", divided2);
                }
                if (nick.contains("-"))
                    nick = nick.replace('-', '_');
                if (!nick.isEmpty())
                    try {
                        updateHighScore(nick, score);
                    } catch (IOException error) {
                        System.out.println("ERROR: IOException updateHighScore");
                    }
                MainMenu mainMenu = new MainMenu();
            }
        } else {
            JPanel panel = new JPanel();
            JLabel lbl = new JLabel("You have not completed the game.");
            panel.add(lbl);
            int selectedOption = JOptionPane.showOptionDialog(null, panel, "Game ober", 1, 1, null, (Object[])options, options[0]);
            if (selectedOption == 0) {
                MainMenu mainMenu = new MainMenu();
            }
        }
    }

    private void updateHighScore(String line, int scr) throws FileNotFoundException {
        int[] scores = new int[5];
        int i = 0;
        String[] lines = new String[5];
        String[] names = new String[5];
        Scanner in = new Scanner(new File("files/highscore.txt"));
        while (in.hasNext()) {
            lines[i] = in.nextLine();
            String[] divided = lines[i].split("-");
            String[] divided2 = lines[i].split("\\s+");
            names[i] = divided2[1];
            scores[i] = Integer.parseInt(divided[1]);
            i++;
        }
        for (int j = 0; j < 5; j++) {
            if (scores[j] < scr) {
                switch (j) {
                    case 4:
                        names[4] = line;
                        scores[4] = scr;
                        break;
                    case 3:
                        names[4] = names[3];
                        scores[4] = scores[3];
                        names[3] = line;
                        scores[3] = scr;
                        break;
                    case 2:
                        names[4] = names[3];
                        scores[4] = scores[3];
                        names[3] = names[2];
                        scores[3] = scores[2];
                        names[2] = line;
                        scores[2] = scr;
                        break;
                    case 1:
                        names[4] = names[3];
                        scores[4] = scores[3];
                        names[3] = names[2];
                        scores[3] = scores[2];
                        names[2] = names[1];
                        scores[2] = scores[1];
                        names[1] = line;
                        scores[1] = scr;
                        break;
                    case 0:
                        names[4] = names[3];
                        scores[4] = scores[3];
                        names[3] = names[2];
                        scores[3] = scores[2];
                        names[2] = names[1];
                        scores[2] = scores[1];
                        names[1] = names[0];
                        scores[1] = scores[0];
                        names[0] = line;
                        scores[0] = scr;
                        break;
                }
                break;
            }
        }
        PrintWriter save = new PrintWriter("files/highscore.txt");
        for (int k = 0; k < 5; k++)
            save.println((k + 1) + ". " + names[k] + " -" + scores[k]);
        save.close();
    }
}
