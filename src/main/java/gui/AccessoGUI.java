package gui;

public class AccessoGUI {

	/**
	 * Punto di ingresso principale dell'applicazione.
	 * <p>
	 * Il metodo avvia l'interfaccia grafica all'interno dell'
	 * {@link javax.swing.SwingUtilities#invokeLater(Runnable)}, garantendo che
	 * la creazione e la visualizzazione del {@link MainFrame} avvengano nel
	 * rispetto del modello di threading di Swing (Event Dispatch Thread - EDT).
	 * Questo è fondamentale per evitare potenziali problemi di concorrenza
	 * e instabilità dell'interfaccia.
	 *
	 * @param args Argomenti passati da riga di comando (non utilizzati).
	 */
	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(MainFrame::new);
	}
}