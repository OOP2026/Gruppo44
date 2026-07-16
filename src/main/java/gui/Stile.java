
package gui;

import javax.swing.*;

import java.awt.*;

/**
 * Classe di utilità che centralizza tutte le risorse grafiche dell'applicazione.
 * <p>
 * Fornisce accesso costante a una palette di colori, tipografie (font),
 * dimensioni standard e metodi factory per la creazione di componenti
 * grafici uniformi (pulsanti e bordi).
 * <p>
 * L'utilizzo di questa classe evita la duplicazione di istanze di oggetti
 * grafici pesanti, migliorando le performance e garantendo uno stile
 * visivo coerente in tutta l'interfaccia utente.
 */

public final class Stile {

    /**
     * Costruttore privato per impedire l'istanziazione di questa classe utility.
     */
    private Stile() {}


    // COLORI

    public static final Color AZZURRO = new Color(235, 243, 249);

    public static final Color BLU_CHIARO = new Color(141, 185, 224);

    public static final Color BLU_SCURO = new Color(24, 43, 73);

    public static final Color VERDE_CHIARO = new Color(168, 218, 220);

    public static final Color VERDE_SCURO = new Color(46, 204, 113);

    public static final Color ARANCIONE1 = new Color(230, 126, 34);

    public static final Color ARANCIONE2 = new Color(243, 156, 18);

    public static final Color ROSSO_CHIARO = new Color(231, 76, 60);

    public static final Color ROSSO_SCURO = new Color(192, 57, 43);

    public static final Color TESTO_SCURO = new Color(44, 62, 80);

    public static final Color TESTO_CHIARO = new Color(70, 80, 95);

    public static final String FONT = "Segoe UI";



    //  FONT

    public static final Font FONT_TITOLO_MEDIO = new Font(FONT, Font.BOLD, 18);

    public static final Font FONT_SOTTOTITOLO = new Font(FONT, Font.PLAIN, 15);

    public static final Font FONT_ISTRUZIONE = new Font(FONT, Font.ITALIC, 13);

    public static final Font FONT_ETICHETTA = new Font(FONT, Font.BOLD, 14);

    public static final Font FONT_TESTO = new Font(FONT, Font.PLAIN, 12);

    public static final Font FONT_PULSANTE = new Font(FONT, Font.BOLD, 12);



    // DIMENSIONI RICORRENTI

    public static final Dimension PULSANTE_GRANDE = new Dimension(180, 50);

    public static final Insets INSET_STANDARD = new Insets(8, 12, 8, 12);
    


    //  PULSANTI

    /**
     * Crea un pulsante specifico per sfondi chiari.
     * @param testo Il testo da visualizzare sul pulsante.
     * @param sfondo Il colore di sfondo del pulsante.
     * @return Un'istanza di {@link JButton} configurata.
     */
    public static JButton creaPulsante(String testo, Color sfondo) {

        JButton pulsante = new JButton(testo);

        pulsante.setFont(FONT_PULSANTE);

        pulsante.setBackground(sfondo);

        pulsante.setForeground(TESTO_SCURO);

        pulsante.setFocusPainted(false);

        return pulsante;

    }



    /**
     * Crea un pulsante specifico per sfondi scuri, con testo in bianco per
     * garantire leggibilità.
     * @param testo Il testo da visualizzare sul pulsante.
     * @param sfondo Il colore di sfondo del pulsante.
     * @return Un'istanza di {@link JButton} con testo bianco.
     */

    public static JButton creaPulsanteTestoBianco(String testo, Color sfondo) {

        JButton pulsante = creaPulsante(testo, sfondo);

        pulsante.setForeground(Color.WHITE);

        return pulsante;

    }



    // BORDI

    /**
     * Genera un bordo titolato personalizzato.
     * @param titolo Il testo del titolo.
     * @param coloreBordo Il colore del perimetro del bordo.
     * @param coloreTesto Il colore del testo del titolo.
     * @return Un oggetto {@link javax.swing.border.Border} configurato.
     */

    public static javax.swing.border.Border creaBordoTitolato(String titolo, Color coloreBordo, Color coloreTesto) {

        return BorderFactory.createTitledBorder(

                BorderFactory.createLineBorder(coloreBordo, 2),

                titolo, 0, 0, FONT_ETICHETTA, coloreTesto);

    }
}