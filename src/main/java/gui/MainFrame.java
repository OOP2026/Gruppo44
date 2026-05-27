package view; // Definisce l'appartenenza al pacchetto della vista grafico-testuale

import javax.swing.*; // Importa i componenti grafici Swing nativi
import java.awt.*; // Importa le classi per i layout grafici standard


               //SEZIONE 1 : GRAFICA BASE DEL MAIN FRAME//


public class MainFrame extends JFrame { // Estende la classe JFrame per diventare una finestra di sistema

    // Costruttore del frame principale dell'applicazione
    public MainFrame() {
        // Imposta il titolo testuale che apparirà nella barra superiore della finestra
        setTitle("Sistema di Gestione Orario Universitario");

        // Imposta la dimensione fissa della finestra (Larghezza = 900 pixel, Altezza = 650 pixel)
        setSize(900, 650);

        // Impedisce all'utente di ridimensionare la finestra per preservare le proporzioni
        setResizable(false);

        // Garantisce che il processo Java termini definitivamente quando l'utente clicca sulla 'X'
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Centra automaticamente la finestra sul monitor dell'utente calcolando la risoluzione
        setLocationRelativeTo(null);

        // Assegna un BorderLayout per permettere al pannello interno (mainPanel) di occupare tutto lo spazio
        setLayout(new BorderLayout());

        // Istanzia il pannello dinamico principale (sviluppato nella Sezione 2)
        MainPanel mainPanel = new MainPanel();

        // Posiziona il pannello principale al centro dell'area utile del frame
        add(mainPanel, BorderLayout.CENTER);

        // Rende la finestra e tutti i suoi componenti effettivamente visibili a schermo
        setVisible(true);
    }
}