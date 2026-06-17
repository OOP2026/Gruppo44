package gui; // Posizionato nel pacchetto view su IntelliJ

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardHome extends JPanel {

    private MainPanel mainPanel; // Riferimento al mainPanel per i cambi di schermata
    //nel mainPanel abbiamo i metodi che ci servono per cambiare schermata

    public DashboardHome(MainPanel mainPanel) {
        this.mainPanel = mainPanel;

        // Colore di sfondo azzurro pastello accademico
        setBackground(new Color(235, 243, 249));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12); // Spaziatura generosa tra gli elementi
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- INTESTAZIONE ISTITUZIONALE ---
        JLabel lblAteneo = new JLabel("UNIVERSITÀ DEGLI STUDI DI NAPOLI FEDERICO II", JLabel.CENTER);
        lblAteneo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblAteneo.setForeground(new Color(24, 43, 73)); // Blu notte istituzionale
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblAteneo, gbc);

        // --- TITOLO DI BENVENUTO ---
        JLabel lblBenvenuto = new JLabel("Benvenuto nel Sistema di Gestione Orari", JLabel.CENTER);
        lblBenvenuto.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblBenvenuto.setForeground(new Color(70, 80, 95));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        add(lblBenvenuto, gbc);

        // --- ISTRUZIONE PER L'UTENTE ---
        JLabel lblIstruzione = new JLabel("Per accedere o registrarti, seleziona il tuo profilo:", JLabel.CENTER);
        lblIstruzione.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblIstruzione.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 12, 15, 12); // Aumentiamo lo stacco sopra l'istruzione
        add(lblIstruzione, gbc);

        // Ripristiniamo la larghezza delle colonne a 1 per i pulsanti affiancati
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 15, 10, 15);

        // --- PULSANTE PORTALE STUDENTE ---
        JButton btnStudente = new JButton("PORTALE STUDENTE");
        btnStudente.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnStudente.setBackground(new Color(168, 218, 220)); // Verde acqua / Salvia pastello
        btnStudente.setForeground(new Color(44, 62, 80));
        btnStudente.setPreferredSize(new Dimension(180, 50)); // Pulsante grande e cliccabile
        btnStudente.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 3;
        add(btnStudente, gbc);

        // --- PULSANTE PORTALE DOCENTE ---
        JButton btnDocente = new JButton("PORTALE DOCENTE");
        btnDocente.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDocente.setBackground(new Color(141, 185, 224)); // Carta da zucchero pastello
        btnDocente.setForeground(new Color(44, 62, 80));
        btnDocente.setPreferredSize(new Dimension(180, 50));
        btnDocente.setFocusPainted(false);
        gbc.gridx = 1; gbc.gridy = 3;
        add(btnDocente, gbc);



        // --- AZIONI DEI PULSANTI ---
/*
Ogni pulsante (Portale Studente, Portale Docente) è dotato di un ActionListener (add)
 che rimane in ascolto dell'ActionEvent (il click dell'utente) per avviare automaticamente
 il metodo actionPerformed che invoca i metodi di navigazione definiti nel MainPanel
 (es. mainPanel.mostraDashboardStudente()).
 */
        // Quando clicchi su Studente, dici al MainPanel di mostrare la Dashboard dello studente
        btnStudente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.mostraDashboardStudente();
            }
        });

        // Quando clicchi su Docente, dici al MainPanel di mostrare la Dashboard del docente
        btnDocente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.mostraDashboardDocente();
            }
        });
    }
}