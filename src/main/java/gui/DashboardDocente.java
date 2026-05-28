package gui;

import controller.Controller;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardDocente extends JPanel {

    protected MainPanel mainPanel;
    protected String emailDocenteLoggato;

    private JPanel panelAutenticazione;
    private JTextField txtLoginEmail;
    private JPasswordField txtLoginPassword;
    private JTextField txtRegNome;
    private JTextField txtRegCognome;
    private JTextField txtRegEmail;
    private JPasswordField txtRegPassword;
    protected JPanel pnlComandi;

    protected List<String[]> vincoliTemporanei = new ArrayList<>();

    public DashboardDocente(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());
        setBackground(new Color(235, 243, 249));

        // 1. PANNELLO AUTENTICAZIONE
        panelAutenticazione = new JPanel(new BorderLayout(0, 15));
        panelAutenticazione.setBackground(new Color(235, 243, 249));
        panelAutenticazione.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitoloPortale = new JLabel("PORTALE DOCENTI - UNIVERSITÀ FEDERICO II", JLabel.CENTER);
        lblTitoloPortale.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitoloPortale.setForeground(new Color(24, 43, 73));
        panelAutenticazione.add(lblTitoloPortale, BorderLayout.NORTH);

        JPanel pnlSdoppiato = new JPanel(new GridLayout(1, 2, 30, 0));
        pnlSdoppiato.setBackground(new Color(235, 243, 249));

        // LOGIN
        JPanel pnlLogin = new JPanel(new GridBagLayout());
        pnlLogin.setBackground(Color.WHITE);
        pnlLogin.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(141, 185, 224), 2),
                "ACCEDI AL PORTALE", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(24, 43, 73)));

        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.insets = new Insets(8, 12, 8, 12); gbcL.fill = GridBagConstraints.HORIZONTAL;
        gbcL.gridx = 0; gbcL.gridy = 0; pnlLogin.add(new JLabel("Email Docente:"), gbcL);
        txtLoginEmail = new JTextField(12); gbcL.gridx = 1; pnlLogin.add(txtLoginEmail, gbcL);
        gbcL.gridx = 0; gbcL.gridy = 1; pnlLogin.add(new JLabel("Password:"), gbcL);
        txtLoginPassword = new JPasswordField(12); gbcL.gridx = 1; pnlLogin.add(txtLoginPassword, gbcL);
        JButton btnAccedi = new JButton("ACCEDI");
        btnAccedi.setBackground(new Color(141, 185, 224));
        gbcL.gridx = 0; gbcL.gridy = 2; gbcL.gridwidth = 2; gbcL.insets = new Insets(20, 12, 8, 12);
        pnlLogin.add(btnAccedi, gbcL);

        // REGISTRAZIONE
        JPanel pnlRegistrazione = new JPanel(new GridBagLayout());
        pnlRegistrazione.setBackground(Color.WHITE);
        pnlRegistrationBorde(pnlRegistrazione);

        GridBagConstraints gbcR = new GridBagConstraints();
        gbcR.insets = new Insets(8, 12, 8, 12); gbcR.fill = GridBagConstraints.HORIZONTAL;
        gbcR.gridx = 0; gbcR.gridy = 0; pnlRegistrazione.add(new JLabel("Nome:"), gbcR);
        txtRegNome = new JTextField(12); gbcR.gridx = 1; pnlRegistrazione.add(txtRegNome, gbcR);
        gbcR.gridx = 0; gbcR.gridy = 1; pnlRegistrazione.add(new JLabel("Cognome:"), gbcR);
        txtRegCognome = new JTextField(12); gbcR.gridx = 1; pnlRegistrazione.add(txtRegCognome, gbcR);
        gbcR.gridx = 0; gbcR.gridy = 2; pnlRegistrazione.add(new JLabel("Email:"), gbcR);
        txtRegEmail = new JTextField(12); gbcR.gridx = 1; pnlRegistrazione.add(txtRegEmail, gbcR);
        gbcR.gridx = 0; gbcR.gridy = 3; pnlRegistrazione.add(new JLabel("Password:"), gbcR);
        txtRegPassword = new JPasswordField(12); gbcR.gridx = 1; pnlRegistrazione.add(txtRegPassword, gbcR);
        JButton btnApriVincoli = new JButton("Imposta Vincoli Orari");
        btnApriVincoli.setBackground(new Color(243, 156, 18));
        gbcR.gridx = 0; gbcR.gridy = 4; gbcR.gridwidth = 2; pnlRegistrazione.add(btnApriVincoli, gbcR);
        JButton btnRegistrati = new JButton("REGISTRATI");
        btnRegistrati.setBackground(new Color(168, 218, 220));
        gbcR.gridx = 0; gbcR.gridy = 5; pnlRegistrazione.add(btnRegistrati, gbcR);

        pnlSdoppiato.add(pnlLogin); pnlSdoppiato.add(pnlRegistrazione);
        panelAutenticazione.add(pnlSdoppiato, BorderLayout.CENTER);

        JButton btnIndietroHome = new JButton("LOGOUT");
        btnIndietroHome.setBackground(new Color(230, 126, 34));
        btnIndietroHome.setForeground(Color.WHITE);
        panelAutenticazione.add(btnIndietroHome, BorderLayout.SOUTH);
        add(panelAutenticazione, BorderLayout.CENTER);

        // Listeners
        btnIndietroHome.addActionListener(e -> mainPanel.mostraHome());
        btnApriVincoli.addActionListener(e -> apriPopUpVincoli());
        btnRegistrati.addActionListener(e -> {
            String email = txtRegEmail.getText().trim();
            Controller.getInstance().creaDocente(txtRegNome.getText(), txtRegCognome.getText(), email, new String(txtRegPassword.getPassword()));
            for (String[] v : vincoliTemporanei) Controller.getInstance().aggiungiVincoloDocente(email, v[0], v[1], v[2]);
            JOptionPane.showMessageDialog(this, "Registrazione completata!");
            vincoliTemporanei.clear();
        });
        btnAccedi.addActionListener(e -> {
            String email = txtLoginEmail.getText().trim();
            String password = new String(txtLoginPassword.getPassword());

            // 1. Controllo credenziali Responsabile
            if (email.equals("resp@unina.it") && password.equals("resp")) {
                this.removeAll();
                this.add(new DashboardResponsabile(mainPanel));
                this.revalidate();
                this.repaint();
            }
            // 2. Login Standard Docente
            else if (Controller.getInstance().login(email, password)) {
                this.emailDocenteLoggato = email;
                passaAlTabelloneDocente();
            }
            // 3. Credenziali errate
            else {
                JOptionPane.showMessageDialog(this, "Credenziali non valide!");
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
        dialog.setSize(500, 250); dialog.setLayout(new BorderLayout(10, 10)); dialog.setLocationRelativeTo(this);
        JPanel pnlCentrale = new JPanel(new GridLayout(4, 4, 5, 5));
        String[] giorni = {"LUNEDI", "MARTEDI", "MERCOLEDI", "GIOVEDI", "VENERDI"};
        String[] orari = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00"};
        JCheckBox[] checks = new JCheckBox[3]; JComboBox<String>[] cGiorno = new JComboBox[3], cInizio = new JComboBox[3], cFine = new JComboBox[3];
        for (int i = 0; i < 3; i++) {
            checks[i] = new JCheckBox(); cGiorno[i] = new JComboBox<>(giorni); cInizio[i] = new JComboBox<>(orari); cFine[i] = new JComboBox<>(orari);
            pnlCentrale.add(checks[i]); pnlCentrale.add(cGiorno[i]); pnlCentrale.add(cInizio[i]); pnlCentrale.add(cFine[i]);
        }
        JButton btnSalva = new JButton("SALVA");
        btnSalva.addActionListener(e -> {
            vincoliTemporanei.clear();
            for (int i = 0; i < 3; i++) if (checks[i].isSelected()) vincoliTemporanei.add(new String[]{(String)cGiorno[i].getSelectedItem(), (String)cInizio[i].getSelectedItem(), (String)cFine[i].getSelectedItem()});
            dialog.dispose();
        });
        dialog.add(pnlCentrale, BorderLayout.CENTER); dialog.add(btnSalva, BorderLayout.SOUTH); dialog.setVisible(true);
    }

    //  METODI PER GESTIONE POST-LOGIN

    private void apriDialogVincoliDocente() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Aggiungi Vincolo", true);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));
        JComboBox<String> cG = new JComboBox<>(new String[]{"LUNEDI", "MARTEDI", "MERCOLEDI", "GIOVEDI", "VENERDI"});
        JComboBox<String> cI = new JComboBox<>(new String[]{"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00"});
        JComboBox<String> cF = new JComboBox<>(new String[]{"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00"});
        JButton btnOk = new JButton("Salva");
        dialog.add(new JLabel("Giorno:")); dialog.add(cG);
        dialog.add(new JLabel("Inizio:")); dialog.add(cI);
        dialog.add(new JLabel("Fine:")); dialog.add(cF);
        dialog.add(btnOk);
        btnOk.addActionListener(e -> {
            Controller.getInstance().aggiungiVincoloDocente(emailDocenteLoggato, (String)cG.getSelectedItem(), (String)cI.getSelectedItem(), (String)cF.getSelectedItem());
            JOptionPane.showMessageDialog(dialog, "Vincolo aggiunto!");
            dialog.dispose();
        });
        dialog.pack(); dialog.setLocationRelativeTo(this); dialog.setVisible(true);
    }

    private void apriDialogSpostamento() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Richiedi Spostamento", true);
        dialog.setLayout(new GridLayout(7, 2, 5, 5));
        JTextField txtIns = new JTextField(); JTextField txtOraOrig = new JTextField();
        JTextField txtGiornoOrig = new JTextField(); JTextField txtGiornoDest = new JTextField();
        JTextField txtOraInizio = new JTextField(); JTextField txtOraFine = new JTextField();
        JButton btnInvio = new JButton("Invia");
        dialog.add(new JLabel("Insegnamento:")); dialog.add(txtIns);
        dialog.add(new JLabel("Ora Originale:")); dialog.add(txtOraOrig);
        dialog.add(new JLabel("Giorno Originale:")); dialog.add(txtGiornoOrig);
        dialog.add(new JLabel("Nuovo Giorno:")); dialog.add(txtGiornoDest);
        dialog.add(new JLabel("Nuovo Inizio:")); dialog.add(txtOraInizio);
        dialog.add(new JLabel("Nuova Fine:")); dialog.add(txtOraFine);
        dialog.add(btnInvio);
        btnInvio.addActionListener(e -> {
            boolean ok = Controller.getInstance().aggiungiRichiestaSpostamento(txtIns.getText(), txtOraOrig.getText(), txtGiornoOrig.getText(), txtGiornoDest.getText(), txtOraInizio.getText(), txtOraFine.getText());
            JOptionPane.showMessageDialog(dialog, ok ? "Richiesta inviata!" : "Errore!");
            dialog.dispose();
        });
        dialog.pack(); dialog.setLocationRelativeTo(this); dialog.setVisible(true);
    }

    //TABELLA CON CELLE SISTEMATE

    private void popolaTabellaConDati(DefaultTableModel modello, String email) {
        for (int r = 0; r < modello.getRowCount(); r++) for (int c = 1; c < modello.getColumnCount(); c++) modello.setValueAt("", r, c);
        ArrayList<String>[] lezioni = Controller.getInstance().getLezioni(email);
        if (lezioni == null) return;
        for (int giorno = 0; giorno < 5; giorno++) {
            if (lezioni[giorno] != null) {
                for (String raw : lezioni[giorno]) {
                    String[] parti = raw.split(",");
                    if (parti.length >= 3) modello.setValueAt(parti[1].trim() + "\n" + parti[2].trim() + "\n(" + parti[0].trim() + ")", getRigaDaOrario(parti[0].trim()), giorno + 1);
                }
            }
        }
    }

    private int getRigaDaOrario(String raw) {
        String[] orari = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00"};
        for (int i = 0; i < orari.length; i++) if (raw.contains(orari[i])) return i;
        return 0;
    }

    private void configuraVisualizzazioneMultiriga(JTable tabella) {
        tabella.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                JTextArea area = new JTextArea(value != null ? value.toString() : "");
                area.setLineWrap(true); area.setWrapStyleWord(true);
                if (isSelected) { area.setBackground(table.getSelectionBackground()); area.setForeground(table.getSelectionForeground()); }
                else { area.setBackground(table.getBackground()); area.setForeground(table.getForeground()); }
                return area;
            }
        });
    }

    protected void passaAlTabelloneDocente() {
        this.removeAll();
        setLayout(new BorderLayout());
        JPanel pnlPrincipale = new JPanel(new BorderLayout(15, 15));
        pnlPrincipale.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));


        // Pulsanti Docente
        this.pnlComandi = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnVincoli = new JButton("Aggiungi Vincolo");
        JButton btnSpostamento = new JButton("Richiedi Spostamento");
        btnVincoli.addActionListener(e -> apriDialogVincoliDocente());
        btnSpostamento.addActionListener(e -> apriDialogSpostamento());
        pnlComandi.add(btnVincoli); pnlComandi.add(btnSpostamento);
        pnlPrincipale.add(pnlComandi, BorderLayout.NORTH);

        String[] col = {"Orario", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"};
        DefaultTableModel mod = new DefaultTableModel(col, 9);
        JTable tabella = new JTable(mod);
        tabella.setRowHeight(80);
        configuraVisualizzazioneMultiriga(tabella);

        String[] orari = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00"};
        for (int i = 0; i < orari.length; i++) mod.setValueAt(orari[i], i, 0);

        popolaTabellaConDati(mod, emailDocenteLoggato);

        pnlPrincipale.add(new JScrollPane(tabella), BorderLayout.CENTER);
        JButton btnLogout = new JButton("LOGOUT");
        btnLogout.addActionListener(e -> mainPanel.mostraHome());
        pnlPrincipale.add(btnLogout, BorderLayout.SOUTH);

        this.add(pnlPrincipale, BorderLayout.CENTER);
        revalidate(); repaint();
    }
}