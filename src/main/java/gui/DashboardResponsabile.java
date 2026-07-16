package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class DashboardResponsabile extends DashboardDocente {

    private static final String[] GIORNI = {"LUNEDI", "MARTEDI", "MERCOLEDI", "GIOVEDI", "VENERDI"};


    /**
     * Inizializza la dashboard specifica per il profilo Responsabile.
     * <p>
     * Il costruttore estende la configurazione base di {@link DashboardDocente}, forzando
     * l'impostazione dei dati utente poiché il Responsabile accede al sistema come utente già autenticato.
     *
     * @param mainPanel Il pannello principale dell'applicazione che gestisce la navigazione.
     * @param email L'indirizzo email del responsabile, utilizzato per identificare la sessione.
     */
    public DashboardResponsabile(MainPanel mainPanel, String email) {
        super(mainPanel); // il Responsabile entra già autenticato
        this.dati = new String[]{"Responsabile", "", email};
        mostraAreaPersonale();
    }



    /**
     * Aggiorna l'interfaccia grafica per visualizzare l'area riservata del Responsabile.
     * <p>
     * Il metodo ridefinisce l'area personale costruendo una navigazione a schede (menu)
     * che permette al Responsabile di accedere alle funzioni amministrative:
     * <ul>
     * <li><b>Gestisci aule:</b> Interfaccia per la manutenzione delle aule.</li>
     * <li><b>Elenco insegnamenti:</b> Vista riepilogativa di tutti gli insegnamenti.</li>
     * <li><b>Gestisci richieste:</b> Pannello operativo per l'approvazione o il rifiuto delle richieste di spostamento.</li>
     * </ul>
     * <p>
     * La struttura del pannello è composta da:
     * <ul>
     * <li>Una barra superiore ({@code NORTH}) tramite {@link DashboardUtils#creaBarraSuperiore(String[], MainPanel)}.</li>
     * <li>Un menu di navigazione a schede ({@code CENTER}) tramite {@link PannelloRiutilizzabileMenu}.</li>
     * </ul>
     */
    @Override
    protected void mostraAreaPersonale() {
        removeAll();
        String[] nomeScelta = {"Gestisci aule", "Elenco insegnamenti", "Gestisci richieste"};
        JPanel[] pannelli = {pannelloGestisciAule(), pannelloElencoInsegnamenti(), pannelloGestisciRichieste()};

        add(new PannelloRiutilizzabileMenu(nomeScelta, pannelli), BorderLayout.CENTER);
        add(DashboardUtils.creaBarraSuperiore(dati, mainPanel), BorderLayout.NORTH);
        revalidate();
        repaint();
    }



    /**
     * Crea e restituisce un pannello per la gestione delle aule.
     * <p>
     * Il pannello fornisce al Responsabile gli strumenti per visualizzare le aule
     * correnti e aggiungerne di nuove tramite un form dedicato.
     * <p>
     * La logica di funzionamento include:
     * <ul>
     * <li><b>Visualizzazione:</b> Richiama {@link #aggiornaListaAule(JPanel)} per
     * popolare la lista delle aule esistenti.</li>
     * <li><b>Creazione:</b> Permette l'inserimento di nome e capienza massima. Il valore
     * di capienza viene convertito in intero; in caso di formato non valido o errore
     * lato {@link Controller}, viene mostrato un messaggio di avviso.</li>
     * <li><b>Feedback:</b> Dopo l'aggiunta, i campi vengono svuotati e la lista
     * viene aggiornata automaticamente.</li>
     * </ul>
     *
     * @return Un {@link JPanel} strutturato con la lista delle aule in alto e il
     * form di inserimento in basso.
     */
    private JPanel pannelloGestisciAule() {
        JPanel pannello = DashboardUtils.creaPannelloGenerico();

        JPanel lista = DashboardUtils.creaLista();
        aggiornaListaAule(lista);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = Stile.INSET_STANDARD;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JTextField campoNome = new JTextField(10);
        JTextField campoCapienza = new JTextField(10);
        int riga = aggiungiCampo(form, gbc, 0, "Nuova aula:", campoNome);
        riga = aggiungiCampo(form, gbc, riga, "Capienza massima:", campoCapienza);

        JButton pulsanteAggiungi = Stile.creaPulsante("AGGIUNGI AULA", Stile.BLU_CHIARO);
        pulsanteAggiungi.addActionListener(e -> {
            try {
                int capienza = Integer.parseInt(campoCapienza.getText().trim());
                Controller.getInstance().creaAula(campoNome.getText().trim(), capienza);
                campoNome.setText("");
                campoCapienza.setText("");
                aggiornaListaAule(lista);
            } catch (Exception errore) {
                JOptionPane.showMessageDialog(this, errore.getMessage(), "Aula non aggiunta", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridy = riga;
        form.add(pulsanteAggiungi, gbc);

        pannello.add(new JLabel("Aule esistenti:"), BorderLayout.NORTH);
        pannello.add(lista, BorderLayout.CENTER);
        pannello.add(form, BorderLayout.SOUTH);
        return pannello;
    }


    /**
     * Aggiorna il pannello lista visualizzando l'elenco delle aule correnti.
     * <p>
     * Il metodo esegue la sincronizzazione dell'interfaccia con i dati del database
     * tramite i seguenti passaggi:
     * <ul>
     * <li>Rimuove tutti i componenti esistenti nel pannello {@code lista}.</li>
     * <li>Recupera la lista aggiornata delle aule dal {@link Controller}
     * tramite {@link Controller#getAule()}.</li>
     * <li>Per ogni aula, invoca {@link #rigaAula(String, JPanel)} per generare
     * una riga interattiva o formattata da aggiungere al pannello.</li>
     * <li>Gestisce il caso di lista vuota o eccezioni durante il recupero dei dati
     * visualizzando un messaggio informativo o di errore.</li>
     * </ul>
     * Infine, invoca {@code revalidate()} e {@code repaint()} per aggiornare il layout.
     *
     * @param lista Il {@link JPanel} destinato a contenere l'elenco delle aule.
     */
    private void aggiornaListaAule(JPanel lista) {
        lista.removeAll();
        try {
            List<String> aule = Controller.getInstance().getAule();
            if (aule.isEmpty()) lista.add(new JLabel("Nessuna aula inserita."));
            for (String nomeAula : aule) lista.add(rigaAula(nomeAula, lista));
        } catch (Exception errore) {
            lista.add(new JLabel(errore.getMessage()));
        }
        lista.revalidate();
        lista.repaint();
    }



    /**
     * Crea e restituisce un pannello personalizzato che rappresenta una singola
     * riga dell'elenco delle aule.
     * <p>
     * Ogni riga contiene il nome dell'aula (centrato) e un pulsante di eliminazione
     * (posizionato a destra) che consente di rimuovere l'aula dal sistema.
     * <p>
     * Il metodo gestisce l'interazione utente per l'eliminazione:
     * <ul>
     * <li>Richiede una conferma esplicita tramite {@link JOptionPane}.</li>
     * <li>In caso di conferma, invoca {@link Controller#eliminaAula(String)} per
     * rimuovere l'elemento dal database.</li>
     * <li>Aggiorna la lista contenitore {@code listaCompleta} per riflettere
     * immediatamente la modifica.</li>
     * <li>Gestisce eventuali errori durante l'eliminazione tramite una finestra
     * di dialogo di avviso.</li>
     * </ul>
     *
     * @param nome Il nome dell'aula da visualizzare.
     * @param listaCompleta Il {@link JPanel} contenitore dell'intera lista, necessario
     * per invocare il metodo di aggiornamento dopo l'eliminazione.
     * @return Un {@link JPanel} configurato con il nome dell'aula e il pulsante di rimozione.
     */
    private JPanel rigaAula(String nome, JPanel listaCompleta) {
        JPanel riga = new JPanel(new BorderLayout(10, 0));
        riga.setBackground(Color.WHITE);
        riga.add(new JLabel(nome), BorderLayout.CENTER);

        JButton pulsanteElimina = Stile.creaPulsanteTestoBianco("ELIMINA", Stile.ROSSO_CHIARO);
        pulsanteElimina.addActionListener(e -> {
            int conferma = JOptionPane.showConfirmDialog(this, "Eliminare l'aula \"" + nome + "\"?", "Conferma eliminazione", JOptionPane.YES_NO_OPTION);
            if (conferma == JOptionPane.YES_OPTION) {
                try {
                    Controller.getInstance().eliminaAula(nome);
                    aggiornaListaAule(listaCompleta);
                } catch (Exception errore) {
                    JOptionPane.showMessageDialog(this, errore.getMessage(), "Eliminazione non riuscita", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        riga.add(pulsanteElimina, BorderLayout.EAST);
        return riga;
    }


    /**
     * Crea e restituisce un pannello dedicato alla visualizzazione dell'elenco
     * completo degli insegnamenti presenti nel sistema.
     * <p>
     * Il pannello utilizza un layout verticale ({@link BoxLayout}) per presentare
     * l'elenco degli insegnamenti in modo sequenziale. La logica di recupero e
     * rendering dei dati è delegata al metodo {@link #aggiornaElencoInsegnamenti(JPanel)}.
     *
     * @return Un {@link JPanel} configurato come contenitore per la lista degli insegnamenti.
     */
    private JPanel pannelloElencoInsegnamenti() {
        JPanel pannello = new JPanel();
        pannello.setLayout(new BoxLayout(pannello, BoxLayout.Y_AXIS));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        aggiornaElencoInsegnamenti(pannello);
        return pannello;
    }



    /**
     * Aggiorna il pannello contenitore con l'elenco corrente di tutti gli insegnamenti.
     * <p>
     * Il metodo esegue una sincronizzazione tra il database e l'interfaccia
     * grafica seguendo questi passaggi:
     * <ul>
     * <li>Svuota il pannello {@code pannello} da ogni componente precedente.</li>
     * <li>Recupera l'elenco degli insegnamenti tramite {@link Controller#getInsegnamenti()}.</li>
     * <li>Per ogni elemento, invoca {@link #rigaInsegnamento(String, JPanel)} per
     * generare la riga corrispondente e aggiunge uno spazio verticale ({@link Box#createVerticalStrut(int)})
     * per migliorare la leggibilità.</li>
     * <li>Gestisce il caso di lista vuota o eccezioni durante il recupero dei dati.</li>
     * </ul>
     * Infine, invoca {@code revalidate()} e {@code repaint()} per forzare il ridisegno
     * del layout.
     *
     * @param pannello Il {@link JPanel} (con {@code BoxLayout}) che ospiterà l'elenco.
     */
    private void aggiornaElencoInsegnamenti(JPanel pannello) {
        pannello.removeAll();
        try {
            List<String> insegnamenti = Controller.getInstance().getInsegnamenti();
            if (insegnamenti.isEmpty()) pannello.add(new JLabel("Nessun insegnamento presente."));
            for (String riga : insegnamenti) {
                pannello.add(rigaInsegnamento(riga, pannello));
                pannello.add(Box.createVerticalStrut(10));
            }
        } catch (Exception errore) {
            pannello.add(new JLabel(errore.getMessage()));
        }
        pannello.revalidate();
        pannello.repaint();
    }

    /**
     * Crea e restituisce un pannello complesso che rappresenta una riga di un insegnamento,
     * fornendo controlli per la gestione, l'aggiornamento e l'eliminazione dei dati.
     * <p>
     * Il metodo analizza la stringa di ingresso per estrarre il nome dell'insegnamento e
     * costruisce un'interfaccia composta da:
     * <ul>
     * <li><b>Intestazione:</b> Visualizza le informazioni correnti dell'insegnamento.</li>
     * <li><b>Form di ingresso:</b> Campi di testo per specificare email del docente, anno e CFU.</li>
     * <li><b>Azioni:</b> Pulsanti per aggiornare i dettagli, creare una nuova lezione (tramite
     * {@link #mostraFinestraCreaLezione(String)}) o eliminare l'insegnamento.</li>
     * </ul>
     * <p>
     * La gestione degli eventi garantisce che ogni azione (aggiornamento/eliminazione)
     * richieda l'interazione con il {@link Controller} e provochi un aggiornamento
     * immediato della {@code listaCompleta} visualizzata.
     *
     * @param testo La stringa contenente i dati dell'insegnamento separati da virgola.
     * @param listaCompleta Il {@link JPanel} contenitore padre, necessario per aggiornare la vista dopo modifiche.
     * @return Un {@link JPanel} interattivo che rappresenta l'insegnamento.
     */
    private JPanel rigaInsegnamento(String testo, JPanel listaCompleta) {
        String nome = testo.split(",")[0].trim();

        JPanel riga = new JPanel();
        riga.setLayout(new BoxLayout(riga, BoxLayout.Y_AXIS));
        riga.setBackground(Stile.AZZURRO);
        riga.setBorder(BorderFactory.createLineBorder(Stile.BLU_CHIARO, 1));

        JLabel intestazione = new JLabel(testo);
        intestazione.setFont(Stile.FONT_ETICHETTA);
        riga.add(intestazione);

        JPanel campi = new JPanel(new FlowLayout(FlowLayout.LEFT));
        campi.setBackground(Stile.AZZURRO);

        JTextField campoEmail = new JTextField(14);
        JTextField campoAnno = new JTextField(3);
        JTextField campoCfu = new JTextField(3);

        JButton pulsanteAggiorna = Stile.creaPulsante("ATTIVA / AGGIORNA", Stile.VERDE_SCURO);
        pulsanteAggiorna.addActionListener(e -> {
            try {
                int anno = Integer.parseInt(campoAnno.getText().trim());
                int cfu = Integer.parseInt(campoCfu.getText().trim());
                Controller.getInstance().aggiornaInsegnamento(nome, cfu, anno, campoEmail.getText().trim());
                aggiornaElencoInsegnamenti(listaCompleta);
            } catch (Exception errore) {
                JOptionPane.showMessageDialog(this, errore.getMessage(), "Aggiornamento non riuscito", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton pulsanteCrea = Stile.creaPulsante("CREA LEZIONE", Stile.BLU_CHIARO);
        pulsanteCrea.addActionListener(e -> mostraFinestraCreaLezione(nome));

        JButton pulsanteElimina = Stile.creaPulsanteTestoBianco("ELIMINA", Stile.ROSSO_CHIARO);
        pulsanteElimina.addActionListener(e -> {
            int conferma = JOptionPane.showConfirmDialog(this, "Eliminare l'insegnamento \"" + nome + "\"?", "Conferma eliminazione", JOptionPane.YES_NO_OPTION);
            if (conferma == JOptionPane.YES_OPTION) {
                try {
                    Controller.getInstance().eliminaInsegnamento(nome);
                    aggiornaElencoInsegnamenti(listaCompleta);
                } catch (Exception errore) {
                    JOptionPane.showMessageDialog(this, errore.getMessage(), "Eliminazione non riuscita", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        campi.add(new JLabel("Email docente:"));
        campi.add(campoEmail);
        campi.add(new JLabel("Anno:"));
        campi.add(campoAnno);
        campi.add(new JLabel("CFU:"));
        campi.add(campoCfu);
        campi.add(pulsanteAggiorna);
        campi.add(pulsanteCrea);
        campi.add(pulsanteElimina);
        riga.add(campi);
        return riga;
    }



    /**
     * Apre una finestra di dialogo ({@link JDialog}) per la creazione di una nuova lezione
     * associata a un insegnamento specifico.
     * <p>
     * La finestra richiede all'utente di inserire le specifiche della lezione:
     * <ul>
     * <li><b>Email Docente:</b> Identificativo del docente responsabile.</li>
     * <li><b>Giorno:</b> Selezionabile da una lista predefinita ({@code GIORNI}).</li>
     * <li><b>Orario (Inizio/Fine):</b> Intervallo temporale della lezione.</li>
     * <li><b>Aula:</b> Identificativo dell'aula in cui si terrà la lezione.</li>
     * </ul>
     * <p>
     * Il pulsante "CREA LEZIONE" attiva la logica di business tramite
     * {@link Controller#creaLezione(String, String, String, String, String)}.
     * Il metodo esegue automaticamente le validazioni necessarie, inclusi il controllo
     * sui vincoli del docente e la disponibilità dell'aula. In caso di esito positivo,
     * la finestra si chiude automaticamente; in caso di errore, viene visualizzato un
     * feedback all'utente tramite {@link JOptionPane}.
     *
     * @param nomeInsegnamento Il nome dell'insegnamento a cui associare la lezione,
     * utilizzato come riferimento nel titolo della finestra.
     */
    private void mostraFinestraCreaLezione(String nomeInsegnamento) {
        JDialog finestra = new JDialog();
        finestra.setTitle("Crea lezione — " + nomeInsegnamento);
        finestra.setModal(true);
        finestra.setSize(380, 340);
        finestra.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = Stile.INSET_STANDARD;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JTextField campoEmailDocente = new JTextField(18);
        JComboBox<String> campoGiorno = new JComboBox<>(GIORNI);
        JTextField campoOraInizio = new JTextField(10);
        JTextField campoOraFine = new JTextField(10);
        JTextField campoAula = new JTextField(10);

        int riga = 0;
        riga = aggiungiCampo(form, gbc, riga, "Email docente:", campoEmailDocente);
        riga = aggiungiCampo(form, gbc, riga, "Giorno:", campoGiorno);
        riga = aggiungiCampo(form, gbc, riga, "Ora inizio (hh:mm):", campoOraInizio);
        riga = aggiungiCampo(form, gbc, riga, "Ora fine (hh:mm):", campoOraFine);
        riga = aggiungiCampo(form, gbc, riga, "Aula:", campoAula);

        JButton pulsanteCrea = Stile.creaPulsante("CREA LEZIONE", Stile.VERDE_SCURO);
        pulsanteCrea.addActionListener(e -> {
            try {
                String giorno = (String) campoGiorno.getSelectedItem();
                Controller.getInstance().creaLezione(nomeInsegnamento, giorno, campoOraInizio.getText().trim(), campoOraFine.getText().trim(), campoAula.getText().trim());
                JOptionPane.showMessageDialog(finestra, "Lezione creata.");
                finestra.dispose();
            } catch (Exception errore) {
                JOptionPane.showMessageDialog(finestra, errore.getMessage(), "Errore nella creazione della lezione", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridy = riga;
        form.add(pulsanteCrea, gbc);

        finestra.setContentPane(form);
        finestra.setVisible(true);
    }



    /**
     * Crea e restituisce un pannello dedicato alla gestione delle richieste di
     * spostamento lezioni inviate dai docenti.
     * <p>
     * Il pannello utilizza un layout verticale ({@link BoxLayout}) per presentare
     * l'elenco delle richieste in sospeso. Il contenuto viene popolato e aggiornato
     * dinamicamente tramite il metodo {@link #aggiornaListaRichieste(JPanel)}.
     * <p>
     * <b>Nota tecnica:</b> L'identificazione delle richieste avviene tramite l'indice
     * della lista, poiché il metodo {@link Controller#getRegistroRichiesteSpostamento()}
     * restituisce un elenco testuale privo di identificativi univoci (ID) persistenti.
     *
     * @return Un {@link JPanel} configurato come contenitore per le richieste di spostamento.
     */
    private JPanel pannelloGestisciRichieste() {
        JPanel pannello = new JPanel();
        pannello.setLayout(new BoxLayout(pannello, BoxLayout.Y_AXIS));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        aggiornaListaRichieste(pannello);
        return pannello;
    }


    /**
     * Aggiorna il pannello contenitore con l'elenco delle richieste di spostamento
     * inviate dai docenti.
     * <p>
     * Il metodo esegue la sincronizzazione dell'interfaccia con i dati correnti:
     * <ul>
     * <li>Svuota il pannello corrente.</li>
     * <li>Recupera la {@link Map} delle richieste tramite
     * {@link Controller#getRegistroRichiesteSpostamento()}, dove la chiave
     * rappresenta l'identificativo (indice) e il valore il testo descrittivo.</li>
     * <li>Per ogni coppia chiave-valore, invoca {@link #rigaRichiesta(String, int, JPanel)}
     * per costruire l'elemento grafico corrispondente, aggiungendo una spaziatura
     * verticale di 8 pixel.</li>
     * <li>Gestisce eccezioni durante il recupero dei dati, visualizzando un messaggio
     * di errore nel pannello.</li>
     * </ul>
     *
     * @param pannello Il {@link JPanel} di destinazione che ospita la lista.
     */
    private void aggiornaListaRichieste(JPanel pannello) {
        pannello.removeAll();
        try {
            Map<Integer, String> richieste = Controller.getInstance().getRegistroRichiesteSpostamento();
            if (richieste.isEmpty()) pannello.add(new JLabel("Nessuna richiesta in attesa."));
            richieste.forEach((key, value) -> {
                pannello.add(rigaRichiesta(richieste.get(key), key , pannello));
                pannello.add(Box.createVerticalStrut(8));
            });
        } catch (Exception errore) {
            pannello.add(new JLabel(errore.getMessage()));
        }
        pannello.revalidate();
        pannello.repaint();
    }



    /**
     * Crea e restituisce un pannello personalizzato che rappresenta una singola
     * richiesta di spostamento lezione all'interno della lista.
     * <p>
     * Il pannello organizza le informazioni della richiesta e fornisce i controlli
     * necessari per la gestione amministrativa:
     * <ul>
     * <li><b>Descrizione:</b> Visualizza il contenuto testuale della richiesta ({@code testoRichiesta}).</li>
     * <li><b>Azioni:</b> Include i pulsanti "APPROVA" e "RIFIUTA".</li>
     * </ul>
     * <p>
     * Ogni pulsante invoca il rispettivo metodo del {@link Controller} utilizzando
     * l'{@code indice} come identificatore univoco della richiesta. In seguito all'azione,
     * il pannello {@code listaCompleta} viene aggiornato automaticamente per riflettere
     * la rimozione o la modifica della richiesta.
     *
     * @param testoRichiesta La descrizione testuale della richiesta di spostamento.
     * @param indice L'identificativo numerico (chiave) associato alla richiesta.
     * @param listaCompleta  Il {@link JPanel} contenitore padre, passato per permettere
     * il refresh dell'interfaccia dopo l'approvazione o il rifiuto.
     * @return Un {@link JPanel} configurato con il testo della richiesta e i relativi
     * pulsanti di controllo.
     */
    private JPanel rigaRichiesta(String testoRichiesta, int indice, JPanel listaCompleta) {
        JPanel riga = new JPanel(new BorderLayout(10, 0));
        riga.setBackground(Color.WHITE);
        riga.add(new JLabel(testoRichiesta), BorderLayout.CENTER);

        JButton pulsanteApprova = Stile.creaPulsanteTestoBianco("APPROVA", Stile.VERDE_SCURO);
        pulsanteApprova.addActionListener(e -> {
            try {
                Controller.getInstance().approvaRichiesta(indice);
                aggiornaListaRichieste(listaCompleta);
            } catch (Exception errore) {
                JOptionPane.showMessageDialog(this, errore.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton pulsanteRifiuta = Stile.creaPulsanteTestoBianco("RIFIUTA", Stile.ROSSO_CHIARO);
        pulsanteRifiuta.addActionListener(e -> {
            try {
                Controller.getInstance().rifiutaRichiesta(indice);
                aggiornaListaRichieste(listaCompleta);
            } catch (Exception errore) {
                JOptionPane.showMessageDialog(this, errore.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel pulsanti = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pulsanti.setBackground(Color.WHITE);
        pulsanti.add(pulsanteApprova);
        pulsanti.add(pulsanteRifiuta);
        riga.add(pulsanti, BorderLayout.EAST);
        return riga;
    }
}