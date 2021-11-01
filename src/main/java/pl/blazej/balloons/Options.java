package pl.blazej.balloons;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Options extends JFrame implements ActionListener {
    private JButton easyButton, mediumButton, hardButton;

    Options() {
        super("Choose difficulty");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 3));
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Options.this.dispose();
                MainMenu mainMenu = new MainMenu();
            }
        });
        this.easyButton = new JButton("Easy");
        this.mediumButton = new JButton("Medium");
        this.hardButton = new JButton("Hard");
        this.easyButton.addActionListener(this);
        this.mediumButton.addActionListener(this);
        this.hardButton.addActionListener(this);
        add(this.easyButton);
        add(this.mediumButton);
        add(this.hardButton);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        int difficultyCode, descendFab, descendArc = 1;
        if (e.getSource() == this.easyButton) {
            difficultyCode = 2;
            descendFab = 0;
        } else if (e.getSource() == this.mediumButton) {
            difficultyCode = 3;
            descendFab = 0;
        } else {
            difficultyCode = 4;
            descendFab = 1;
        }
        dispose();
        try {
            PrintWriter save = new PrintWriter("files/difficulty.txt");
            save.println(difficultyCode);
            save.println(descendFab);
            save.println(descendArc);
            save.close();
        } catch (IOException error) {
            System.out.println("IOException actionPerformed");
        }
        MainMenu mainMenu = new MainMenu();
    }
}