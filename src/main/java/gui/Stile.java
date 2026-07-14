
package gui;

import javax.swing.*;

import java.awt.*;

/*La classe Stile contiene tutto cio' che puo' essere riutilizzato graficamente
nella GUI (colori, font, dimensioni) percio’ per evitare di istanziare ripetutamente
colori/font/stili si usa direttamente questa classe */

public final class Stile {

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



    //  FONT

    public static final Font FONT_TITOLO = new Font("Segoe UI", Font.BOLD, 20);

    public static final Font FONT_TITOLO_MEDIO = new Font("Segoe UI", Font.BOLD, 18);

    public static final Font FONT_SOTTOTITOLO = new Font("Segoe UI", Font.PLAIN, 15);

    public static final Font FONT_ISTRUZIONE = new Font("Segoe UI", Font.ITALIC, 13);

    public static final Font FONT_ETICHETTA = new Font("Segoe UI", Font.BOLD, 14);

    public static final Font FONT_TESTO = new Font("Segoe UI", Font.PLAIN, 12);

    public static final Font FONT_PULSANTE = new Font("Segoe UI", Font.BOLD, 12);

    public static final Font FONT_PULSANTE_PICCOLO = new Font("Segoe UI", Font.BOLD, 11);



    // DIMENSIONI RICORRENTI

    public static final Dimension PULSANTE_GRANDE = new Dimension(180, 50);

    public static final Insets INSET_STANDARD = new Insets(8, 12, 8, 12);

    public static final Insets INSET_LARGO = new Insets(20, 12, 8, 12);



    //  PULSANTI

    /* Creiamo un pulsante standard adatto per sfondo chiaro  */

    public static JButton creaPulsante(String testo, Color sfondo) {

        JButton pulsante = new JButton(testo);

        pulsante.setFont(FONT_PULSANTE);

        pulsante.setBackground(sfondo);

        pulsante.setForeground(TESTO_SCURO);

        pulsante.setFocusPainted(false);

        return pulsante;

    }



    /*Variante per pulsanti su sfondo scuro come logout o rifiuta, dove il testo deve essere bianco*/

    public static JButton creaPulsanteTestoBianco(String testo, Color sfondo) {

        JButton pulsante = creaPulsante(testo, sfondo);

        pulsante.setForeground(Color.WHITE);

        return pulsante;

    }



    // BORDI

    /* Creiamo il bordo titolato usato per i riquadri di login/registrazione etc
    con colore del bordo e del titolo personalizzabili*/

    public static javax.swing.border.Border creaBordoTitolato(String titolo, Color coloreBordo, Color coloreTesto) {

        return BorderFactory.createTitledBorder(

                BorderFactory.createLineBorder(coloreBordo, 2),

                titolo, 0, 0, FONT_ETICHETTA, coloreTesto);

    }
}