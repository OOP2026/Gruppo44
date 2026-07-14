package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DashboardDocente extends JPanel {

    protected final MainPanel mainPanel;
    protected String[] dati; // contiene nome[0], cognome[1], email[2] riempito dopo il login

    // registrazione = true mostra il form di iscrizione, false mostra il login
    public DashboardDocente(MainPanel mainPanel, boolean registrazione) {
        this.mainPanel = mainPanel;
        setBackground(Stile.AZZURRO);
        setLayout(new BorderLayout());

        if (registrazione) {
            mostraRegistrazione();
        } else {
            mostraLogin();
        }
    }

    // costruttore protected usato solo dalla sottoclasse DashboardResponsabile, che (al momento) entra già autenticata
    // non mostra né login né registrazione, ci pensa la docente (per ora) a chiamare mostraAreaPersonale().
    protected DashboardDocente(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        setBackground(Stile.AZZURRO);
        setLayout(new BorderLayout());
    }

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

    // Come in studente aggiunge una riga label + campo e restituisce la riga successiva libera
    protected int aggiungiCampo(JPanel form, GridBagConstraints gbc, int riga, String etichetta, JComponent campo) {
        JLabel label = new JLabel(etichetta);
        label.setFont(Stile.FONT_ETICHETTA);
        gbc.gridy = riga++;
        form.add(label, gbc);
        gbc.gridy = riga++;
        form.add(campo, gbc);
        return riga;
    }

    //analogo a studente
    protected JPanel centra(JPanel contenuto) {
        JPanel esterno = new JPanel(new GridBagLayout());
        esterno.setBackground(Stile.AZZURRO);
        esterno.add(contenuto);
        return esterno;
    }

    // schermata personale dopo il login
    // usata da DashboardResponsabile che mostra pero' voci diverse
    protected void mostraAreaPersonale() {
        removeAll();

        String[] nomeScelta = {"Dati anagrafici", "I miei insegnamenti", "Le mie lezioni", "Richiedi spostamento", "I miei vincoli"};
        JPanel[] pannelli = {
                pannelloDatiAnagrafici(),
                pannelloInsegnamenti(),
                pannelloLezioni(),
                pannelloRichiestaSpostamento(),
                pannelloVincoli()
        };

        add(new PannelloRiutilizzabileMenu(nomeScelta, pannelli), BorderLayout.CENTER);
        add(creaBarraSuperiore(), BorderLayout.NORTH);
        revalidate();
        repaint();
    }

    protected JPanel creaBarraSuperiore() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(Stile.AZZURRO);
        barra.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel benvenuto = new JLabel("Benvenuto, " + dati[0] + " " + dati[1]);
        benvenuto.setFont(Stile.FONT_TITOLO_MEDIO);
        benvenuto.setForeground(Stile.BLU_SCURO);

        JButton pulsanteLogout = Stile.creaPulsanteTestoBianco("LOGOUT", Stile.ARANCIONE1);
        pulsanteLogout.addActionListener(e -> mainPanel.mostraHome());

        barra.add(benvenuto, BorderLayout.WEST);
        barra.add(pulsanteLogout, BorderLayout.EAST);
        return barra;
    }

    protected JPanel pannelloDatiAnagrafici() {
        JPanel pannello = new JPanel(new GridLayout(3, 1, 0, 10));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        pannello.add(rigaDato("Nome:", dati[0]));
        pannello.add(rigaDato("Cognome:", dati[1]));
        pannello.add(rigaDato("Email:", dati[2]));
        return pannello;
    }

    protected JLabel rigaDato(String etichetta, String valore) {
        JLabel label = new JLabel(etichetta + " " + valore);
        label.setFont(Stile.FONT_TESTO);
        return label;
    }

    // elenco dei nomi degli insegnamenti del docente più un form per proporne uno nuovo
    protected JPanel pannelloInsegnamenti() {
        JPanel pannello = new JPanel(new BorderLayout(0, 15));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(Color.WHITE);
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
                Controller.getInstance().creaInsegnamento(campoNome.getText().trim(), 0, 0, dati[2]);
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

    private void aggiornaListaInsegnamenti(JPanel lista) {
        lista.removeAll();
        try {
            List<String> insegnamenti = Controller.getInstance().getInsegnamentiDocente(dati[2]);
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

    // orario delle lezioni che il docente deve tenere giorno per giorno
    protected JPanel pannelloLezioni() {
        JPanel pannello = new JPanel();
        pannello.setLayout(new BoxLayout(pannello, BoxLayout.Y_AXIS));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] giorni = {"LUNEDI", "MARTEDI", "MERCOLEDI", "GIOVEDI", "VENERDI"};
        try {
            ArrayList<String>[] lezioni = Controller.getInstance().getLezioni(dati[2]);
            for (int i = 0; i < giorni.length; i++) {
                JLabel titoloGiorno = new JLabel(giorni[i]);
                titoloGiorno.setFont(Stile.FONT_ETICHETTA);
                pannello.add(titoloGiorno);

                ArrayList<String> lezioniGiorno = lezioni[i];
                if (lezioniGiorno == null || lezioniGiorno.isEmpty()) {
                    pannello.add(new JLabel("Nessuna lezione"));
                } else {
                    for (String lezione : lezioniGiorno) {
                        String[] campi = lezione.split("\n"); // oraInizio, oraFine, insegnamento, aula
                        JLabel riga = new JLabel(campi[0] + "-" + campi[1] + "  " + campi[2] + " (aula " + campi[3] + ")");
                        riga.setFont(Stile.FONT_TESTO);
                        pannello.add(riga);
                    }
                }
                pannello.add(Box.createVerticalStrut(10));
            }
        } catch (Exception errore) {
            pannello.add(new JLabel("Errore nel recupero delle lezioni: " + errore.getMessage()));
        }
        return pannello;
    }

    // form per inviare la richiesta spostamento di una lezione
    // converto per il controller (che vuole orario/data come LocalTime/LocalDate e anche l'aula: qui li convertiamo.
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
                        LocalTime.parse(campoOraOriginale.getText().trim()),
                        LocalDate.parse(campoGiornoOriginale.getText().trim()),
                        LocalDate.parse(campoGiornoRichiesto.getText().trim()),
                        LocalTime.parse(campoOraInizioRichiesta.getText().trim()),
                        LocalTime.parse(campoOraFineRichiesta.getText().trim()),
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

    // elenco dei vincoli già inseriti e form per aggiungerne uno nuovo o modificare
    protected JPanel pannelloVincoli() {
        JPanel pannello = new JPanel(new BorderLayout(0, 15));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel listaVincoli = new JPanel();
        listaVincoli.setLayout(new BoxLayout(listaVincoli, BoxLayout.Y_AXIS));
        listaVincoli.setBackground(Color.WHITE);
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
                        dati[2], (String) campoGiorno.getSelectedItem(),
                        campoOraInizio.getText().trim(), campoOraFine.getText().trim());
                aggiornaListaVincoli(listaVincoli);
            } catch (Exception errore) {
                JOptionPane.showMessageDialog(this, errore.getMessage(), "Vincolo non aggiunto", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridy = riga;
        form.add(pulsanteAggiungi, gbc);

        pannello.add(new JLabel("I tuoi vincoli attuali:"), BorderLayout.NORTH);
        pannello.add(listaVincoli, BorderLayout.CENTER);
        pannello.add(form, BorderLayout.SOUTH);
        return pannello;
    }


    private void aggiornaListaVincoli(JPanel listaVincoli) {
        listaVincoli.removeAll();
        try {
            List<String> vincoli = Controller.getInstance().getVincoliDocente(dati[2]);
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
}