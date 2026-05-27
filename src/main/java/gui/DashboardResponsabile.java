package view;

import controller.Controller;
import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class DashboardResponsabile extends DashboardDocente {

    public DashboardResponsabile(MainPanel mainPanel, String email) {
        super(mainPanel);
        this.emailDocenteLoggato = email;
        // Salta l'autenticazione ed entra direttamente nel tabellone di controllo
        passaAlTabelloneDocente();
    }

    // Sovrascrive la sidebar per mostrare solo i poteri del Responsabile Porfirio Tramontana
    @Override
    protected void aggiornaSidebarOpzioni(JPanel pnlSidebar) {
        pnlSidebar.removeAll(); // Rimuove il bottone "Invia Richiesta" del docente comune

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        JLabel lblRuolo = new JLabel("RUOLO: RESPONSABILE", JLabel.CENTER);
        lblRuolo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblRuolo.setForeground(new Color(192, 57, 43));
        pnlSidebar.add(lblRuolo, gbc);


        // =====================================================================
        // 1. CORREZIONE: GESTIONE RICHIESTE REALI CON APPROVA / RIFIUTA
        // =====================================================================
        gbc.gridy = 1;
        JButton btnVediRichieste = new JButton("Visualizza Richieste Spostamento");
        btnVediRichieste.setBackground(new Color(168, 218, 220));

        btnVediRichieste.addActionListener(e -> {
            // Creiamo una finestra di dialogo pop-up (JDialog) personalizzata per mostrare la lista e i bottoni
            JDialog dialogRichieste = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Bacheca Richieste Ricevute", true);
            dialogRichieste.setLayout(new BorderLayout(10, 10));
            dialogRichieste.setSize(550, 350);
            dialogRichieste.setLocationRelativeTo(this);

            // JList grafica per mostrare le richieste
            JList<String> listaRichiesteGrafica = new JList<>();
            listaRichiesteGrafica.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // Sotto-funzione per aggiornare la lista in tempo reale dentro il pop-up
            Runnable rinfrescaLista = () -> {
                ArrayList<String> richiesteReali = Controller.getInstance().getRegistroRichiesteSpostamento();
                if (richiesteReali.isEmpty()) {
                    listaRichiesteGrafica.setListData(new String[]{"Nessuna richiesta presente"});
                } else {
                    listaRichiesteGrafica.setListData(richiesteReali.toArray(new String[0]));
                }
            };

            // Primo caricamento dati
            rinfrescaLista.run();
            dialogRichieste.add(new JScrollPane(listaRichiesteGrafica), BorderLayout.CENTER);

            // Pannello inferiore con i pulsanti Approva e Rifiuta
            JPanel pnlBottoniAzioni = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            JButton btnApprova = new JButton("✅ Approva");
            JButton btnRifiuta = new JButton("❌ Rifiuta");

            btnApprova.setBackground(new Color(46, 204, 113));
            btnApprova.setForeground(Color.WHITE);
            btnRifiuta.setBackground(new Color(231, 76, 60));
            btnRifiuta.setForeground(Color.WHITE);

            pnlBottoniAzioni.add(btnApprova);
            pnlBottoniAzioni.add(btnRifiuta);
            dialogRichieste.add(pnlBottoniAzioni, BorderLayout.SOUTH);

            // Logica del Bottone Approva
            btnApprova.addActionListener(ae -> {
                int indiceSelezionato = listaRichiesteGrafica.getSelectedIndex();
                ArrayList<String> richiesteAttuali = Controller.getInstance().getRegistroRichiesteSpostamento();

                if (indiceSelezionato != -1 && !richiesteAttuali.isEmpty()) {
                    Controller.getInstance().approvaRichiesta(indiceSelezionato);
                    rinfrescaLista.run(); // Rinfresca lo schermo
                    JOptionPane.showMessageDialog(dialogRichieste, "Richiesta approvata con successo!");
                } else {
                    JOptionPane.showMessageDialog(dialogRichieste, "Seleziona una richiesta valida da approvare.");
                }
            });

            // Logica del Bottone Rifiuta
            btnRifiuta.addActionListener(ae -> {
                int indiceSelezionato = listaRichiesteGrafica.getSelectedIndex();
                ArrayList<String> richiesteAttuali = Controller.getInstance().getRegistroRichiesteSpostamento();

                if (indiceSelezionato != -1 && !richiesteAttuali.isEmpty()) {
                    Controller.getInstance().rifiutaRichiesta(indiceSelezionato);
                    rinfrescaLista.run(); // Rinfresca lo schermo
                    JOptionPane.showMessageDialog(dialogRichieste, "Richiesta rifiutata e rimossa dal sistema.");
                } else {
                    JOptionPane.showMessageDialog(dialogRichieste, "Seleziona una richiesta valida da rifiutare.");
                }
            });

            dialogRichieste.setVisible(true);
        });
        pnlSidebar.add(btnVediRichieste, gbc);


        // =====================================================================
        // 2. CORREZIONE: VISUALIZZA VINCOLI REALI DAL CONTROLLER
        // =====================================================================
        gbc.gridy = 2;
        JButton btnVediVincoli = new JButton("Visualizza Vincoli Orari Docenti");
        btnVediVincoli.setBackground(new Color(141, 185, 224));

        btnVediVincoli.addActionListener(e -> {
            // Recuperiamo la mappa reale dei vincoli dal controller
            HashMap<String, ArrayList<String>> mappaVincoli = Controller.getInstance().getRegistroVincoliDocenti();
            StringBuilder riepilogoTestuale = new StringBuilder("Registro Vincoli Orari di Sistema:\n\n");

            if (mappaVincoli.isEmpty()) {
                riepilogoTestuale.append("Nessun docente ha inserito vincoli temporali al momento.");
            } else {
                // Cicliamo sulla mappa per costruire la stringa dinamica
                mappaVincoli.forEach((emailDocente, listaVincoli) -> {
                    riepilogoTestuale.append("• Docente: ").append(emailDocente).append("\n");
                    for (String vincolo : listaVincoli) {
                        riepilogoTestuale.append("  - ").append(vincolo).append("\n");
                    }
                    riepilogoTestuale.append("\n");
                });
            }

            JOptionPane.showMessageDialog(this, riepilogoTestuale.toString(),
                    "Anagrafica Vincoli Docenti", JOptionPane.INFORMATION_MESSAGE);
        });
        pnlSidebar.add(btnVediVincoli, gbc);


        // =====================================================================
        // 3 e 4. CORREZIONE: INSERIMENTO LEZIONE NON CASUALE MA TRAMITE FORM GUI
        // =====================================================================
        gbc.gridy = 3;
        JButton btnGeneraOrario = new JButton("➕ AGGIUNGI NUOVA LEZIONE");
        btnGeneraOrario.setBackground(new Color(46, 204, 113));
        btnGeneraOrario.setForeground(Color.WHITE);
        btnGeneraOrario.setFont(new Font("Segoe UI", Font.BOLD, 11));

        btnGeneraOrario.addActionListener(e -> {
            // Creiamo un piccolo pannello form di inserimento dati a runtime
            JPanel pnlFormInnesco = new JPanel(new GridLayout(5, 2, 8, 8));

            JTextField txtInsegnamento = new JTextField("Programmazione Object-Oriented");
            JComboBox<model.GiornoSettimana> cmbGiorno = new JComboBox<>(model.GiornoSettimana.values());
            JTextField txtInizio = new JTextField("11:30");
            JTextField txtFine = new JTextField("13:30");
            JTextField txtAula = new JTextField("A6");

            pnlFormInnesco.add(new JLabel("Insegnamento:"));   pnlFormInnesco.add(txtInsegnamento);
            pnlFormInnesco.add(new JLabel("Giorno:"));         pnlFormInnesco.add(cmbGiorno);
            pnlFormInnesco.add(new JLabel("Ora Inizio (HH:mm):")); pnlFormInnesco.add(txtInizio);
            pnlFormInnesco.add(new JLabel("Ora Fine (HH:mm):"));   pnlFormInnesco.add(txtFine);
            pnlFormInnesco.add(new JLabel("Aula:"));           pnlFormInnesco.add(txtAula);

            int opzioneScelta = JOptionPane.showConfirmDialog(this, pnlFormInnesco,
                    "Pianificazione Nuova Lezione Amministrativa", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (opzioneScelta == JOptionPane.OK_OPTION) {
                try {
                    // Estraiamo i dati digitati realmente dal Responsabile sul form grafico
                    String nomeCorso = txtInsegnamento.getText();
                    model.GiornoSettimana giorno = (model.GiornoSettimana) cmbGiorno.getSelectedItem();
                    LocalTime oraInizio = LocalTime.parse(txtInizio.getText());
                    LocalTime oraFine = LocalTime.parse(txtFine.getText());
                    String nomeAula = txtAula.getText();

                    // Invocazione reale del metodo del controller del collega
                    Controller.getInstance().creaLezione(nomeCorso, giorno, oraInizio, oraFine, nomeAula);

                    JOptionPane.showMessageDialog(this,
                            "La lezione è stata validata ed inserita nel calendario generale di sistema.",
                            "Pianificazione Riuscita", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Errore di inserimento: Verifica che il formato dell'orario sia corretto (es. 09:30).",
                            "Errore Formattazione Dati", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        pnlSidebar.add(btnGeneraOrario, gbc);
    }
}