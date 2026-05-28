package gui; // Posizionato nel pacchetto view su IntelliJ

import controller.Controller; // Importa il controller
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

//
//SEZIONE 4: GESTIONE STUDENTE
//COMPRENDE tutto il flusso dello studente (Entra, Registrati, Guarda Orario)
//

public class DashboardStudente extends JPanel {

    private MainPanel mainPanel; // Per tornare alla Home se si preme "Indietro"

    // Pannelli principali per lo switch di stato
    private JPanel panelAutenticazione;

    // Definisco i campi del modulo LOGIN (Sinistra)
    private JTextField txtLoginEmail;
    private JPasswordField txtLoginPassword;

    // Definisco i campi del modulo REGISTRAZIONE (Destra)
    private JTextField txtRegNome;
    private JTextField txtRegCognome;
    private JTextField txtRegEmail;
    private JPasswordField txtRegPassword;
    private JTextField txtRegMatricola;
    private JComboBox<Integer> comboAnno;

    // ATTRIBUTI DI SESSIONE GRAFICA (Nuovi: servono a salvare lo stato dello studente loggato)
    private String emailStudenteLoggato;
    private int annoStudenteLoggato;

    public DashboardStudente(MainPanel mainPanel) {
        this.mainPanel = mainPanel;

        // Impostiamo il layout principale della dashboard
        setLayout(new BorderLayout());
        setBackground(new Color(235, 243, 249)); // Azzurro pastello

        // 1. COSTRUZIONE DEL PANNELLO DI AUTENTICAZIONE (LOGIN + REGISTRAZIONE)

        panelAutenticazione = new JPanel(new BorderLayout(0, 15));
        panelAutenticazione.setBackground(new Color(235, 243, 249));
        panelAutenticazione.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titolo superiore del portale
        JLabel lblTitoloPortale = new JLabel("PORTALE STUDENTI - FEDERICO II", JLabel.CENTER);
        lblTitoloPortale.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitoloPortale.setForeground(new Color(24, 43, 73));
        panelAutenticazione.add(lblTitoloPortale, BorderLayout.NORTH);

        // Pannello centrale sdoppiato (1 riga, 2 colonne con 30 pixel di spazio nel mezzo)
        JPanel pnlSdoppiato = new JPanel(new GridLayout(1, 2, 30, 0));
        pnlSdoppiato.setBackground(new Color(235, 243, 249));

        // ================= CREO COLONNA SINISTRA: LOGIN =================
        JPanel pnlLogin = new JPanel(new GridBagLayout());
        pnlLogin.setBackground(Color.WHITE); // Sfondo bianco per staccare i moduli dallo sfondo
        pnlLogin.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(141, 185, 224), 2), "ACCEDI AL PORTALE", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(24, 43, 73)));

        GridBagConstraints gbcL = new GridBagConstraints(); //definisco regole
        gbcL.insets = new Insets(8, 12, 8, 12); //margini sopra, sinistra, sotto, destra
        gbcL.fill = GridBagConstraints.HORIZONTAL; //allargati orizzontalmente per occupare tutto lo spazio disponibile nella cella

        gbcL.gridx = 0; gbcL.gridy = 0; pnlLogin.add(new JLabel("Email Studente:"), gbcL);
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
        gbcL.insets = new Insets(20, 12, 8, 12); //setto i margini
        pnlLogin.add(btnAccedi, gbcL); //inserisco il btnAccedi con le regole gbcL impostate prima

        // ================= CREO COLONNA DESTRA: REGISTRAZIONE =================
        JPanel pnlRegistrazione = new JPanel(new GridBagLayout());
        pnlRegistrazione.setBackground(Color.WHITE);
        pnlRegistrazione.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(168, 218, 220), 2), "NUOVA REGISTRAZIONE", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(44, 62, 80)));

        GridBagConstraints gbcR = new GridBagConstraints();
        gbcR.insets = new Insets(6, 12, 6, 12);
        gbcR.fill = GridBagConstraints.HORIZONTAL;

        gbcR.gridx = 0; gbcR.gridy = 0; pnlRegistrazione.add(new JLabel("Nome:"), gbcR);
        txtRegNome = new JTextField(12); gbcR.gridx = 1; pnlRegistrazione.add(txtRegNome, gbcR);
        gbcR.gridx = 0; gbcR.gridy = 1; pnlRegistrazione.add(new JLabel("Cognome:"), gbcR);
        txtRegCognome = new JTextField(12); gbcR.gridx = 1; pnlRegistrazione.add(txtRegCognome, gbcR);
        gbcR.gridx = 0; gbcR.gridy = 2; pnlRegistrazione.add(new JLabel("Email:"), gbcR);
        txtRegEmail = new JTextField(12); gbcR.gridx = 1; pnlRegistrazione.add(txtRegEmail, gbcR);
        gbcR.gridx = 0; gbcR.gridy = 3; pnlRegistrazione.add(new JLabel("Password:"), gbcR);
        txtRegPassword = new JPasswordField(12); gbcR.gridx = 1; pnlRegistrazione.add(txtRegPassword, gbcR);
        gbcR.gridx = 0; gbcR.gridy = 4; pnlRegistrazione.add(new JLabel("Matricola:"), gbcR);
        txtRegMatricola = new JTextField(12); gbcR.gridx = 1; pnlRegistrazione.add(txtRegMatricola, gbcR);

        Integer[] anni = {1, 2, 3};
        comboAnno = new JComboBox<>(anni);
        gbcR.gridx = 0; gbcR.gridy = 5; pnlRegistrazione.add(new JLabel("Anno di corso:"), gbcR);
        gbcR.gridx = 1; pnlRegistrazione.add(comboAnno, gbcR);

        JButton btnRegistrati = new JButton("REGISTRATI");
        btnRegistrati.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegistrati.setBackground(new Color(168, 218, 220));
        btnRegistrati.setFocusPainted(false);
        gbcR.gridx = 0; gbcR.gridy = 6; gbcR.gridwidth = 2;
        gbcR.insets = new Insets(15, 12, 6, 12);
        pnlRegistrazione.add(btnRegistrati, gbcR);

        pnlSdoppiato.add(pnlLogin);
        pnlSdoppiato.add(pnlRegistrazione);
        panelAutenticazione.add(pnlSdoppiato, BorderLayout.CENTER);

        JButton btnIndietroHome = new JButton("TORNA ALLA HOME PRINCIPALE");
        btnIndietroHome.setBackground(new Color(230, 126, 34));
        btnIndietroHome.setForeground(Color.WHITE);
        btnIndietroHome.setFocusPainted(false);
        panelAutenticazione.add(btnIndietroHome, BorderLayout.SOUTH);

        add(panelAutenticazione, BorderLayout.CENTER);

        btnIndietroHome.addActionListener(e -> mainPanel.mostraHome());

        btnRegistrati.addActionListener(e -> {
            String nome = txtRegNome.getText().trim();
            String cognome = txtRegCognome.getText().trim();
            String email = txtRegEmail.getText().trim();
            String password = new String(txtRegPassword.getPassword());
            String matricola = txtRegMatricola.getText().trim();
            int annoSelezionato = (Integer) comboAnno.getSelectedItem();

            if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty() || matricola.isEmpty()) {
                JOptionPane.showMessageDialog(DashboardStudente.this, "Errore: Tutti i campi di registrazione sono obbligatori!", "Campi Mancanti", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Controller.getInstance().creaStudente(nome, cognome, email, password, matricola, annoSelezionato);
            JOptionPane.showMessageDialog(DashboardStudente.this, "Registrazione completata! Inserisci le credenziali nel modulo di sinistra per accedere.", "Registrazione OK", JOptionPane.INFORMATION_MESSAGE);

            txtRegNome.setText(""); txtRegCognome.setText(""); txtRegEmail.setText(""); txtRegPassword.setText(""); txtRegMatricola.setText("");
        });

        btnAccedi.addActionListener(e -> {
            String email = txtLoginEmail.getText().trim();
            String password = new String(txtLoginPassword.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(DashboardStudente.this, "Errore: Inserisci sia l'email che la password!", "Campi Vuoti", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean loginSuccesso = Controller.getInstance().login(email, password);
            if (loginSuccesso) {
                emailStudenteLoggato = email;
                try {
                    annoStudenteLoggato = Controller.getInstance().getAnnoStudente(email);
                } catch (Exception ex) {
                    annoStudenteLoggato = 1;
                }
                JOptionPane.showMessageDialog(DashboardStudente.this, "Accesso eseguito con successo!", "Login Corretto", JOptionPane.INFORMATION_MESSAGE);
                passaAlTabelloneOrario();
            } else {
                JOptionPane.showMessageDialog(DashboardStudente.this, "Errore: Credenziali non valide!", "Errore Accesso", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    //
    //STRUTTURA DELLA TABELLA (AGGIORNATA PER TESTO MULTIRIGA)
    private void popolaTabellaDinamica(DefaultTableModel modello, String email) {
        // 1. Pulisci la tabella
        for (int r = 0; r < modello.getRowCount(); r++) {
            for (int c = 1; c < modello.getColumnCount(); c++) {
                modello.setValueAt("", r, c);
            }
        }

        // 2. Chiamata protetta al controller
        ArrayList<String>[] lezioniErogate = Controller.getInstance().getLezioni(email);

        // 3. Verifica se il risultato è nullo (nessun dato o errore di comunicazione)
        if (lezioniErogate == null) {
            System.out.println("DEBUG: Nessuna lezione restituita dal controller per: " + email);
            return;
        }

        // 4. Inizializziamo il flag per monitorare se troviamo lezioni
        boolean trovataAlmenoUnaLezione = false;

        // 5. Ciclo sicuro
        for (int giorno = 0; giorno < 5; giorno++) {
            // Controllo che l'array del giorno non sia null e contenga dati
            if (lezioniErogate[giorno] != null && !lezioniErogate[giorno].isEmpty()) {
                trovataAlmenoUnaLezione = true;

                for (String raw : lezioniErogate[giorno]) {
                    try {
                        String[] parti = raw.split(",");
                        if (parti.length >= 3) {
                            String contenuto = parti[1].trim() + "\n" + parti[2].trim() + "\n(" + parti[0].trim() + ")";
                            int riga = getRigaDaOrario(parti[0].trim());
                            modello.setValueAt(contenuto, riga, giorno + 1);
                        } else {
                            System.out.println("DEBUG: Formato stringa errato (mancano le virgole): " + raw);
                        }
                    } catch (Exception e) {
                        System.err.println("DEBUG: Errore nel processare la lezione per lo studente: " + raw);
                    }
                }
            }
        }

        // 6. Log di sistema se lo studente non ha lezioni (utile per il debugging)
        if (!trovataAlmenoUnaLezione) {
            System.out.println("Nessuna lezione presente per questo studente.");
        }
    }

    private int getRigaDaOrario(String raw) {
        // MODIFICA CRITICA: Sostituiti gli orari ":30" con gli orari esatti ":00" per farli coincidere col Controller
        String[] orari = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00"};
        for (int i = 0; i < orari.length; i++) {
            if (raw.contains(orari[i])) return i;
        }
        return 0; // Se l'orario non viene trovato, andrà alla riga 0.
    }

    // Configura la tabella per interpretare i caratteri \n
    private void configuraVisualizzazioneMultiriga(JTable tabella) {
        tabella.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                JTextArea areaTesto = new JTextArea();
                areaTesto.setText(value != null ? value.toString() : "");
                areaTesto.setLineWrap(true);
                areaTesto.setWrapStyleWord(true);

                if (isSelected) {
                    areaTesto.setBackground(table.getSelectionBackground());
                    areaTesto.setForeground(table.getSelectionForeground());
                } else {
                    areaTesto.setBackground(table.getBackground());
                    areaTesto.setForeground(table.getForeground());
                }
                return areaTesto;
            }
        });
    }

    private void passaAlTabelloneOrario() {
        this.removeAll();

        JPanel pnlOrario = new JPanel(new BorderLayout(0, 15));
        pnlOrario.setBackground(new Color(235, 243, 249));
        pnlOrario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblOrarioTitolo = new JLabel("IL TUO ORARIO SETTIMANALE - " + annoStudenteLoggato + "° ANNO", JLabel.CENTER);
        lblOrarioTitolo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblOrarioTitolo.setForeground(new Color(24, 43, 73));
        pnlOrario.add(lblOrarioTitolo, BorderLayout.NORTH);

        String[] colonne = {"Orario", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"};
        DefaultTableModel modelloTabella = new DefaultTableModel(colonne, 9);
        JTable tabellaOrari = new JTable(modelloTabella);

        tabellaOrari.setRowHeight(80); // Altezza riga maggiore per ospitare il testo su più righe
        configuraVisualizzazioneMultiriga(tabellaOrari); // Applichiamo il nuovo renderer

        // MODIFICA CRITICA: Anche qui sostituiti gli orari per farli stampare correttamente nella tabella visiva
        String[] orari = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00"};
        for (int i = 0; i < orari.length; i++) {
            modelloTabella.setValueAt(orari[i], i, 0);
        }

        popolaTabellaDinamica(modelloTabella, emailStudenteLoggato);

        pnlOrario.add(new JScrollPane(tabellaOrari), BorderLayout.CENTER);

        JPanel pnlInferiore = new JPanel(new BorderLayout(0, 12));
        pnlInferiore.setBackground(new Color(235, 243, 249));

        JPanel pnlAvvisi = new JPanel(new BorderLayout());
        pnlAvvisi.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(230, 126, 34), 2),
                "AVVISI E VARIAZIONI ORARIO", 0, 0,
                new Font("Segoe UI", Font.BOLD, 12), new Color(192, 57, 43)));

        JTextArea txtAvvisi = new JTextArea(4, 30);
        txtAvvisi.setEditable(false);

        try {
            ArrayList<String> variazioni = Controller.getInstance().getVariazioni(annoStudenteLoggato);
            if (variazioni != null && !variazioni.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String a : variazioni) sb.append("• ").append(a).append("\n");
                txtAvvisi.setText(sb.toString());
            } else {
                txtAvvisi.setText("Nessuna variazione recente.");
            }
        } catch (Exception ex) {
            txtAvvisi.setText("Errore nel caricamento avvisi.");
        }

        pnlAvvisi.add(new JScrollPane(txtAvvisi), BorderLayout.CENTER);

        JButton btnLogout = new JButton("LOGOUT");
        btnLogout.setBackground(new Color(192, 57, 43));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.addActionListener(e -> mainPanel.mostraHome());

        pnlInferiore.add(pnlAvvisi, BorderLayout.CENTER);
        pnlInferiore.add(btnLogout, BorderLayout.SOUTH);

        pnlOrario.add(pnlInferiore, BorderLayout.SOUTH);

        this.add(pnlOrario, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }
}