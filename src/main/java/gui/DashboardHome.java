package gui;

import javax.swing.*;
import java.awt.*;

// La schermata iniziale ha due colonne affiancate per Studente e Docente
// con due pulsanti: accedi e registrati
public class DashboardHome extends JPanel {

    public DashboardHome(MainPanel mainPanel) {
        setBackground(Stile.AZZURRO);
        setLayout(new BorderLayout());

        add(creaIntestazione(), BorderLayout.NORTH);
        add(creaGrigliaColonne(mainPanel), BorderLayout.CENTER);
    }

    // Nome ateneo + sottotitolo + domanda per scelta in alto alla schermata
    private JPanel creaIntestazione() {
        JPanel pannello = new JPanel(new GridLayout(3, 1, 0, 8));
        pannello.setBackground(Stile.AZZURRO);
        pannello.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));

        JLabel lblAteneo = new JLabel("UNIVERSITÀ DEGLI STUDI DI NAPOLI FEDERICO II", SwingConstants.CENTER);
        lblAteneo.setFont(Stile.FONT_TITOLO_MEDIO);
        lblAteneo.setForeground(Stile.BLU_SCURO);

        JLabel lblBenvenuto = new JLabel("Benvenuto nel Sistema di Gestione Orari", SwingConstants.CENTER);
        lblBenvenuto.setFont(Stile.FONT_SOTTOTITOLO);
        lblBenvenuto.setForeground(Stile.TESTO_CHIARO);

        JLabel lblIstruzione = new JLabel("Seleziona il tuo profilo per accedere o registrarti:", SwingConstants.CENTER);
        lblIstruzione.setFont(Stile.FONT_ISTRUZIONE);
        lblIstruzione.setForeground(Stile.TESTO_SCURO);

        pannello.add(lblAteneo);
        pannello.add(lblBenvenuto);
        pannello.add(lblIstruzione);
        return pannello;
    }

    // le due colonne Studente a sinistra e Docente a destra
    private JPanel creaGrigliaColonne(MainPanel mainPanel) {
        JPanel griglia = new JPanel(new GridLayout(1, 2, 30, 0));
        griglia.setBackground(Stile.AZZURRO);
        griglia.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        griglia.add(creaColonnaStudente(mainPanel));
        griglia.add(creaColonnaDocente(mainPanel));

        return griglia;
    }

    // la colonna Studente ha titolo + pulsante accedi + pulsante registrati
    private JPanel creaColonnaStudente(MainPanel mainPanel) {
        JPanel colonna = creaColonnaBase("PORTALE STUDENTE", Stile.VERDE_CHIARO);

        JButton pulsanteAccedi = Stile.creaPulsante("ACCEDI", Stile.VERDE_CHIARO);
        pulsanteAccedi.setPreferredSize(Stile.PULSANTE_GRANDE);
        pulsanteAccedi.addActionListener(e -> mainPanel.mostraLoginStudente());

        JButton pulsanteRegistrati = Stile.creaPulsante("REGISTRATI", Color.WHITE);
        pulsanteRegistrati.setBorder(BorderFactory.createLineBorder(Stile.VERDE_CHIARO, 2));
        pulsanteRegistrati.setPreferredSize(Stile.PULSANTE_GRANDE);
        pulsanteRegistrati.addActionListener(e -> mainPanel.mostraRegistrazioneStudente());

        aggiungiPulsanti(colonna, pulsanteAccedi, pulsanteRegistrati);
        return colonna;
    }

    // la colonna Docente ha titolo + pulsante accedi + pulsante registrati
    private JPanel creaColonnaDocente(MainPanel mainPanel) {
        JPanel colonna = creaColonnaBase("PORTALE DOCENTE", Stile.BLU_CHIARO);

        JButton pulsanteAccedi = Stile.creaPulsante("ACCEDI", Stile.BLU_CHIARO);
        pulsanteAccedi.setPreferredSize(Stile.PULSANTE_GRANDE);
        pulsanteAccedi.addActionListener(e -> mainPanel.mostraLoginDocente());

        JButton pulsanteRegistrati = Stile.creaPulsante("REGISTRATI", Color.WHITE);
        pulsanteRegistrati.setBorder(BorderFactory.createLineBorder(Stile.BLU_CHIARO, 2));
        pulsanteRegistrati.setPreferredSize(Stile.PULSANTE_GRANDE);
        pulsanteRegistrati.addActionListener(e -> mainPanel.mostraRegistrazioneDocente());

        aggiungiPulsanti(colonna, pulsanteAccedi, pulsanteRegistrati);
        return colonna;
    }

    // panel vuoto con solo il bordo titolato definito in Stile, comune a entrambe le colonne
    private JPanel creaColonnaBase(String titolo, Color colore) {
        JPanel colonna = new JPanel(new GridBagLayout());
        colonna.setBackground(Color.WHITE);
        colonna.setBorder(Stile.creaBordoTitolato(titolo, colore, Stile.BLU_SCURO));
        return colonna;
    }

    private void aggiungiPulsanti(JPanel colonna, JButton pulsanteAccedi, JButton pulsanteRegistrati) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 25, 15, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        gbc.gridy = 0;
        colonna.add(pulsanteAccedi, gbc);
        gbc.gridy = 1;
        colonna.add(pulsanteRegistrati, gbc);
    }
}