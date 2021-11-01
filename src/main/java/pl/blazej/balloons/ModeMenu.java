package pl.blazej.balloons;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;

public class ModeMenu extends JFrame implements ActionListener {
    private JButton levelModeButton, arcadeModeButton, returnButton;

    ModeMenu() {
        super("Bubble Hit");
        setDefaultCloseOperation(3);
        setSize(350, 500);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1));
        this.levelModeButton = new JButton("Level mode");
        this.arcadeModeButton = new JButton("Arcade mode");
        this.returnButton = new JButton("Return");
        this.levelModeButton.addActionListener(this);
        this.arcadeModeButton.addActionListener(this);
        this.returnButton.addActionListener(this);
        add(this.levelModeButton);
        add(this.arcadeModeButton);
        add(this.returnButton);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.levelModeButton)
            try {
                dispose();
                Map map = new Map("/maps/fabularny.txt");
                EventQueue.invokeLater(() -> map.setVisible(true));
            } catch (IOException error) {
                System.out.println("ERROR: IOException w actionPerformed fabularny");
            }
        if (e.getSource() == this.arcadeModeButton)
            try {
                dispose();
                Map map = new Map("/maps/arcade.txt");
                EventQueue.invokeLater(() -> map.setVisible(true));
            } catch (IOException error) {
                System.out.println("ERROR: IOException w actionPerformed arcade");
            }
        if (e.getSource() == this.returnButton) {
            dispose();
            MainMenu mainMenu = new MainMenu();
        }
    }
}
