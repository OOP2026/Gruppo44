package gui;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {


    /**
     * Inizializza il pannello principale che funge da contenitore dinamico per
     * le varie schermate dell'applicazione.
     * <p>
     * Il costruttore imposta un layout di tipo {@link BorderLayout}, che permette
     * di gestire facilmente la sostituzione del contenuto centrale dell'interfaccia.
     * All'avvio, invoca il metodo {@link #mostraHome()} per rendere visibile
     * la schermata iniziale di login.
     */
    public MainPanel() {
        setLayout(new BorderLayout());
        mostraHome();
    }


    /**
     * Ripristina l'interfaccia utente alla schermata iniziale (Home).
     * <p>
     * Questo metodo funge da punto di reset della navigazione: inizializza una nuova
     * istanza di {@link DashboardHome} passando un riferimento a {@code this}
     * (il {@code MainPanel} corrente) per permettere alla schermata di home di
     * gestire le transizioni verso altre sezioni dell'applicazione.
     * <p>
     * Il metodo delega la sostituzione effettiva dei componenti grafici a
     * {@link #cambiaSchermata(JPanel)}.
     */
    public void mostraHome() {
        cambiaSchermata(new DashboardHome(this));
    }


    /**
     * Esegue la transizione dell'interfaccia verso la schermata di login dedicata agli studenti.
     * <p>
     * Questo metodo istanzia il componente {@link DashboardStudente} in modalità
     * di login (passando {@code false} come secondo parametro per indicare che
     * l'utente non è ancora autenticato o che deve mostrare il form di accesso).
     * <p>
     * Il metodo delega la sostituzione effettiva del pannello corrente a
     * {@link #cambiaSchermata(JPanel)}.
     */
    public void mostraLoginStudente() {
        cambiaSchermata(new DashboardStudente(this, false));
    }


    /**
     * Esegue la transizione dell'interfaccia verso la schermata di registrazione
     * per un nuovo studente.
     * <p>
     * Questo metodo istanzia il componente {@link DashboardStudente} in modalità
     * di registrazione (passando {@code true} come secondo parametro), abilitando
     * la visualizzazione dei campi e delle logiche necessarie alla creazione di
     * un nuovo profilo utente.
     * <p>
     * Il metodo delega la sostituzione effettiva del pannello corrente a
     * {@link #cambiaSchermata(JPanel)}.
     */
    public void mostraRegistrazioneStudente() {
        cambiaSchermata(new DashboardStudente(this, true));
    }


    /**
     * Esegue la transizione dell'interfaccia verso la schermata di login
     * dedicata ai docenti.
     * <p>
     * Questo metodo inizializza il componente {@link DashboardDocente} in modalità
     * di login (passando {@code false} come secondo parametro), predisponendo
     * l'interfaccia per l'inserimento delle credenziali di accesso riservate
     * al corpo docente.
     * <p>
     * Il metodo delega la sostituzione effettiva del pannello corrente a
     * {@link #cambiaSchermata(JPanel)}.
     */
    public void mostraLoginDocente() {
        cambiaSchermata(new DashboardDocente(this, false));
    }


    /**
     * Esegue la transizione dell'interfaccia verso la schermata di registrazione
     * per un nuovo docente.
     * <p>
     * Questo metodo istanzia il componente {@link DashboardDocente} in modalità
     * di registrazione (passando {@code true} come secondo parametro), abilitando
     * la visualizzazione dei campi necessari per la creazione di un nuovo
     * profilo docente.
     * <p>
     * Il metodo delega la sostituzione effettiva del pannello corrente a
     * {@link #cambiaSchermata(JPanel)}.
     */
    public void mostraRegistrazioneDocente() {
        cambiaSchermata(new DashboardDocente(this, true));
    }



    /**
     * Sostituisce il contenuto corrente del pannello con una nuova schermata.
     * <p>
     * Questo metodo gestisce il ciclo di vita della transizione visiva in Swing:
     * <ul>
     * <li><b>Rimozione:</b> Rimuove tutti i componenti esistenti dal pannello ({@link #removeAll()}).</li>
     * <li><b>Inserimento:</b> Aggiunge il nuovo componente al centro ({@link BorderLayout#CENTER}).</li>
     * <li><b>Aggiornamento:</b> Notifica al gestore di layout di ricalcolare le
     * posizioni ({@link #revalidate()}) e richiede il ridisegno grafico ({@link #repaint()}).</li>
     * </ul>
     *
     * @param nuovaSchermata Il {@link JPanel} che deve diventare la vista attiva.
     */
    public void cambiaSchermata(JPanel nuovaSchermata) {
        this.removeAll();
        this.add(nuovaSchermata, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }
}