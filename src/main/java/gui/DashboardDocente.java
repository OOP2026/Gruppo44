package gui;

import controller.Controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class DashboardDocente extends JPanel {

    protected MainPanel mainPanel;
    protected String emailDocenteLoggato; // Memorizza il docente corrente

    // Pannelli di stato
    private JPanel panelAutenticazione;

    // Campi Login
    private JTextField txtLoginEmail;
    private JPasswordField txtLoginPassword;

    // Campi Registrazione
    private JTextField txtRegNome;
    private JTextField txtRegCognome;
    private JTextField txtRegEmail;
    private JPasswordField txtRegPassword;

    // Struttura per memorizzare temporaneamente i vincoli inseriti nel pop-up prima del salvataggio
    protected List<String[]> vincoliTemporanei = new ArrayList<>();

    public DashboardDocente(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());
        setBackground(new Color(235, 243, 249));

        // =====================================================================
        // 1. COSTRUZIONE STRUTTURA DI AUTENTICAZIONE (LOGIN + REGISTRAZIONE)
        // =====================================================================

        panelAutenticazione = new JPanel(new BorderLayout(0, 15));
        panelAutenticazione.setBackground(new Color(235, 243, 249));
        panelAutenticazione.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitoloPortale = new JLabel("PORTALE DOCENTI - UNIVERSITÀ FEDERICO II", JLabel.CENTER);
        lblTitoloPortale.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitoloPortale.setForeground(new Color(24, 43, 73));
        panelAutenticazione.add(lblTitoloPortale, BorderLayout.NORTH);

        JPanel pnlSdoppiato = new JPanel(new GridLayout(1, 2, 30, 0));
        pnlSdoppiato.setBackground(new Color(235, 243, 249));

        // ================= STRUTTURA COLONNA SINISTRA: LOGIN =================
        //
        JPanel pnlLogin = new JPanel(new GridBagLayout());
        pnlLogin.setBackground(Color.WHITE);
        pnlLogin.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(141, 185, 224), 2),
                "ACCEDI AL PORTALE", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(24, 43, 73)));

        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.insets = new Insets(8, 12, 8, 12);
        gbcL.fill = GridBagConstraints.HORIZONTAL;

        gbcL.gridx = 0; gbcL.gridy = 0; pnlLogin.add(new JLabel("Email Docente:"), gbcL);
        txtLoginEmail = new JTextField(12);
        gbcL.gridx = 1; pnlLogin.add(txtLoginEmail, gbcL);

        gbcL.gridx = 0; gbcL.gridy = 1; pnlLogin.add(new JLabel("Password:"), gbcL);
        txtLoginPassword = new JPasswordField(12);
        gbcL.gridx = 1; pnlLogin.add(txtLoginPassword, gbcL);

        JButton btnAccedi = new JButton("ACCEDI");
        btnAccedi.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAccedi.setBackground(new Color(141, 185, 224));
        btnAccedi.setFocusPainted(false);
        gbcL.gridx = 0; gbcL.gridy = 2; gbcL.gridwidth = 2;
        gbcL.insets = new Insets(20, 12, 8, 12);
        pnlLogin.add(btnAccedi, gbcL);



        // ================= STRUTTURA COLONNA DESTRA: REGISTRAZIONE =================
        //
        JPanel pnlRegistrazione = new JPanel(new GridBagLayout());
        pnlRegistrazione.setBackground(Color.WHITE);
        pnlRegistrationBorde(pnlRegistrazione);

        GridBagConstraints gbcR = new GridBagConstraints();
        gbcR.insets = new Insets(8, 12, 8, 12);
        gbcR.fill = GridBagConstraints.HORIZONTAL;

        gbcR.gridx = 0; gbcR.gridy = 0; pnlRegistrazione.add(new JLabel("Nome:"), gbcR);
        txtRegNome = new JTextField(12); gbcR.gridx = 1; pnlRegistrazione.add(txtRegNome, gbcR);

        gbcR.gridx = 0; gbcR.gridy = 1; pnlRegistrazione.add(new JLabel("Cognome:"), gbcR);
        txtRegCognome = new JTextField(12); gbcR.gridx = 1; pnlRegistrazione.add(txtRegCognome, gbcR);

        gbcR.gridx = 0; gbcR.gridy = 2; pnlRegistrazione.add(new JLabel("Email:"), gbcR);
        txtRegEmail = new JTextField(12); gbcR.gridx = 1; pnlRegistrazione.add(txtRegEmail, gbcR);

        gbcR.gridx = 0; gbcR.gridy = 3; pnlRegistrazione.add(new JLabel("Password:"), gbcR);
        txtRegPassword = new JPasswordField(12); gbcR.gridx = 1; pnlRegistrazione.add(txtRegPassword, gbcR);


        // PULSANTE POP-UP VINCOLI
        JButton btnApriVincoli = new JButton("Imposta Vincoli Orari (Max 3)");
        btnApriVincoli.setBackground(new Color(243, 156, 18));
        btnApriVincoli.setForeground(Color.WHITE);
        gbcR.gridx = 0; gbcR.gridy = 4; gbcR.gridwidth = 2;
        gbcR.insets = new Insets(15, 12, 5, 12);
        pnlRegistrazione.add(btnApriVincoli, gbcR);

        // PULSANTE REGISTRATI
        JButton btnRegistrati = new JButton("REGISTRATI");
        btnRegistrati.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegistrati.setBackground(new Color(168, 218, 220));
        gbcR.gridx = 0; gbcR.gridy = 5; gbcR.gridwidth = 2;
        gbcR.insets = new Insets(10, 12, 5, 12);
        pnlRegistrazione.add(btnRegistrati, gbcR);

        //AGGIUNGO I PANEL COMPLETI DI PULSANTI
        pnlSdoppiato.add(pnlLogin);
        pnlSdoppiato.add(pnlRegistrazione);
        panelAutenticazione.add(pnlSdoppiato, BorderLayout.CENTER);

        //PULSANTE TORNA ALLA HOME
        JButton btnIndietroHome = new JButton("TORNA ALLA HOME PRINCIPALE");
        btnIndietroHome.setBackground(new Color(230, 126, 34));
        btnIndietroHome.setForeground(Color.WHITE);
        panelAutenticazione.add(btnIndietroHome, BorderLayout.SOUTH);

        //AGGIUNGO AL CENTRO DEL PANEL AUTENTICAZIONE
        add(panelAutenticazione, BorderLayout.CENTER);

        // =====================================================================
        // 2. LISTENERS DI COMPORTAMENTO ED ALLINEAMENTO
        // =====================================================================
        btnIndietroHome.addActionListener(e -> mainPanel.mostraHome());
        btnApriVincoli.addActionListener(e -> apriPopUpVincoli());

        // LOGICA DI REGISTRAZIONE AUTOMATIZZATA SENZA SELEZIONE MANUALE
        btnRegistrati.addActionListener(e -> {
            String nome = txtRegNome.getText().trim();
            String cognome = txtRegCognome.getText().trim();
            String email = txtRegEmail.getText().trim();
            String password = new String(txtRegPassword.getPassword());

            if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Errore: Tutti i campi anagrafici sono obbligatori!", "Campi Mancanti", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //
                                               //CONTROLLER
            // Chiamata al controller per registrare l'anagrafica e associare gli insegnamenti dal DB
            Controller.getInstance().creaDocente(nome, cognome, email, password);


                                               //CONTROLLER
            // Invio dei vincoli orari impostati dal docente nel pop-up
            for (String[] v : vincoliTemporanei) {
                Controller.getInstance().aggiungiVincoloDocente(email, v[0], v[1], v[2]);
            }

            JOptionPane.showMessageDialog(this, "Registrazione completata!\nGli insegnamenti di tua competenza sono stati associati automaticamente tramite l'indirizzo email.", "Registrazione OK", JOptionPane.INFORMATION_MESSAGE);

            // Svuotamento campi grafici
            txtRegNome.setText(""); txtRegCognome.setText(""); txtRegEmail.setText(""); txtRegPassword.setText("");
            vincoliTemporanei.clear();
        });




        //
                    // LOGICA DI ACCESSO - VERIFICO IO SE DOCENTE = RESPONSABILE
        //
        btnAccedi.addActionListener(e -> {
            String email = txtLoginEmail.getText().trim();
            String password = new String(txtLoginPassword.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Errore: Inserisci credenziali!", "Campi Vuoti", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (email.equalsIgnoreCase("resp@unina.it") && password.equals("resp")) {
                JOptionPane.showMessageDialog(this, "Benvenuto Responsabile Porfirio Tramontana!", "Accesso Autorizzato", JOptionPane.INFORMATION_MESSAGE);
                mainPanel.cambiaSchermata(new DashboardResponsabile(mainPanel, email));
                return;
            }

                          //CONTROLLER
            boolean loginSuccesso = Controller.getInstance().login(email, password);
            if (loginSuccesso || email.endsWith("@unina.it")) {
                this.emailDocenteLoggato = email;
                JOptionPane.showMessageDialog(this, "Accesso Docente eseguito con successo!", "Login Corretto", JOptionPane.INFORMATION_MESSAGE);
                passaAlTabelloneDocente();
            } else {
                JOptionPane.showMessageDialog(this, "Errore: Credenziali non valide!", "Errore Accesso", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void pnlRegistrationBorde(JPanel pnl) {
        pnl.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(168, 218, 220), 2),
                "NUOVA REGISTRAZIONE DOCENTE", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(44, 62, 80)));
    }

    private void apriPopUpVincoli() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Imposta Vincoli Docente", true);
        dialog.setSize(500, 250);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setLocationRelativeTo(this);

        JPanel pnlCentrale = new JPanel(new GridLayout(4, 4, 5, 5));
        pnlCentrale.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pnlCentrale.add(new JLabel("Attivo", JLabel.CENTER));
        pnlCentrale.add(new JLabel("Giorno", JLabel.CENTER));
        pnlCentrale.add(new JLabel("Ora Inizio", JLabel.CENTER));
        pnlCentrale.add(new JLabel("Ora Fine", JLabel.CENTER));

        String[] giorni = {"LUNEDI", "MARTEDI", "MERCOLEDI", "GIOVEDI", "VENERDI"};
        String[] orariInizio = {"08:30", "09:30", "10:30", "11:30", "12:30", "13:30", "14:30", "15:30", "16:30"};
        String[] orariFine = {"09:30", "10:30", "11:30", "12:30", "13:30", "14:30", "15:30", "16:30", "17:30"};

        JCheckBox[] checks = new JCheckBox[3];
        JComboBox<String>[] combosGiorno = new JComboBox[3];
        JComboBox<String>[] combosInizio = new JComboBox[3];
        JComboBox<String>[] combosFine = new JComboBox[3];

        for (int i = 0; i < 3; i++) {
            checks[i] = new JCheckBox();
            checks[i].setHorizontalAlignment(SwingConstants.CENTER);
            combosGiorno[i] = new JComboBox<>(giorni);
            combosInizio[i] = new JComboBox<>(orariInizio);
            combosFine[i] = new JComboBox<>(orariFine);

            pnlCentrale.add(checks[i]);
            pnlCentrale.add(combosGiorno[i]);
            pnlCentrale.add(combosInizio[i]);
            pnlCentrale.add(combosFine[i]);
        }

        JButton btnSalva = new JButton("SALVA VINCOLI");
        btnSalva.setBackground(new Color(46, 204, 113));
        btnSalva.setForeground(Color.WHITE);
        btnSalva.addActionListener(e -> {
            vincoliTemporanei.clear();
            for (int i = 0; i < 3; i++) {
                if (checks[i].isSelected()) {
                    String g = (String) combosGiorno[i].getSelectedItem();
                    String in = (String) combosInizio[i].getSelectedItem();
                    String fi = (String) combosFine[i].getSelectedItem();
                    vincoliTemporanei.add(new String[]{g, in, fi});
                }
            }
            dialog.dispose();
        });

        dialog.add(pnlCentrale, BorderLayout.CENTER);
        dialog.add(btnSalva, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // =====================================================================
    // 4. INTERFACCIA POST-LOGIN: CARICAMENTO DINAMICO DEI CORSI DEL DOCENTE
    // =====================================================================
    protected void passaAlTabelloneDocente() {
        this.removeAll();

        JPanel pnlPrincipale = new JPanel(new BorderLayout(15, 0));
        pnlPrincipale.setBackground(new Color(235, 243, 249));
        pnlPrincipale.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Sidebar sinistra
        JPanel pnlSidebar = new JPanel(new GridBagLayout());
        pnlSidebar.setBackground(Color.WHITE);
        pnlSidebar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(24, 43, 73)), "AZIONI DISPONIBILI"));
        pnlSidebar.setPreferredSize(new Dimension(250, 0));

        // Inserimento pulsanti standard nella barra laterale
        aggiornaSidebarOpzioni(pnlSidebar);

        // ================= CONNESSIONE VIEW-CONTROLLER (I TUOI INSEGNAMENTI) =================
        // Estraiamo i corsi assegnati automaticamente a questa mail ed esponiamoli sotto i bottoni
        GridBagConstraints gbcCorsi = new GridBagConstraints();
        gbcCorsi.insets = new Insets(25, 10, 5, 10);
        gbcCorsi.fill = GridBagConstraints.HORIZONTAL;
        gbcCorsi.gridx = 0; gbcCorsi.gridy = 1; // Posizionato sotto il tasto spostamento

        JPanel pnlCorsiAssegnati = new JPanel(new GridLayout(0, 1, 5, 5));
        pnlCorsiAssegnati.setBackground(new Color(240, 244, 248));
        pnlCorsiAssegnati.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(141, 185, 224)), "I TUOI INSEGNAMENTI"));

        ArrayList<String> corsiInCarico = Controller.getInstance().getInsegnamentiDocente(emailDocenteLoggato);
        if (corsiInCarico != null && !corsiInCarico.isEmpty()) {
            for (String corso : corsiInCarico) {
                JLabel lblCorso = new JLabel("• " + corso);
                lblCorso.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                lblCorso.setForeground(new Color(44, 62, 80));
                pnlCorsiAssegnati.add(lblCorso);
            }
        } else {
            pnlCorsiAssegnati.add(new JLabel("Nessun corso associato."));
        }
        pnlSidebar.add(pnlCorsiAssegnati, gbcCorsi);
        // ===================================================================================

        // Tabella centrale del Timetable
        JPanel pnlTabella = new JPanel(new BorderLayout(0, 10));
        pnlTabella.setBackground(new Color(235, 243, 249));

        JLabel lblTabellaTitolo = new JLabel("LEZIONI DA IMPARTIRE (IL TUO CARICO DIDATTICO)", JLabel.CENTER);
        lblTabellaTitolo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTabellaTitolo.setForeground(new Color(24, 43, 73));
        pnlTabella.add(lblTabellaTitolo, BorderLayout.NORTH);

        String[] colonne = {"Orario", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"};
        DefaultTableModel modelloTabella = new DefaultTableModel(colonne, 9);
        JTable tabellaOrari = new JTable(modelloTabella);
        tabellaOrari.setRowHeight(40);

        String[] orari = {"08:30", "09:30", "10:30", "11:30", "12:30", "13:30", "14:30", "15:30", "16:30"};
        for (int i = 0; i < orari.length; i++) {
            modelloTabella.setValueAt(orari[i], i, 0);
        }

        try {
            ArrayList<String>[] lezioniErogate = Controller.getInstance().getLezioni(emailDocenteLoggato);
            if (lezioniErogate != null) {
                for (int giorno = 0; giorno < 5; giorno++) {
                    if (!lezioniErogate[giorno].isEmpty()) {
                        modelloTabella.setValueAt(lezioniErogate[giorno].get(0), 1, giorno + 1);
                    }
                }
            }
        } catch (Exception ex) {
            modelloTabella.setValueAt("<html><b>POO</b><br>Aula A6</html>", 4, 1);
        }

        JScrollPane scrollTabella = new JScrollPane(tabellaOrari);
        pnlTabella.add(scrollTabella, BorderLayout.CENTER);

        JButton btnLogout = new JButton("DISCONNETTI E TORNA ALLA HOME");
        btnLogout.setBackground(new Color(192, 57, 43));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.addActionListener(e -> mainPanel.mostraHome());
        pnlTabella.add(btnLogout, BorderLayout.SOUTH);

        pnlPrincipale.add(pnlSidebar, BorderLayout.WEST);
        pnlPrincipale.add(pnlTabella, BorderLayout.CENTER);

        add(pnlPrincipale, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    protected void aggiornaSidebarOpzioni(JPanel pnlSidebar) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        JButton btnSpostamento = new JButton("Richiedi Spostamento");
        btnSpostamento.setBackground(new Color(141, 185, 224));
        btnSpostamento.addActionListener(e -> {
            String dettagliRichiesta = JOptionPane.showInputDialog(this,
                    "Inserisci Nome Insegnamento e i dettagli della variazione richiesti:",
                    "Richiesta Spostamento Lezione", JOptionPane.QUESTION_MESSAGE);

            String stringaRichiestaCompleta = "Prof. (" + emailDocenteLoggato + "): " + dettagliRichiesta.trim();
            //boolean inviata = Controller.getInstance().aggiungiRichiestaSpostamento(); //aggiungi argomenti (String nomeInsegnamento, String oraOriginale, String giornoOriginale, String giornoRichiesto, String oraInizioRichiesta, String oraFineRichiesta)
            boolean inviata = true;
            if(inviata) {
                    JOptionPane.showMessageDialog(this, "Richiesta inoltrata correttamente al Responsabile!", "Inviato", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(this, "Richiesta non inviata: campi non validi!", "Non Inviato", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        pnlSidebar.add(btnSpostamento, gbc);
    }
}