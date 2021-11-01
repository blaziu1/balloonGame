package pl.blazej.balloons;

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


    /**
     * Responsible for lowering the balloons on the map.
     * @param Balloons A container containing all balloons to be displayed.
     * @param descend Says how much ballons are to go down.
     */
    void descendBallons(Vector<Balloon> Balloons, int descend) {
        for (Balloon b : Balloons)
            b.setyCoordinate(b.getyCoordinate() + descend);
    }

    /**
     * Adds balloons loaded from a file to the board.
     * Triggered in arcade mode after the balloons have been lowered.
     * @return A container with added ballons
     */
    Vector<Balloon> addBallons() {
        Vector balloons = new Vector();
        Util util = new Util();
        try {
            return util.getBalloons("/maps/addedBalloons.txt");
        } catch (IOException error) {
            System.out.println("ERROR: IOException /maps/addedBalloons.txt");
            return balloons;
        }
    }


    /**
     * Checks if any balloon is at the height of the bullet on the map.
     * @param Balloons Container with all balloons displayed.
     * @return true if the ballons are above the bullet, false if any is at the same height.
     */
    boolean checkStatus(Vector<Balloon> Balloons) {
        for (Balloon b : Balloons) {
            if (b.getyCoordinate() >= 13)
                return false;
        }
        return true;
    }

    /**
     * Responsible for game's behavior after removing all the balloons.
     * After removing all balloons, a new map is created from the text file.
     * If a player has cleared ballons on all available maps, a message is displayed stating that he has won.
     */
    void nextLevel() {
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

    /**
     * Responsible for the behavior of the game after a defeat.
     * A defeat message is displayed in which the player enters his name.
     * If the name was entered and the player's score is better than the results in the highscore.txt file, it is entered there.
     * @param score The result obtained by the player during the game.
     */
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
