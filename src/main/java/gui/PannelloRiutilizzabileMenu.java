package gui;

import javax.swing.*;
import java.awt.*;

//la classe va a semplificare le dashboard studente, docente e responsabile definendo un
// layout RIUTILIZZABILE dello scheletro del panel con a sinistra menu a tendina (jComboBox) statico
// a destra il contenuto dinamico che cambia in base alla scelta del menu
//nomeScelta[i] è il nome della scelta nella tendina, pannelli[i] è cosa si vede a destra
// quando quella voce è selezionata

public class PannelloRiutilizzabileMenu extends JPanel {

    public PannelloRiutilizzabileMenu(String[] nomeScelta, JPanel[] pannelli) {
        setLayout(new BorderLayout(15, 0));
        setBackground(Stile.AZZURRO);

        JPanel areaDestra = new JPanel(new CardLayout());
        for (int i = 0; i < nomeScelta.length; i++) {
            areaDestra.add(pannelli[i], nomeScelta[i]);
        }

        JComboBox<String> tendina = new JComboBox<>(nomeScelta);
        tendina.setFont(Stile.FONT_ETICHETTA);
        tendina.addActionListener(e -> {
            CardLayout layoutCarte = (CardLayout) areaDestra.getLayout();
            layoutCarte.show(areaDestra, (String) tendina.getSelectedItem());
        });

        JPanel sinistra = new JPanel(new BorderLayout());
        sinistra.setBackground(Stile.AZZURRO);
        sinistra.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sinistra.setPreferredSize(new Dimension(190, 0));
        sinistra.add(tendina, BorderLayout.NORTH);

        add(sinistra, BorderLayout.WEST);
        add(areaDestra, BorderLayout.CENTER);
    }
}