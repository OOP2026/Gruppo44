package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DashboardDocente extends JPanel {

    protected final MainPanel mainPanel;
    protected String[] dati; // contiene nome[0], cognome[1], email[2] riempito dopo il login

    /**
     * Inizializza la {@code DashboardDocente}, configurando l'interfaccia grafica
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
    public DashboardDocente(MainPanel mainPanel, boolean registrazione) {
        this.mainPanel = mainPanel;
        setBackground(Stile.AZZURRO);
        setLayout(new BorderLayout());

        // registrazione = true mostra il form di iscrizione, false mostra il login
        if (registrazione) {
            mostraRegistrazione();
        } else {
            mostraLogin();
        }
    }


    /**
     * Inizializza una versione semplificata della {@code DashboardDocente}, utilizzata
     * dalla sottoclasse {@code DashboardResponsabile} che opera in uno stato già autenticato.
     * <p>
     * Questo costruttore configura le impostazioni grafiche di base (layout e colore
     * di sfondo) senza avviare i processi di autenticazione o registrazione.
     * È progettato per essere richiamata dalla sottoclasse {@link DashboardResponsabile},
     * la quale gestirà autonomamente la visualizzazione
     * dei contenuti specifici tramite metodi dedicati (es. {@code mostraAreaPersonale()}).
     *
     * @param mainPanel Il pannello principale dell'applicazione che funge da contenitore.
     */
    protected DashboardDocente(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        setBackground(Stile.AZZURRO);
        setLayout(new BorderLayout());
    }

    /**
     * Configura e visualizza il pannello di login all'interno della dashboard.
     * <p>
     * Il metodo rimuove i componenti correnti, inizializza un nuovo {@link LoginPanel}
     * e registra l'{@link ActionListener} per il pulsante di accesso.
     * <p>
     * La logica di autenticazione prevede:
     * <ul>
     * <li>Un controllo di credenziali fisse per il profilo "Responsabile".</li>
     * <li>Una richiesta di validazione al {@link Controller} per i profili docente.</li>
     * </ul>
     * In caso di successo, viene caricata la vista pertinente (Area Personale o Dashboard Responsabile);
     * in caso di errore, viene mostrato un {@link JOptionPane} con il messaggio dell'eccezione.
     */
    private void mostraLogin() {
        removeAll();
        LoginPanel login = new LoginPanel("ACCESSO DOCENTE", Stile.BLU_CHIARO);

        login.getPulsanteAccedi().addActionListener(e -> {
            String email = login.getEmail();
            String password = login.getPassword();

            // il Responsabile ha un accesso con credenziali fisse
            //DA MODIFICARE
            if (email.equals("resp@unina.it") && password.equals("resp")) {
                mainPanel.cambiaSchermata(new DashboardResponsabile(mainPanel, email));
                return;
            }

            try {
                dati = Controller.getInstance().loginDocente(email, password);
                mostraAreaPersonale();
            } catch (Exception errore) {
                JOptionPane.showMessageDialog(this, errore.getMessage(), "Accesso negato.", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(centra(login), BorderLayout.CENTER);
        revalidate();
        repaint();
    }


    /**
     * Configura e visualizza il modulo di registrazione per un nuovo docente.
     * <p>
     * Il metodo prepara un pannello basato su {@link GridBagLayout} contenente i campi
     * di ingresso necessari (Nome, Cognome, Email, Password).
     * <p>
     * Al click del pulsante di registrazione, il metodo tenta di creare un nuovo utente
     * tramite {@link Controller#creaDocente(String, String, String, String)}.
     * Se l'operazione ha successo, il sistema passa alla vista dell'area personale;
     * in caso contrario, viene visualizzato un messaggio di errore tramite {@link JOptionPane}.
     */
    private void mostraRegistrazione() {
        removeAll();

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(Stile.creaBordoTitolato("REGISTRAZIONE DOCENTE", Stile.BLU_CHIARO, Stile.BLU_SCURO));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = Stile.INSET_STANDARD;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JTextField campoNome = new JTextField(18);
        JTextField campoCognome = new JTextField(18);
        JTextField campoEmail = new JTextField(18);
        JPasswordField campoPassword = new JPasswordField(18);

        int riga = 0;
        riga = aggiungiCampo(form, gbc, riga, "Nome:", campoNome);
        riga = aggiungiCampo(form, gbc, riga, "Cognome:", campoCognome);
        riga = aggiungiCampo(form, gbc, riga, "Email:", campoEmail);
        riga = aggiungiCampo(form, gbc, riga, "Password:", campoPassword);

        JButton pulsanteRegistrati = Stile.creaPulsante("REGISTRATI", Stile.BLU_CHIARO);
        pulsanteRegistrati.addActionListener(e -> {
            try {
                dati = Controller.getInstance().creaDocente(
                        campoNome.getText().trim(), campoCognome.getText().trim(),
                        campoEmail.getText().trim(), new String(campoPassword.getPassword()));
                mostraAreaPersonale();
            } catch (Exception errore) {
                JOptionPane.showMessageDialog(this, errore.getMessage(), "Registrazione non riuscita", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridy = riga;
        form.add(pulsanteRegistrati, gbc);

        add(centra(form), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    /**
     * Aggiunge una coppia etichetta-campo al pannello specificato, gestendo il posizionamento
     * tramite {@link GridBagConstraints} e restituendo l'indice della riga successiva.
     * <p>
     * Questo metodo funge da wrapper per {@link DashboardUtils#aggiungiCampo(JPanel, GridBagConstraints, int, String, JComponent)},
     * centralizzando la logica di layout per garantire coerenza estetica tra i diversi
     * form dell'applicazione (es. Login, Registrazione).
     *
     * @param form Il pannello {@link JPanel} in cui aggiungere i componenti.
     * @param gbc I vincoli di layout {@link GridBagConstraints} da utilizzare.
     * @param riga L'indice della riga corrente in cui inserire i componenti.
     * @param etichetta Il testo della label da visualizzare accanto al campo.
     * @param campo Il componente di ingresso da aggiungere.
     * @return L'indice della riga successiva libera nel layout.
     */
    protected int aggiungiCampo(JPanel form, GridBagConstraints gbc, int riga, String etichetta, JComponent campo) {
        return DashboardUtils.aggiungiCampo(form, gbc, riga, etichetta, campo);
    }


    /**
     * Inserisce il pannello fornito all'interno di un contenitore che lo mantiene centrato.
     * <p>
     * Il metodo crea un pannello esterno con {@link GridBagLayout} configurato per
     * posizionare il componente {@code contenuto} al centro dello spazio disponibile.
     * Questo metodo è utilizzato per garantire un allineamento grafico coerente
     * tra le diverse viste della dashboard.
     *
     * @param contenuto Il {@link JPanel} che si desidera centrare.
     * @return Un nuovo {@link JPanel} che funge da wrapper e contiene il pannello
     * passato come argomento centrato.
     */
    protected JPanel centra(JPanel contenuto) {
        JPanel esterno = new JPanel(new GridBagLayout());
        esterno.setBackground(Stile.AZZURRO);
        esterno.add(contenuto);
        return esterno;
    }

    /**
     * Configura e visualizza la schermata principale dell'area personale dopo l'autenticazione.
     * <p>
     * Il metodo inizializza un {@link PannelloRiutilizzabileMenu} che organizza le diverse
     * funzionalità disponibili (Dati anagrafici, Insegnamenti, Lezioni, ecc.) tramite
     * un sistema a schede o menu laterale.
     * <p>
     * La struttura dell'interfaccia è composta da:
     * <ul>
     * <li>Un menu centrale basato su un array di titoli ({@code nomeScelta}) e
     * i relativi pannelli di contenuto ({@code pannelli}).</li>
     * <li>Una barra superiore informativa creata tramite {@link DashboardUtils#creaBarraSuperiore(String[], MainPanel)}.</li>
     * </ul>
     * Questa vista è progettata per essere estensibile: le sottoclassi, come
     * {@link DashboardResponsabile}, possono ridefinire il comportamento per mostrare
     * voci di menu differenziate.
     */
    protected void mostraAreaPersonale() {
        removeAll();

        String[] nomeScelta = {"Dati anagrafici", "I miei insegnamenti", "Le mie lezioni", "Variazioni", "Richiedi spostamento", "I miei vincoli"};
        JPanel[] pannelli = {
                pannelloDatiAnagrafici(),
                pannelloInsegnamenti(),
                pannelloLezioni(),
                pannelloVariazioni(),
                pannelloRichiestaSpostamento(),
                pannelloVincoli()
        };

        add(new PannelloRiutilizzabileMenu(nomeScelta, pannelli), BorderLayout.CENTER);
        add(DashboardUtils.creaBarraSuperiore(dati, mainPanel), BorderLayout.NORTH);
        revalidate();
        repaint();
    }



    /**
     * Crea e restituisce un pannello contenente le informazioni anagrafiche del docente.
     * <p>
     * Il metodo organizza i dati (nome, cognome, email) in un layout a griglia ({@link GridLayout})
     * con 3 righe e 1 colonna, applicando un bordo vuoto per migliorare la leggibilità.
     * Ogni dato viene formattato tramite il metodo di utilità {@code rigaDato(String, String)}.
     * <p>
     * I dati vengono estratti dall'array {@code dati}, assumendo il seguente ordine:
     * <ul>
     * <li>{@code dati[0]}: Nome</li>
     * <li>{@code dati[1]}: Cognome</li>
     * <li>{@code dati[2]}: Email</li>
     * </ul>
     *
     * @return Un {@link JPanel} configurato con le etichette e i valori anagrafici.
     */
    protected JPanel pannelloDatiAnagrafici() {
        JPanel pannello = new JPanel(new GridLayout(3, 1, 0, 10));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        pannello.add(rigaDato("Nome:", dati[0]));
        pannello.add(rigaDato("Cognome:", dati[1]));
        pannello.add(rigaDato("Email:", dati[2]));
        return pannello;
    }



    /**
     * Crea e restituisce un'etichetta formattata che associa una descrizione a un valore specifico.
     * <p>
     * Il metodo combina l'etichetta (es. "Nome:") e il valore (es. "Mario") in un'unica stringa,
     * applicando lo stile tipografico definito in {@code Stile.FONT_TESTO}.
     *
     * @param etichetta Il testo descrittivo del dato.
     * @param valore Il contenuto effettivo da visualizzare.
     * @return Un {@link JLabel} configurato con il font standard dell'applicazione.
     */
    protected JLabel rigaDato(String etichetta, String valore) {
        JLabel label = new JLabel(etichetta + " " + valore);
        label.setFont(Stile.FONT_TESTO);
        return label;
    }

    /**
     * Crea e restituisce un pannello che visualizza l'elenco degli insegnamenti del docente
     * e fornisce un form per la proposta di un nuovo corso.
     * <p>
     * Il pannello è organizzato in tre sezioni principali:
     * <ul>
     * <li>Un'intestazione informativa.</li>
     * <li>Un pannello lista che viene popolato dinamicamente tramite {@link #aggiornaListaInsegnamenti(JPanel)}.</li>
     * <li>Un form di ingresso per l'inserimento di un nuovo insegnamento, che interagisce
     * con il {@link Controller} per la persistenza dei dati.</li>
     * </ul>
     * <p>
     * In caso di successo durante l'inserimento, il campo di ingresso viene svuotato e
     * la lista degli insegnamenti viene aggiornata automaticamente. Eventuali errori
     * vengono notificati all'utente tramite {@link JOptionPane}.
     *
     * @return Un {@link JPanel} configurato con la lista degli insegnamenti e il form di inserimento.
     */
    protected JPanel pannelloInsegnamenti() {
        JPanel pannello = DashboardUtils.creaPannelloGenerico();

        JPanel lista = DashboardUtils.creaLista();

        aggiornaListaInsegnamenti(lista);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = Stile.INSET_STANDARD;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JTextField campoNome = new JTextField(18);
        int riga = aggiungiCampo(form, gbc, 0, "Nuovo insegnamento:", campoNome);

        JButton pulsanteProponi = Stile.creaPulsante("PROPONI", Stile.BLU_CHIARO);
        pulsanteProponi.addActionListener(e -> {
            try {
                Controller.getInstance().creaInsegnamento(campoNome.getText().trim(), 0, 0);
                campoNome.setText("");
                aggiornaListaInsegnamenti(lista);
            } catch (Exception errore) {
                JOptionPane.showMessageDialog(this, errore.getMessage(), "Inserimento insegnamento non riuscito", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridy = riga;
        form.add(pulsanteProponi, gbc);

        pannello.add(new JLabel("I tuoi insegnamenti:"), BorderLayout.NORTH);
        pannello.add(lista, BorderLayout.CENTER);
        pannello.add(form, BorderLayout.SOUTH);
        return pannello;
    }


    /**
     * Aggiorna il contenuto del pannello lista con gli insegnamenti recuperati dal database.
     * <p>
     * Il metodo esegue le seguenti operazioni:
     * <ul>
     * <li>Rimuove i componenti esistenti nel pannello {@code lista}.</li>
     * <li>Interroga il {@link Controller} per ottenere l'elenco aggiornato degli insegnamenti
     * del docente tramite {@link Controller#getInsegnamentiDocente()}.</li>
     * <li>Formatta ogni elemento estraendo solo il nome (prima della virgola) e lo
     * aggiunge come {@link JLabel} al pannello.</li>
     * <li>Gestisce il caso di lista vuota o di errori durante la comunicazione con il
     * sistema, visualizzando un messaggio informativo all'interno del pannello.</li>
     * </ul>
     *
     * @param lista Il {@link JPanel} che funge da contenitore per le etichette degli insegnamenti.
     */
    private void aggiornaListaInsegnamenti(JPanel lista) {
        lista.removeAll();
        try {
            List<String> insegnamenti = Controller.getInstance().getInsegnamentiDocente();
            if (insegnamenti.isEmpty()) {
                lista.add(new JLabel("Nessun insegnamento proposto."));
            }
            for (String insegnamento : insegnamenti) {
                String nome = insegnamento.split(",")[0]; // il controller restituisce "nome, anno X, Y CFU"
                JLabel riga = new JLabel("• " + nome);
                riga.setFont(Stile.FONT_TESTO);
                lista.add(riga);
            }
        } catch (Exception errore) {
            lista.add(new JLabel("Errore nel recupero degli insegnamenti: " + errore.getMessage()));
        }
        lista.revalidate();
        lista.repaint();
    }


    /**
     * Crea e restituisce un pannello che visualizza il calendario settimanale delle
     * lezioni del docente.
     * <p>
     * Il metodo organizza le informazioni in un layout verticale ({@link BoxLayout}),
     * strutturando le lezioni suddivise per i cinque giorni lavorativi.
     * <p>
     * La logica di recupero e rendering prevede:
     * <ul>
     * <li>L'interrogazione del {@link Controller} per ottenere l'elenco delle lezioni
     * organizzato per giorno (array di {@link ArrayList} di {@link String}).</li>
     * <li>L'invocazione di {@link DashboardUtils#aggiornaLezioni(JPanel, String[], ArrayList[])}
     * per popolare il pannello con i dati formattati.</li>
     * <li>La gestione di eventuali eccezioni durante il recupero dei dati, visualizzando
     * un messaggio di errore direttamente nell'interfaccia.</li>
     * </ul>
     *
     * @return Un {@link JPanel} contenente la rappresentazione visiva dell'orario settimanale.
     */
    protected JPanel pannelloLezioni() {
        JPanel pannello = new JPanel();
        pannello.setLayout(new BoxLayout(pannello, BoxLayout.Y_AXIS));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] giorni = {"LUNEDI", "MARTEDI", "MERCOLEDI", "GIOVEDI", "VENERDI"};
        try {
            ArrayList<String>[] lezioni = Controller.getInstance().getLezioniDocente();
            DashboardUtils.aggiornaLezioni(pannello, giorni, lezioni);
        } catch (Exception errore) {
            pannello.add(new JLabel("Errore nel recupero delle lezioni: " + errore.getMessage()));
        }
        return pannello;
    }



    /**
     * Crea e restituisce un pannello per l'invio di una richiesta di spostamento lezione.
     * <p>
     * Il form permette al docente di specificare i dettagli della lezione originale e
     * i parametri desiderati per lo spostamento. I dati inseriti vengono inviati al
     * {@link Controller} per la validazione e la persistenza.
     * <p>
     * <b>Requisiti di formato per l'ingresso:</b>
     * <ul>
     * <li>Data (Giorno originale/richiesto): YYYY-MM-DD.</li>
     * <li>Orario (Ora originale/inizio/fine): HH:MM.</li>
     * </ul>
     * <p>
     * In caso di successo dell'operazione di invio, viene mostrato un messaggio di conferma;
     * in caso di errore, viene visualizzato un {@link JOptionPane} con il messaggio di eccezione.
     *
     * @return Un {@link JPanel} configurato con i campi di ingresso e il pulsante di invio.
     */
    protected JPanel pannelloRichiestaSpostamento() {
        JPanel pannello = new JPanel(new GridBagLayout());
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = Stile.INSET_STANDARD;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JTextField campoInsegnamento = new JTextField(18);
        JTextField campoOraOriginale = new JTextField(18);
        JTextField campoGiornoOriginale = new JTextField(18);
        JTextField campoGiornoRichiesto = new JTextField(18);
        JTextField campoOraInizioRichiesta = new JTextField(18);
        JTextField campoOraFineRichiesta = new JTextField(18);
        JTextField campoAula = new JTextField(18);

        int riga = 0;
        riga = aggiungiCampo(pannello, gbc, riga, "Insegnamento:", campoInsegnamento);
        riga = aggiungiCampo(pannello, gbc, riga, "Ora originale (hh:mm):", campoOraOriginale);
        riga = aggiungiCampo(pannello, gbc, riga, "Giorno originale (aaaa-mm-gg):", campoGiornoOriginale);
        riga = aggiungiCampo(pannello, gbc, riga, "Giorno richiesto (aaaa-mm-gg):", campoGiornoRichiesto);
        riga = aggiungiCampo(pannello, gbc, riga, "Ora inizio richiesta (hh:mm):", campoOraInizioRichiesta);
        riga = aggiungiCampo(pannello, gbc, riga, "Ora fine richiesta (hh:mm):", campoOraFineRichiesta);
        riga = aggiungiCampo(pannello, gbc, riga, "Aula richiesta:", campoAula);

        JButton pulsanteInvia = Stile.creaPulsante("INVIA RICHIESTA", Stile.ARANCIONE2);
        pulsanteInvia.addActionListener(e -> {
            try {
                Controller.getInstance().aggiungiRichiestaSpostamento(
                        campoInsegnamento.getText().trim(),
                        campoOraOriginale.getText().trim(),
                        campoGiornoOriginale.getText().trim(),
                        campoGiornoRichiesto.getText().trim(),
                        campoOraInizioRichiesta.getText().trim(),
                        campoOraFineRichiesta.getText().trim(),
                        campoAula.getText().trim());
                JOptionPane.showMessageDialog(this, "Richiesta inviata.");
            } catch (Exception errore) {
                JOptionPane.showMessageDialog(this, errore.getMessage(), "Richiesta non inviata", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridy = riga;
        pannello.add(pulsanteInvia, gbc);

        return pannello;
    }



    /**
     * Crea e restituisce un pannello per la gestione dei vincoli temporali del docente.
     * <p>
     * Il pannello organizza l'interfaccia in tre sezioni:
     * <ul>
     * <li>Un'intestazione descrittiva.</li>
     * <li>Un pannello lista che visualizza i vincoli correnti, aggiornato tramite {@link #aggiornaListaVincoli(JPanel)}.</li>
     * <li>Un form di ingresso per aggiungere nuovi vincoli e un pulsante per eliminare tutti i vincoli esistenti.</li>
     * </ul>
     * <p>
     * <b>Requisiti di formato per gli ingressi:</b>
     * <ul>
     * <li>Orario (Ora inizio/fine): HH:MM.</li>
     * </ul>
     * <p>
     * Le azioni di aggiunta ed eliminazione interagiscono direttamente con il {@link Controller},
     * garantendo che la lista visualizzata rifletta sempre lo stato attuale dei dati nel database.
     *
     * @return Un {@link JPanel} configurato con la lista dei vincoli, il form di inserimento e i controlli di gestione.
     */
    protected JPanel pannelloVincoli() {
        JPanel pannello = DashboardUtils.creaPannelloGenerico();

        JPanel listaVincoli = DashboardUtils.creaLista();
        aggiornaListaVincoli(listaVincoli);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = Stile.INSET_STANDARD;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JComboBox<String> campoGiorno = new JComboBox<>(new String[]{"LUNEDI", "MARTEDI", "MERCOLEDI", "GIOVEDI", "VENERDI"});
        JTextField campoOraInizio = new JTextField(10);
        JTextField campoOraFine = new JTextField(10);

        int riga = 0;
        riga = aggiungiCampo(form, gbc, riga, "Giorno:", campoGiorno);
        riga = aggiungiCampo(form, gbc, riga, "Ora inizio (hh:mm):", campoOraInizio);
        riga = aggiungiCampo(form, gbc, riga, "Ora fine (hh:mm):", campoOraFine);

        JButton pulsanteAggiungi = Stile.creaPulsante("AGGIUNGI VINCOLO", Stile.ARANCIONE2);
        pulsanteAggiungi.addActionListener(e -> {
            try {
                Controller.getInstance().aggiungiVincoloDocente(
                        (String) campoGiorno.getSelectedItem(),
                        campoOraInizio.getText().trim(), campoOraFine.getText().trim());
                aggiornaListaVincoli(listaVincoli);
            } catch (Exception errore) {
                JOptionPane.showMessageDialog(this, errore.getMessage(), "Vincolo non aggiunto", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton pulsanteElimina = Stile.creaPulsante("ELIMINA VINCOLI", Stile.ROSSO_SCURO);
        pulsanteElimina.addActionListener(e -> {
            try {
                Controller.getInstance().eliminaVincoliDocente();
                aggiornaListaVincoli(listaVincoli);
            } catch (Exception errore) {
                JOptionPane.showMessageDialog(this, errore.getMessage(), "Vincoli non eliminati", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridy = riga;
        form.add(pulsanteAggiungi, gbc);
        gbc.gridy++;
        form.add(pulsanteElimina, gbc);

        pannello.add(new JLabel("I tuoi vincoli attuali:"), BorderLayout.NORTH);
        pannello.add(listaVincoli, BorderLayout.CENTER);
        pannello.add(form, BorderLayout.SOUTH);
        return pannello;
    }



    /**
     * Aggiorna il pannello lista con i vincoli temporali correnti del docente.
     * <p>
     * Il metodo esegue una sincronizzazione tra lo stato nel database e l'interfaccia
     * grafica seguendo questi passaggi:
     * <ul>
     * <li>Svuota il pannello {@code listaVincoli} dai componenti precedenti.</li>
     * <li>Richiede al {@link Controller} l'elenco aggiornato dei vincoli tramite
     * {@link Controller#getVincoliDocente()}.</li>
     * <li>Popola il pannello creando un'etichetta ({@link JLabel}) per ogni vincolo recuperato.</li>
     * <li>Gestisce lo stato di lista vuota o di errore, fornendo feedback visivo diretto all'utente.</li>
     * </ul>
     * Infine, invoca {@code revalidate()} e {@code repaint()} per forzare l'aggiornamento
     * del layout nel contenitore grafico.
     *
     * @param listaVincoli Il {@link JPanel} destinato a contenere le righe dei vincoli.
     */
    private void aggiornaListaVincoli(JPanel listaVincoli) {
        listaVincoli.removeAll();
        try {
            List<String> vincoli = Controller.getInstance().getVincoliDocente();
            if (vincoli.isEmpty()) {
                listaVincoli.add(new JLabel("Nessun vincolo inserito."));
            }
            for (String vincolo : vincoli) {
                JLabel riga = new JLabel(vincolo);
                riga.setFont(Stile.FONT_TESTO);
                listaVincoli.add(riga);
            }
        } catch (Exception errore) {
            listaVincoli.add(new JLabel("Errore nel recupero dei vincoli: " + errore.getMessage()));
        }
        listaVincoli.revalidate();
        listaVincoli.repaint();
    }


    /**
     * Crea e restituisce un pannello che visualizza le variazioni relative alle attività del docente.
     * <p>
     * Il pannello organizza le informazioni in un layout verticale ({@link BoxLayout}),
     * popolandolo dinamicamente tramite i dati recuperati dal {@link Controller}.
     * <p>
     * Il metodo gestisce tre scenari:
     * <ul>
     * <li><b>Successo con dati:</b> Ogni variazione viene visualizzata come una {@link JLabel}
     * formattata con lo stile {@code Stile.FONT_TESTO}.</li>
     * <li><b>Successo senza dati:</b> Viene mostrato un messaggio informativo che indica
     * l'assenza di variazioni.</li>
     * <li><b>Errore:</b> In caso di eccezioni durante il recupero dei dati, viene mostrato
     * un messaggio di errore descrittivo direttamente nel pannello.</li>
     * </ul>
     *
     * @return Un {@link JPanel} contenente la lista delle variazioni correnti.
     */
    private JPanel pannelloVariazioni() {
        JPanel pannello = new JPanel();
        pannello.setLayout(new BoxLayout(pannello, BoxLayout.Y_AXIS));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {
            List<String> variazioni = Controller.getInstance().getVariazioniDocente();
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