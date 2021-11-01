package pl.blazej.balloons;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;

public class MainMenu extends JFrame implements ActionListener {
    private JButton start;
    private JButton wyjdz;
    private JButton opcje;
    private JButton wyniki;

    MainMenu() {
        super("Bubble Hit");
        setDefaultCloseOperation(3);
        setSize(350, 500);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1));
        this.start = new JButton("Start");
        this.opcje = new JButton("Options");
        this.wyniki = new JButton("Results");
        this.wyjdz = new JButton("Exit");
        this.start.addActionListener(this);
        this.opcje.addActionListener(this);
        this.wyniki.addActionListener(this);
        this.wyjdz.addActionListener(this);
        add(this.start);
        add(this.opcje);
        add(this.wyniki);
        add(this.wyjdz);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.start) {
            dispose();
            ModeMenu modeMenu = new ModeMenu();
        }
        if (e.getSource() == this.wyniki)
            try {
                dispose();
                HighScore highScore = new HighScore();
            } catch (IOException error) {
                System.out.println("ERROR: IOException highScore");
            }
        if (e.getSource() == this.opcje) {
            dispose();
            Options options = new Options();
        }
        if (e.getSource() == this.wyjdz)
            System.exit(0);
    }
}