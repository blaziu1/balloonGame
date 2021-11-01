package pl.blazej.balloons;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Creates the main menu displayed immediately after starting the program
 * From the main menu the player can choose the game mode, options, highscores or exit the game.
 */
public class MainMenu extends JFrame implements ActionListener {
    private JButton startButton, exitButton, optionsButton, resultsButton;

    MainMenu() {
        super("Bubble Hit");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 500);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1));
        this.startButton = new JButton("Start");
        this.optionsButton = new JButton("Options");
        this.resultsButton = new JButton("Results");
        this.exitButton = new JButton("Exit");
        this.startButton.addActionListener(this);
        this.optionsButton.addActionListener(this);
        this.resultsButton.addActionListener(this);
        this.exitButton.addActionListener(this);
        add(this.startButton);
        add(this.optionsButton);
        add(this.resultsButton);
        add(this.exitButton);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.startButton) {
            dispose();
            ModeMenu modeMenu = new ModeMenu();
        }
        if (e.getSource() == this.resultsButton)
            try {
                dispose();
                HighScore highScore = new HighScore();
            } catch (IOException error) {
                System.out.println("ERROR: IOException highScore");
            }
        if (e.getSource() == this.optionsButton) {
            dispose();
            Options options = new Options();
        }
        if (e.getSource() == this.exitButton)
            System.exit(0);
    }
}