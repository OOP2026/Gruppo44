package gui;
import gui.MainFrame;

public class MainTest {
    public static void main(String[] args) {
        // Avvia l'interfaccia grafica all'interno dell'Event Dispatch Thread (sicurezza di Swing)
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Instanzia il frame principale che fa partire a cascata tutta la grafica
                new MainFrame();
                System.out.println("[SISTEMA] Applicazione Orari Federico II avviata con successo.");
            }
        });
    }
}