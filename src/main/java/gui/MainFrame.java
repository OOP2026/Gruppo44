package gui;

import javax.swing.*;

public class MainFrame {
    private JPanel framePanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainFrame");
        frame.setContentPane(new LoginPanel().loginPanel); //chiama la classe LoginPanel come inizio
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
