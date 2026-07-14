package gui;

import javax.swing.*;
import java.awt.*;

// Finestra principale dell'applicazione la quale contiene il MainPanel e basta
public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Sistema di Gestione Orario Universitario");
        setSize(900, 650);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centra la finestra sullo schermo
        setLayout(new BorderLayout());

        MainPanel mainPanel = new MainPanel();
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true); //la rende visibile
    }
}