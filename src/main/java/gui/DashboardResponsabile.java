package gui;

import controller.Controller;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class DashboardResponsabile extends DashboardDocente {

    public DashboardResponsabile(MainPanel mainPanel) {
        super(mainPanel);
        this.emailDocenteLoggato = "resp@unina.it";
        passaAlTabelloneDocente();
    }

    @Override
    protected void passaAlTabelloneDocente() {
        super.passaAlTabelloneDocente();

        JButton btnGestisciRichieste = new JButton("Gestisci Richieste Spostamento");
        btnGestisciRichieste.setBackground(new Color(231, 76, 60));
        btnGestisciRichieste.setForeground(Color.WHITE);

        btnGestisciRichieste.addActionListener(e -> apriFinestraGestioneRichieste());

        if (pnlComandi != null) {
            pnlComandi.add(btnGestisciRichieste);
        }

        this.revalidate();
        this.repaint();
    }

    private void apriFinestraGestioneRichieste() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Gestione Richieste", true);
        dialog.setSize(600, 450);
        dialog.setLayout(new BorderLayout());

        String[] colonne = {"Richiesta"};
        DefaultTableModel model = new DefaultTableModel(colonne, 0);
        JTable table = new JTable(model);

        // Label per il feedback in tempo reale
        JLabel lblEsitoPrevalidazione = new JLabel("Seleziona una richiesta per vedere l'idoneità...", SwingConstants.CENTER);
        lblEsitoPrevalidazione.setFont(new Font("Arial", Font.BOLD, 12));
        lblEsitoPrevalidazione.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Listener per la pre-validazione
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int riga = table.getSelectedRow();
                if (riga != -1) {
                    String esito = Controller.getInstance().validaRichiesta(riga);
                    lblEsitoPrevalidazione.setText("Stato: " + esito);
                    if (!esito.equals("IDONEA")) {
                        lblEsitoPrevalidazione.setForeground(Color.RED);
                    } else {
                        lblEsitoPrevalidazione.setForeground(new Color(39, 174, 96)); // Verde
                    }
                } else {
                    lblEsitoPrevalidazione.setText("Seleziona una richiesta...");
                    lblEsitoPrevalidazione.setForeground(Color.BLACK);
                }
            }
        });

        ArrayList<String> richieste = Controller.getInstance().getRegistroRichiesteSpostamento();
        if (richieste != null) {
            for (String r : richieste) model.addRow(new Object[]{r});
        }

        JPanel pnlAzioni = new JPanel();
        JButton btnApprova = new JButton("Approva");
        JButton btnRifiuta = new JButton("Rifiuta");

        btnApprova.addActionListener(e -> {
            int riga = table.getSelectedRow();
            if (riga == -1) {
                JOptionPane.showMessageDialog(dialog, "Seleziona una richiesta!");
                return;
            }

            String esito = Controller.getInstance().validaRichiesta(riga);
            if (esito.equals("IDONEA")) {
                Controller.getInstance().approvaRichiesta(riga);
                JOptionPane.showMessageDialog(dialog, "Richiesta approvata!");
                model.removeRow(riga);
            } else {
                JOptionPane.showMessageDialog(dialog, "Errore: " + esito, "Non approvabile", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRifiuta.addActionListener(e -> {
            int riga = table.getSelectedRow();
            if (riga != -1) {
                Controller.getInstance().rifiutaRichiesta(riga);
                model.removeRow(riga);
                JOptionPane.showMessageDialog(dialog, "Richiesta rifiutata.");
            } else {
                JOptionPane.showMessageDialog(dialog, "Seleziona una richiesta!");
            }
        });

        pnlAzioni.add(btnApprova);
        pnlAzioni.add(btnRifiuta);

        // Pannello contenitore per label e bottoni
        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.add(lblEsitoPrevalidazione, BorderLayout.NORTH);
        pnlSouth.add(pnlAzioni, BorderLayout.SOUTH);

        dialog.add(new JScrollPane(table), BorderLayout.CENTER);
        dialog.add(pnlSouth, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}