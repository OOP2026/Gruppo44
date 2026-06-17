package gui; // Posizionato nel pacchetto view su IntelliJ

import controller.Controller; // Importa il controller
import javax.swing.*;
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
        //in panel sdoppiato metteremo panel login a sx e panel registrazione a dx
        JPanel pnlSdoppiato = new JPanel(new GridLayout(1, 2, 30, 0));
        pnlSdoppiato.setBackground(new Color(235, 243, 249));


        // ================= CREO COLONNA SINISTRA: LOGIN =================
        JPanel pnlLogin = new JPanel(new GridBagLayout());
        pnlLogin.setBackground(Color.WHITE); // Sfondo bianco per staccare i moduli dallo sfondo
        pnlLogin.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(141, 185, 224), 2), "ACCEDI AL PORTALE", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(24, 43, 73)));

        GridBagConstraints gbcL = new GridBagConstraints(); //definisco regole

        gbcL.insets = new Insets(8, 12, 8, 12); //margini sopra, sinistra, sotto, destra
        gbcL.fill = GridBagConstraints.HORIZONTAL; //allargati orizzontalmente per occupare tutto lo spazio disponibile nella cella

        //gbcL(L=login) è un'istruzione che passi al pannello quando aggiungi il componente
        //prima di chiamare add devo aggiornare le sue proprietà
        //gridx=colonna gridy=riga
        //nella cella con riga0 e colonna0 aggiungo etichetta email studente
        gbcL.gridx = 0; gbcL.gridy = 0; pnlLogin.add(new JLabel("Email Studente:"), gbcL);

        //mantengo la stessa riga gridy=0 e mi sposto nella colonna gridx=1
        //aggiungo il campo di testo accanto la label
        txtLoginEmail = new JTextField(12);
        gbcL.gridx = 1; pnlLogin.add(txtLoginEmail, gbcL);

        //rimango nella colonna x=0 e vado nella cella in riga y=1 ed inserisco label password
        gbcL.gridx = 0; gbcL.gridy = 1; pnlLogin.add(new JLabel("Password:"), gbcL);
        txtLoginPassword = new JPasswordField(12);
        //mantengo riga y=1 e vado in colonna x=1 e inserisco password field
        gbcL.gridx = 1; pnlLogin.add(txtLoginPassword, gbcL);


        //CREO IL PULSANTE LOGIN
        JButton btnAccedi = new JButton("ACCEDI");
        btnAccedi.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAccedi.setBackground(new Color(141, 185, 224));
        btnAccedi.setFocusPainted(false);
        /*inserisco gridwidth=2 secondo cui
        il pulsante non deve occupare solo la cella (0,2), ma deve estendersi per 2 colonne*/
        gbcL.gridx = 0; gbcL.gridy = 2; gbcL.gridwidth = 2;
        gbcL.insets = new Insets(20, 12, 8, 12); //setto i margini
        pnlLogin.add(btnAccedi, gbcL); //inserisco il btnAccedi con le regole gbcL impostate prima


        // ================= CREO COLONNA DESTRA: REGISTRAZIONE =================
        JPanel pnlRegistrazione = new JPanel(new GridBagLayout());
        pnlRegistrazione.setBackground(Color.WHITE);
        pnlRegistrazione.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(168, 218, 220), 2), "NUOVA REGISTRAZIONE", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(44, 62, 80)));

        //stesse regole spiegate sopra con gbcR(R=registrazione)
        GridBagConstraints gbcR = new GridBagConstraints();
        gbcR.insets = new Insets(6, 12, 6, 12);
        gbcR.fill = GridBagConstraints.HORIZONTAL;

        //aggiungo tutti i campi per la registrazione dello studente
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

        // Definizione del combo box con soli 3 anni
        Integer[] anni = {1, 2, 3};
        comboAnno = new JComboBox<>(anni);

        // Posizionamento nel pannello (dopo la matricola)
        gbcR.gridx = 0; gbcR.gridy = 5;
        pnlRegistrazione.add(new JLabel("Anno di corso:"), gbcR);

        gbcR.gridx = 1;
        pnlRegistrazione.add(comboAnno, gbcR);

        //CREO PULSANTE REGISTRAZIONE
        JButton btnRegistrati = new JButton("REGISTRATI");
        btnRegistrati.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegistrati.setBackground(new Color(168, 218, 220));
        btnRegistrati.setFocusPainted(false);
        gbcR.gridx = 0; gbcR.gridy = 6; gbcR.gridwidth = 2;
        gbcR.insets = new Insets(15, 12, 6, 12);
        pnlRegistrazione.add(btnRegistrati, gbcR);

        // Aggiungiamo i due panel login e registrazione affiancati nel pannel sdoppiato
        pnlSdoppiato.add(pnlLogin);
        pnlSdoppiato.add(pnlRegistrazione);

        //aggiungo il panel sdoppiato nel panel autenticazione (primo panel creato per contenere login e registrazione)
        panelAutenticazione.add(pnlSdoppiato, BorderLayout.CENTER);

        // CREO PULSANTE PER TORNARE ALLA HOME
        JButton btnIndietroHome = new JButton("TORNA ALLA HOME PRINCIPALE");
        btnIndietroHome.setBackground(new Color(230, 126, 34));
        btnIndietroHome.setForeground(Color.WHITE);
        btnIndietroHome.setFocusPainted(false);
        panelAutenticazione.add(btnIndietroHome, BorderLayout.SOUTH); //aggiungo al panel principale

        // schermata di default della dashboard studente:
        // mostra inizialmente Login e Registrazione
        add(panelAutenticazione, BorderLayout.CENTER);


        // ================= GESTIONE DEI LISTENER SU BTN HOME, ACCEDI E REGISTRATI=================
        //
        //

        // Torna alla Home principale dell'ateneo
        /*actionPerformed(ActionEvent e): È il metodo che viene invocato automaticamente dal sistema
        nel momento esatto in cui avviene il click sul componente*/

        /*mainPanel.mostraHome(): È l'istruzione che esegui in risposta a quel click
         sfruttando il riferimento salvato per cambiare la vista dell'applicazione*/
        btnIndietroHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.mostraHome();
            }
        });


        // CLICK TASTO REGISTRATI -> Invia dati al Controller
        btnRegistrati.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = txtRegNome.getText().trim();
                String cognome = txtRegCognome.getText().trim();
                String email = txtRegEmail.getText().trim();
                String password = new String(txtRegPassword.getPassword());
                String matricola = txtRegMatricola.getText().trim();
                int annoSelezionato = (Integer) comboAnno.getSelectedItem();

                if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty() || matricola.isEmpty()) {
                    //pop up di errore
                    JOptionPane.showMessageDialog(DashboardStudente.this, "Errore: Tutti i campi di registrazione sono obbligatori!", "Campi Mancanti", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Chiamata diretta al controller
                Controller.getInstance().creaStudente(nome, cognome, email, password, matricola, annoSelezionato);

                JOptionPane.showMessageDialog(DashboardStudente.this, "Inserisci le credenziali nella sezione login per accedere.", "Registrazione Completata!", JOptionPane.INFORMATION_MESSAGE);

                // Pulizia campi registrazione dopo il successo
                txtRegNome.setText(""); txtRegCognome.setText(""); txtRegEmail.setText(""); txtRegPassword.setText(""); txtRegMatricola.setText("");
            }
        });


        // CLICK TASTO ACCEDI -> Autentica lo studente inviando dati al Controller e cambia vista interna
        btnAccedi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtLoginEmail.getText().trim();
                String password = new String(txtLoginPassword.getPassword());

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(DashboardStudente.this, "Errore: Inserisci sia l'email che la password!", "Campi Vuoti", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Chiama il login sul controller
                //FONDAMENTALE PER PASSAGGIO A TABELLONE ORARIO SE RICEVE TRUE
                boolean loginSuccesso = Controller.getInstance().login(email, password);

                if (loginSuccesso) {
                    // Memorizzo l'email e recupero l'anno dal controller per filtrare i dati
                    emailStudenteLoggato = email;
                    try {
                        annoStudenteLoggato = Controller.getInstance().getAnnoStudente(email);
                    } catch (Exception ex) {
                        annoStudenteLoggato = 1; // Fallback di sicurezza per i test grafici
                    }

                    JOptionPane.showMessageDialog(DashboardStudente.this, "Accesso eseguito con successo! Chiudi questo messaggio per visualizzare la tua dashboard", "Accesso Consentito!", JOptionPane.INFORMATION_MESSAGE);
                    passaAlTabelloneOrario(); // Nasconde i moduli e mostra il tabellone orario nello stesso pannello!
                } else {
                    JOptionPane.showMessageDialog(DashboardStudente.this, "Errore: Credenziali non valide o profilo non registrato!", "Accesso Negato!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }


    // --- 2. CAMBIO DI STATO INTERNO: RIMOZIONE MODULI ACCESSO E COSTRUZIONE ORARIO STUDENTE ---
    //
    //

    private void passaAlTabelloneOrario() {
        this.removeAll(); // Rimuove completamente il pnlAutenticazione dallo schermo

        // Creiamo il pannello dell'orario
        JPanel pnlOrario = new JPanel(new BorderLayout(0, 15));
        pnlOrario.setBackground(new Color(235, 243, 249));
        pnlOrario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Intestazione dell'orario personalizzato
        JLabel lblOrarioTitolo = new JLabel("IL TUO ORARIO SETTIMANALE - " + annoStudenteLoggato + "° ANNO", JLabel.CENTER);
        lblOrarioTitolo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblOrarioTitolo.setForeground(new Color(24, 43, 73));
        pnlOrario.add(lblOrarioTitolo, BorderLayout.NORTH);

        //Costruzione della Tabella dell'Orario

        // Definizione delle 6 colonne (Orario + 5 Giorni)
        String[] colonne = {"Orario", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"};

        // Creazione del modello con 9 righe iniziali (una per ogni slot orario)
        DefaultTableModel modelloTabella = new DefaultTableModel(colonne, 9);
        JTable tabellaOrari = new JTable(modelloTabella);

        // Inserimento manuale degli orari nella prima colonna
        String[] orari = {"08:30", "09:30", "10:30", "11:30", "12:30", "13:30", "14:30", "15:30", "16:30"};
        for (int i = 0; i < orari.length; i++) {
            modelloTabella.setValueAt(orari[i], i, 0); // i=riga, 0=prima colonna
        }

        // CARICAMENTO ORARIO DINAMICO DAL CONTROLLER
        try {
            ArrayList<String>[] lezioniAnno = Controller.getInstance().getLezioni(emailStudenteLoggato);
            if (lezioniAnno != null) {
                for (int g = 0; g < 5; g++) {
                    if (!lezioniAnno[g].isEmpty()) {
                        modelloTabella.setValueAt(lezioniAnno[g].get(0), 1, g + 1);
                    }
                }
            }
        } catch (Exception ex) {
            // Test di riserva se il controller restituisce provvisoriamente null
            modelloTabella.setValueAt("<html><b>Geometria</b><br>Aula A5</html>", 1, 1);
        }

        tabellaOrari.setRowHeight(35);
        tabellaOrari.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollTabella = new JScrollPane(tabellaOrari);
        pnlOrario.add(scrollTabella, BorderLayout.CENTER);


        // ================= SEZIONE INFERIORE CON AVVISI + LOGOUT =================

        // Creiamo un pannello contenitore per la zona sud (Avvisi + Pulsante)
        JPanel pnlInferiore = new JPanel(new BorderLayout(0, 12));
        pnlInferiore.setBackground(new Color(235, 243, 249));

        // Pannello specifico per la bacheca degli avvisi
        JPanel pnlAvvisi = new JPanel(new BorderLayout());
        pnlAvvisi.setBackground(Color.WHITE);
        // Bordo arancione/rosso per attirare l'attenzione sugli spostamenti
        pnlAvvisi.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(230, 126, 34), 2),
                "AVVISI E VARIAZIONI ORARIO",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(192, 57, 43)
        ));

        // JTextArea per elencare gli avvisi ricevuti dal responsabile
        JTextArea txtAvvisi = new JTextArea(4, 30);
        txtAvvisi.setEditable(false);
        txtAvvisi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtAvvisi.setLineWrap(true);
        txtAvvisi.setWrapStyleWord(true);

        // Testo di esempio (In seguito lo si popola dinamicamente chiamando il Controller)
        try {
            ArrayList<String> variazioniBacheca = Controller.getInstance().getVariazioni(annoStudenteLoggato);
            if (variazioniBacheca != null && !variazioniBacheca.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String avviso : variazioniBacheca) {
                    sb.append("• ").append(avviso).append("\n");
                }
                txtAvvisi.setText(sb.toString());
            } else {
                txtAvvisi.setText("• Non ci sono altre variazioni recenti per il tuo anno di corso.");
            }
        } catch (Exception ex) {
            txtAvvisi.setText("• [APPROVATO] Lo spostamento della lezione di 'Ingegneria del Software' a Venerdì ore 14:30 è stato confermato dal Responsabile.\n" +
                    "• Non ci sono altre variazioni recenti per il tuo anno di corso.");
        }

        JScrollPane scrollAvvisi = new JScrollPane(txtAvvisi);
        pnlAvvisi.add(scrollAvvisi, BorderLayout.CENTER);

        // Pulsante di LOGOUT
        JButton btnLogout = new JButton("LOGOUT");
        btnLogout.setBackground(new Color(192, 57, 43)); // Rosso scuro
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);

        // Assembliamo i componenti nel pannello inferiore
        pnlInferiore.add(pnlAvvisi, BorderLayout.CENTER);  // Gli avvisi prendono lo spazio centrale del sotto-pannello
        pnlInferiore.add(btnLogout, BorderLayout.SOUTH);   // Il pulsante rimane agganciato sul fondo

        // Posizioniamo l'intero blocco inferiore nella zona SOUTH di pnlOrario
        pnlOrario.add(pnlInferiore, BorderLayout.SOUTH);

        // ===================================================================


        // LISTENER PULSANTE LOGOUT
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Riporta l'utente alla Home dell'ateneo
                mainPanel.mostraHome();
            }
        });

        // Aggiunge il nuovo pannello orario e aggiorna forzatamente l'interfaccia di Swing
        add(pnlOrario, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}