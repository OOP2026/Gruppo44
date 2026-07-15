package gui;

public class AccessoGUI {
	public static void main(String[] args) {
		// Avvia l'interfaccia grafica all'interno dell'Event Dispatch Thread (sicurezza di Swing)
        // Instanzia il frame principale che fa partire a cascata tutta la grafica
        javax.swing.SwingUtilities.invokeLater(MainFrame::new);
	}
}