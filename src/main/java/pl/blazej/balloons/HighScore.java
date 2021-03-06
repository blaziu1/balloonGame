package pl.blazej.balloons;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * Responsible for displaying the window with the players have obtained the best score.
 */
class HighScore extends JFrame {
    HighScore() throws FileNotFoundException {
        super("Highest scores");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                HighScore.this.dispose();
                MainMenu mainmenu = new MainMenu();
            }
        });
        setSize(350, 500);
        setLocationRelativeTo(null);
        Scanner fileIn = new Scanner(new File("files/highscore.txt"));
        JTextArea scores = new JTextArea();
        scores.setEditable(false);
        scores.setFocusable(false);
        while (fileIn.hasNext()) {
            scores.append(fileIn.nextLine());
            scores.append("\n");
        }
        setVisible(true);
        add(scores);
    }
}