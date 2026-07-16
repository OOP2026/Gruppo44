package gui;

import javax.swing.*;
import java.awt.*;

public class PannelloRiutilizzabileMenu extends JPanel {

    /**
     * Inizializza un pannello di navigazione a menu laterale che permette di
     * selezionare e visualizzare diversi pannelli di contenuto.
     * <p>
     * Il componente utilizza un layout a due aree:
     * <ul>
     * <li><b>Area SINISTRA (WEST):</b> Contiene una {@link JComboBox} che elenca le
     * opzioni disponibili. Al variare della selezione, il layout gestore aggiorna
     * il contenuto dell'area centrale.</li>
     * <li><b>Area CENTRALE (CENTER):</b> Utilizza un {@link CardLayout} per
     * sovrapporre i pannelli passati come parametro, mostrando solo quello
     * corrispondente alla scelta effettuata nella tendina.</li>
     * </ul>
     *
     * @param nomeScelta Un array di {@link String} che definisce i nomi delle opzioni
     * nel menu a tendina e le chiavi per il {@code CardLayout}.
     * @param pannelli Un array di {@link JPanel} contenente i pannelli da associare
     * a ciascuna voce del menu (deve avere la stessa lunghezza
     * di {@code nomeScelta}).
     */
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