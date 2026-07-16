package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardStudente extends JPanel {

    private final MainPanel mainPanel;
    private String[] dati; // nome, cognome, email, matricola, anno



    /**
     * Inizializza la {@code DashboardStudente}, configurando l'interfaccia grafica
     * e determinando la vista iniziale da mostrare (Login o Registrazione).
     * <p>
     * Il metodo imposta il layout, lo sfondo e seleziona il pannello da visualizzare
     * tramite il parametro {@code registrazione}. Se {@code true}, viene attivata la
     * vista di registrazione; se {@code false}, viene attivata la vista di login.
     *
     * @param mainPanel Il pannello principale dell'applicazione che funge da contenitore.
     * @param registrazione Valore booleano per decidere la vista: {@code true} per mostrare il form di iscrizione,
     * {@code false} per mostrare il form di login.
     */
    public DashboardStudente(MainPanel mainPanel, boolean registrazione) {
        this.mainPanel = mainPanel;
        setBackground(Stile.AZZURRO);
        setLayout(new BorderLayout());

        if (registrazione) {
            mostraRegistrazione();
        } else {
            mostraLogin();
        }
    }



    /**
     * Configura e visualizza il pannello di login per lo studente all'interno della dashboard.
     * <p>
     * Il metodo prepara l'interfaccia rimuovendo eventuali componenti precedenti e
     * inizializzando un {@link LoginPanel} con il titolo "ACCESSO STUDENTE" e il
     * colore identificativo {@code Stile.VERDE_CHIARO}.
     * <p>
     * Il comportamento del pulsante di accesso è così definito:
     * <ul>
     * <li>Recupera le credenziali dai campi del {@code LoginPanel}.</li>
     * <li>Invia una richiesta di autenticazione al {@link Controller} tramite
     * {@link Controller#loginStudente(String, String)}.</li>
     * <li>In caso di successo, aggiorna l'oggetto {@code dati} e invoca
     * {@link #mostraAreaPersonale()} per caricare la dashboard operativa.</li>
     * <li>In caso di errore, mostra una finestra di dialogo ({@link JOptionPane})
     * con il messaggio dell'eccezione catturata.</li>
     * </ul>
     */
    private void mostraLogin() {
        removeAll();
        LoginPanel login = new LoginPanel("ACCESSO STUDENTE", Stile.VERDE_CHIARO);

        login.getPulsanteAccedi().addActionListener(e -> {
            try {
                dati = Controller.getInstance().loginStudente(login.getEmail(), login.getPassword());
                mostraAreaPersonale();
            } catch (Exception errore) {
                JOptionPane.showMessageDialog(this, errore.getMessage(), "Accesso non riuscito", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(centra(login), BorderLayout.CENTER);
        revalidate();
        repaint();
    }



    /**
     * Configura e visualizza il form di registrazione per un nuovo studente all'interno
     * della dashboard.
     * <p>
     * Il metodo prepara l'interfaccia rimuovendo i componenti esistenti e creando
     * dinamicamente un modulo di ingresso composto da:
     * <ul>
     * <li>Campi testuali per nome, cognome, email, matricola e anno accademico.</li>
     * <li>Campo {@link JPasswordField} per la protezione della password.</li>
     * <li>Un pulsante di azione che, al click, invoca
     * {@link Controller#creaStudente(String, String, String, String, String, int)}.</li>
     * </ul>
     * <p>
     * In caso di registrazione corretta, i dati dell'utente vengono salvati nell'oggetto
     * {@code dati} e viene invocato {@link #mostraAreaPersonale()} per l'accesso
     * all'area riservata. Se l'operazione fallisce, viene mostrato un messaggio di errore
     * tramite {@link JOptionPane}.
     */
    private void mostraRegistrazione() {
        removeAll();

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(Stile.creaBordoTitolato("REGISTRAZIONE STUDENTE", Stile.VERDE_CHIARO, Stile.BLU_SCURO));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = Stile.INSET_STANDARD;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JTextField campoNome = new JTextField(18);
        JTextField campoCognome = new JTextField(18);
        JTextField campoEmail = new JTextField(18);
        JPasswordField campoPassword = new JPasswordField(18);
        JTextField campoMatricola = new JTextField(18);
        JComboBox<Integer> campoAnno = new JComboBox<>(new Integer[]{1, 2, 3});

        int riga = 0;
        riga = aggiungiCampo(form, gbc, riga, "Nome:", campoNome);
        riga = aggiungiCampo(form, gbc, riga, "Cognome:", campoCognome);
        riga = aggiungiCampo(form, gbc, riga, "Email:", campoEmail);
        riga = aggiungiCampo(form, gbc, riga, "Password:", campoPassword);
        riga = aggiungiCampo(form, gbc, riga, "Matricola:", campoMatricola);
        riga = aggiungiCampo(form, gbc, riga, "Anno accademico:", campoAnno);

        JButton pulsanteRegistrati = Stile.creaPulsante("REGISTRATI", Stile.VERDE_CHIARO);
        pulsanteRegistrati.addActionListener(e -> {
            try {
                dati = Controller.getInstance().creaStudente(
                        campoNome.getText().trim(), campoCognome.getText().trim(),
                        campoEmail.getText().trim(), new String(campoPassword.getPassword()),
                        campoMatricola.getText().trim(), (Integer) campoAnno.getSelectedItem());
                mostraAreaPersonale();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Registrazione non riuscita", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridy = riga;
        form.add(pulsanteRegistrati, gbc);

        add(centra(form), BorderLayout.CENTER);
        revalidate();
        repaint();
    }



    /**
     * Aggiunge una coppia etichetta-campo al pannello di layout specificato
     * utilizzando le impostazioni di {@link GridBagConstraints}.
     * <p>
     * Questo metodo funge da wrapper per {@link DashboardUtils#aggiungiCampo} e
     * automatizza la disposizione degli elementi in una griglia. Dopo l'aggiunta,
     * incrementa il contatore di riga per garantire che il componente successivo
     * sia posizionato correttamente sotto quello corrente.
     *
     * @param form Il {@link JPanel} di destinazione dove aggiungere gli elementi.
     * @param gbc L'oggetto {@link GridBagConstraints} che definisce le regole di layout.
     * @param riga L'indice della riga corrente in cui inserire i componenti.
     * @param etichetta Il testo dell'etichetta da visualizzare a fianco del campo.
     * @param campo Il componente {@link JComponent} (es. {@link JTextField}) da aggiungere.
     * @return L'indice della riga successiva libera ({@code riga + 1}).
     */
    private int aggiungiCampo(JPanel form, GridBagConstraints gbc, int riga, String etichetta, JComponent campo) {
        return DashboardUtils.aggiungiCampo(form, gbc, riga, etichetta, campo);
    }



    /**
     * Incapsula un pannello in un contenitore che ne garantisce il centraggio
     * all'interno dello spazio disponibile.
     * <p>
     * Il metodo utilizza un {@link GridBagLayout} per forzare il posizionamento
     * del componente {@code contenuto} esattamente al centro del pannello
     * {@code esterno}, impedendo che il componente si espanda per occupare l'intero
     * spazio disponibile nel layout padre.
     *
     * @param contenuto Il {@link JPanel} da centrare.
     * @return Un nuovo {@link JPanel} che funge da contenitore con il pannello
     * originale centrato al suo interno.
     */
    private JPanel centra(JPanel contenuto) {
        JPanel esterno = new JPanel(new GridBagLayout());
        esterno.setBackground(Stile.AZZURRO);
        esterno.add(contenuto);
        return esterno;
    }



    /**
     * Aggiorna l'interfaccia grafica per visualizzare l'area riservata dello studente.
     * <p>
     * Il metodo ridefinisce la vista principale costruendo una navigazione a schede (menu)
     * che permette allo studente di consultare le informazioni personali e accademiche:
     * <ul>
     * <li><b>Dati anagrafici:</b> Visualizzazione delle informazioni personali dell'utente.</li>
     * <li><b>Il mio orario:</b> Tabella o lista delle lezioni previste.</li>
     * <li><b>Variazioni:</b> Consultazione di eventuali spostamenti o modifiche all'orario.</li>
     * </ul>
     * <p>
     * La struttura del pannello è composta da:
     * <ul>
     * <li>Una barra superiore ({@code NORTH}) tramite {@link DashboardUtils#creaBarraSuperiore(String[], MainPanel)}.</li>
     * <li>Un menu di navigazione a schede ({@code CENTER}) tramite {@link PannelloRiutilizzabileMenu},
     * che ospita i pannelli specifici per le tre funzionalità principali.</li>
     * </ul>
     */
    private void mostraAreaPersonale() {
        removeAll();

        String[] nomeScelta = {"Dati anagrafici", "Il mio orario", "Variazioni"};
        JPanel[] pannelli = {
                pannelloDatiAnagrafici(),
                pannelloOrario(),
                pannelloVariazioni()
        };

        add(new PannelloRiutilizzabileMenu(nomeScelta, pannelli), BorderLayout.CENTER);
        add(DashboardUtils.creaBarraSuperiore(dati, mainPanel), BorderLayout.NORTH);
        revalidate();
        repaint();
    }




    /**
     * Crea e restituisce un pannello visualizzativo per i dati anagrafici dello studente.
     * <p>
     * Il pannello organizza le informazioni personali estratte dall'array {@code dati}
     * in una griglia verticale ({@link GridLayout}). Per ogni dato, viene invocato
     * il metodo {@link #rigaDato(String, String)} per generare una riga formattata
     * contenente l'etichetta del campo e il valore corrispondente.
     * <p>
     * La struttura dei dati prevista nell'array è:
     * <ul>
     * <li>{@code dati[0]}: Nome</li>
     * <li>{@code dati[1]}: Cognome</li>
     * <li>{@code dati[2]}: Email</li>
     * <li>{@code dati[3]}: Matricola</li>
     * <li>{@code dati[4]}: Anno accademico</li>
     * </ul>
     *
     * @return Un {@link JPanel} configurato con la disposizione dei dati anagrafici.
     */
    private JPanel pannelloDatiAnagrafici() {
        JPanel pannello = new JPanel(new GridLayout(5, 1, 0, 10));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        pannello.add(rigaDato("Nome:", dati[0]));
        pannello.add(rigaDato("Cognome:", dati[1]));
        pannello.add(rigaDato("Email:", dati[2]));
        pannello.add(rigaDato("Matricola:", dati[3]));
        pannello.add(rigaDato("Anno accademico:", dati[4]));
        return pannello;
    }


    /**
     * Crea una {@link JLabel} formattata per visualizzare una coppia etichetta-valore.
     * <p>
     * Questo metodo di utilità centralizza la creazione delle etichette nei pannelli
     * informativi, applicando lo stile di font predefinito definito nella classe
     * {@link Stile}.
     *
     * @param etichetta Il testo descrittivo del dato.
     * @param valore Il valore associato da visualizzare.
     * @return Una {@link JLabel} contenente il testo concatenato e formattato.
     */
    private JLabel rigaDato(String etichetta, String valore) {
        JLabel label = new JLabel(etichetta + " " + valore);
        label.setFont(Stile.FONT_TESTO);
        return label;
    }



    /**
     * Crea e restituisce un pannello che visualizza l'orario settimanale delle lezioni
     * per lo studente, basato sul suo anno accademico.
     * <p>
     * Il metodo inizializza un contenitore con layout verticale ({@link BoxLayout})
     * e popola l'interfaccia recuperando i dati dal {@link Controller}.
     * <p>
     * La logica di visualizzazione prevede:
     * <ul>
     * <li>Definizione dei giorni della settimana (Lunedì-Venerdì).</li>
     * <li>Richiesta al {@code Controller} di un array di {@link ArrayList} contenente
     * le lezioni organizzate per giorno.</li>
     * <li>Utilizzo di {@link DashboardUtils#aggiornaLezioni(JPanel, String[], ArrayList[])}
     * per eseguire il rendering grafico della tabella oraria.</li>
     * <li>Gestione delle eccezioni: in caso di errori di connessione o recupero,
     * viene visualizzato un messaggio informativo all'interno del pannello.</li>
     * </ul>
     *
     * @return Un {@link JPanel} contenente l'orario settimanale delle lezioni.
     */
    private JPanel pannelloOrario() {
        JPanel pannello = new JPanel();
        pannello.setLayout(new BoxLayout(pannello, BoxLayout.Y_AXIS));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] giorni = {"LUNEDI", "MARTEDI", "MERCOLEDI", "GIOVEDI", "VENERDI"};

        try {
            ArrayList<String>[] lezioni = Controller.getInstance().getLezioniStudente();
            DashboardUtils.aggiornaLezioni(pannello, giorni, lezioni);
        } catch (Exception e) {
            pannello.add(new JLabel("Errore nel recupero dell'orario: " + e.getMessage()));
        }
        return pannello;
    }




    /**
     * Crea e restituisce un pannello per la visualizzazione delle variazioni
     * occorse alle lezioni (es. Spostamenti, cancellazioni o cambi d'aula).
     * <p>
     * Il metodo recupera dal {@link Controller} un elenco di stringhe descrittive
     * rappresentanti le variazioni correnti. Il pannello utilizza un layout verticale
     * ({@link BoxLayout}) per elencare ciascuna variazione come una {@link JLabel}
     * formattata.
     * <p>
     * Gestisce i seguenti scenari:
     * <ul>
     * <li><b>Lista vuota:</b> Visualizza un messaggio informativo ("Nessuna variazione al momento.").</li>
     * <li><b>Lista non vuota:</b> Itera su ogni stringa e crea un'etichetta con lo stile {@code Stile.FONT_TESTO}.</li>
     * <li><b>Eccezione:</b> Intercetta errori durante il recupero dei dati e mostra un avviso testuale all'interno del pannello.</li>
     * </ul>
     *
     * @return Un {@link JPanel} contenente l'elenco aggiornato delle variazioni.
     */
    private JPanel pannelloVariazioni() {
        JPanel pannello = new JPanel();
        pannello.setLayout(new BoxLayout(pannello, BoxLayout.Y_AXIS));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {
            List<String> variazioni = Controller.getInstance().getVariazioniStudente();
            if (variazioni.isEmpty()) {
                pannello.add(new JLabel("Nessuna variazione al momento."));
            }
            for (String variazione : variazioni) {
                JLabel label = new JLabel(variazione);
                label.setFont(Stile.FONT_TESTO);
                pannello.add(label);
            }
        } catch (Exception e) {
            pannello.add(new JLabel("Errore nel recupero delle variazioni: " + e.getMessage()));
        }
        return pannello;
    }
}