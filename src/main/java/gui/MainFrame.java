package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {


    /**
     * Inizializza il frame principale dell'applicazione.
     * <p>
     * Il costruttore configura le proprietà fondamentali della finestra, tra cui:
     * <ul>
     * <li><b>Titolo:</b> Impostato su "Sistema di Gestione Orario Universitario".</li>
     * <li><b>Dimensioni:</b> Fissate a 900x650 pixel, con il ridimensionamento disabilitato
     * per mantenere l'integrità del layout.</li>
     * <li><b>Posizionamento:</b> La finestra viene centrata automaticamente sullo schermo.</li>
     * <li><b>Gestione eventi:</b> Imposta la chiusura dell'applicazione alla chiusura del frame.</li>
     * </ul>
     * <p>
     * Il frame utilizza un {@link BorderLayout} e ospita al centro un'istanza di
     * {@link MainPanel}, che funge da contenitore principale per le viste dinamiche
     * dell'applicazione.
     */
    public MainFrame() {
        setTitle("Sistema di Gestione Orario Universitario");
        setSize(900, 650);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centra la finestra sullo schermo
        setLayout(new BorderLayout());

        MainPanel mainPanel = new MainPanel();
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true); //la rende visibile
    }
}