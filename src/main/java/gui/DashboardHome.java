package gui;

import javax.swing.*;
import java.awt.*;

public class DashboardHome extends JPanel {


    /**
     * Inizializza la dashboard principale dell'applicazione (Home), configurando
     * la struttura di base dell'interfaccia utente.
     * <p>
     * Il metodo imposta lo sfondo e il layout, aggiungendo i componenti fondamentali:
     * <ul>
     * <li>Un'intestazione nella parte superiore ({@code NORTH}) tramite {@code creaIntestazione()}.</li>
     * <li>Una griglia di navigazione o contenuto centrale ({@code CENTER}) tramite {@code creaGrigliaColonne(mainPanel)}.</li>
     * </ul>
     *
     * @param mainPanel Il pannello principale dell'applicazione che gestisce la navigazione tra le viste.
     */
    public DashboardHome(MainPanel mainPanel) {
        setBackground(Stile.AZZURRO);
        setLayout(new BorderLayout());

        add(creaIntestazione(), BorderLayout.NORTH);
        add(creaGrigliaColonne(mainPanel), BorderLayout.CENTER);
    }



    /**
     * Crea e restituisce il pannello dell'intestazione per la schermata Home.
     * <p>
     * Il pannello utilizza un {@link GridLayout} a 3 righe per organizzare verticalmente
     * le informazioni istituzionali e le istruzioni di navigazione:
     * <ul>
     * <li><b>Nome Ateneo:</b> Titolo principale con font e colore distintivi.</li>
     * <li><b>Sottotitolo:</b> Descrizione del sistema di gestione orari.</li>
     * <li><b>Istruzione:</b> Messaggio di guida per l'utente sulla selezione del profilo.</li>
     * </ul>
     *
     * @return Un {@link JPanel} configurato con le etichette dell'intestazione centrata.
     */
    private JPanel creaIntestazione() {
        JPanel pannello = new JPanel(new GridLayout(3, 1, 0, 8));
        pannello.setBackground(Stile.AZZURRO);
        pannello.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));

        JLabel lblAteneo = new JLabel("UNIVERSITÀ DEGLI STUDI DI NAPOLI FEDERICO II", SwingConstants.CENTER);
        lblAteneo.setFont(Stile.FONT_TITOLO_MEDIO);
        lblAteneo.setForeground(Stile.BLU_SCURO);

        JLabel lblBenvenuto = new JLabel("Benvenuto nel Sistema di Gestione Orari", SwingConstants.CENTER);
        lblBenvenuto.setFont(Stile.FONT_SOTTOTITOLO);
        lblBenvenuto.setForeground(Stile.TESTO_CHIARO);

        JLabel lblIstruzione = new JLabel("Seleziona il tuo profilo per accedere o registrarti:", SwingConstants.CENTER);
        lblIstruzione.setFont(Stile.FONT_ISTRUZIONE);
        lblIstruzione.setForeground(Stile.TESTO_SCURO);

        pannello.add(lblAteneo);
        pannello.add(lblBenvenuto);
        pannello.add(lblIstruzione);
        return pannello;
    }



    /**
     * Crea e restituisce un pannello a due colonne che organizza le opzioni di
     * accesso per le due tipologie di utenti: Studente e Docente.
     * <p>
     * Il metodo utilizza un {@link GridLayout} con 1 riga e 2 colonne, impostando
     * uno spazio orizzontale di 30 pixel tra le colonne per una chiara separazione
     * visiva. Ogni colonna viene generata tramite metodi di supporto specifici.
     *
     * @param mainPanel Il pannello principale dell'applicazione, passato ai metodi
     * di creazione delle colonne per gestire i cambi di schermata.
     * @return Un {@link JPanel} contenente la griglia delle colonne Studente e Docente.
     */
    private JPanel creaGrigliaColonne(MainPanel mainPanel) {
        JPanel griglia = new JPanel(new GridLayout(1, 2, 30, 0));
        griglia.setBackground(Stile.AZZURRO);
        griglia.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        griglia.add(creaColonnaStudente(mainPanel));
        griglia.add(creaColonnaDocente(mainPanel));

        return griglia;
    }



    /**
     * Crea e restituisce il pannello colonna dedicato al portale studente.
     * <p>
     * Il metodo inizializza una colonna base con il titolo "PORTALE STUDENTE" e
     * aggiunge i pulsanti di navigazione necessari per l'interazione:
     * <ul>
     * <li><b>Accedi:</b> Reindirizza alla schermata di login studente.</li>
     * <li><b>Registrati:</b> Reindirizza alla schermata di registrazione studente.</li>
     * </ul>
     * I pulsanti sono configurati con stili differenziati per distinguere l'azione
     * principale (accedi) da quella secondaria (registrati).
     *
     * @param mainPanel Il pannello principale dell'applicazione, utilizzato per
     * eseguire il cambio vista all'interno del flusso di navigazione.
     * @return Un {@link JPanel} configurato come colonna per lo studente.
     */
    private JPanel creaColonnaStudente(MainPanel mainPanel) {
        JPanel colonna = creaColonnaBase("PORTALE STUDENTE", Stile.VERDE_CHIARO);

        JButton pulsanteAccedi = Stile.creaPulsante("ACCEDI", Stile.VERDE_CHIARO);
        pulsanteAccedi.setPreferredSize(Stile.PULSANTE_GRANDE);
        pulsanteAccedi.addActionListener(e -> mainPanel.mostraLoginStudente());

        JButton pulsanteRegistrati = Stile.creaPulsante("REGISTRATI", Color.WHITE);
        pulsanteRegistrati.setBorder(BorderFactory.createLineBorder(Stile.VERDE_CHIARO, 2));
        pulsanteRegistrati.setPreferredSize(Stile.PULSANTE_GRANDE);
        pulsanteRegistrati.addActionListener(e -> mainPanel.mostraRegistrazioneStudente());

        aggiungiPulsanti(colonna, pulsanteAccedi, pulsanteRegistrati);
        return colonna;
    }



    /**
     * Crea e restituisce il pannello colonna dedicato al portale docente.
     * <p>
     * Il metodo inizializza una colonna base con il titolo "PORTALE DOCENTE" e
     * aggiunge i pulsanti di navigazione necessari per l'interazione:
     * <ul>
     * <li><b>Accedi:</b> Reindirizza alla schermata di login docente.</li>
     * <li><b>Registrati:</b> Reindirizza alla schermata di registrazione docente.</li>
     * </ul>
     * Lo stile utilizzato ({@code Stile.BLU_CHIARO}) permette di mantenere la
     * distinzione visiva tra le aree riservate ai due differenti profili utente.
     *
     * @param mainPanel Il pannello principale dell'applicazione, utilizzato per
     * gestire la transizione tra le diverse viste.
     * @return Un {@link JPanel} configurato come colonna per il docente.
     */
    private JPanel creaColonnaDocente(MainPanel mainPanel) {
        JPanel colonna = creaColonnaBase("PORTALE DOCENTE", Stile.BLU_CHIARO);

        JButton pulsanteAccedi = Stile.creaPulsante("ACCEDI", Stile.BLU_CHIARO);
        pulsanteAccedi.setPreferredSize(Stile.PULSANTE_GRANDE);
        pulsanteAccedi.addActionListener(e -> mainPanel.mostraLoginDocente());

        JButton pulsanteRegistrati = Stile.creaPulsante("REGISTRATI", Color.WHITE);
        pulsanteRegistrati.setBorder(BorderFactory.createLineBorder(Stile.BLU_CHIARO, 2));
        pulsanteRegistrati.setPreferredSize(Stile.PULSANTE_GRANDE);
        pulsanteRegistrati.addActionListener(e -> mainPanel.mostraRegistrazioneDocente());

        aggiungiPulsanti(colonna, pulsanteAccedi, pulsanteRegistrati);
        return colonna;
    }



    /**
     * Crea e restituisce un pannello base configurato come colonna per la dashboard,
     * caratterizzato da un bordo titolato personalizzato.
     * <p>
     * Il metodo prepara la struttura comune utilizzata sia per la colonna dello
     * studente che per quella del docente, garantendo uniformità estetica:
     * <ul>
     * <li>Utilizza {@link GridBagLayout} per un posizionamento preciso degli elementi interni.</li>
     * <li>Imposta lo sfondo a bianco per contrastare con il colore del bordo.</li>
     * <li>Applica un bordo titolato tramite {@link Stile#creaBordoTitolato(String, Color, Color)},
     * usando il {@code colore} specificato per il titolo e {@link Stile#BLU_SCURO} per
     * i dettagli del bordo.</li>
     * </ul>
     *
     * @param titolo Il testo da visualizzare nel bordo titolato del pannello.
     * @param colore Il colore associato al titolo e al bordo per distinguere il profilo.
     * @return Un {@link JPanel} configurato con bordo titolato e layout a griglia.
     */
    private JPanel creaColonnaBase(String titolo, Color colore) {
        JPanel colonna = new JPanel(new GridBagLayout());
        colonna.setBackground(Color.WHITE);
        colonna.setBorder(Stile.creaBordoTitolato(titolo, colore, Stile.BLU_SCURO));
        return colonna;
    }


    /**
     * Posiziona i pulsanti di azione all'interno del pannello colonna utilizzando
     * un {@link GridBagLayout}.
     * <p>
     * Il metodo organizza i due pulsanti (Accedi e Registrati) in una struttura
     * a colonna singola, applicando dei margini ({@link Insets}) standard per garantire
     * una spaziatura coerente tra i componenti e i bordi del pannello.
     *
     * @param colonna Il {@link JPanel} di destinazione.
     * @param pulsanteAccedi Il componente {@link JButton} per l'azione di accesso.
     * @param pulsanteRegistrati Il componente {@link JButton} per l'azione di registrazione.
     */
    private void aggiungiPulsanti(JPanel colonna, JButton pulsanteAccedi, JButton pulsanteRegistrati) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 25, 15, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        gbc.gridy = 0;
        colonna.add(pulsanteAccedi, gbc);
        gbc.gridy = 1;
        colonna.add(pulsanteRegistrati, gbc);
    }
}