package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//estende docente per averne le funzionalità grafiche ma nella sua dashboard
//ha solo le funzionalità da responsabile e non mostra nessuna delle voci del Docente
public class DashboardResponsabile extends DashboardDocente {

    private static final String[] GIORNI = {"LUNEDI", "MARTEDI", "MERCOLEDI", "GIOVEDI", "VENERDI"};

    public DashboardResponsabile(MainPanel mainPanel, String email) {
        super(mainPanel); // il Responsabile entra già autenticato
        this.dati = new String[]{"Responsabile", "", email};
        mostraAreaPersonale();
    }

    @Override
    protected void mostraAreaPersonale() {
        removeAll();
        String[] nomeScelta = {"Gestisci aule", "Elenco insegnamenti", "Gestisci richieste"};
        JPanel[] pannelli = {pannelloGestisciAule(), pannelloElencoInsegnamenti(), pannelloGestisciRichieste()};

        add(new PannelloRiutilizzabileMenu(nomeScelta, pannelli), BorderLayout.CENTER);
        add(creaBarraSuperiore(), BorderLayout.NORTH);
        revalidate();
        repaint();
    }

    // GESTIONE DELLE AULE
    // getAule() restituisce solo il nome e non la capienza, anche se creaAula
    // la richiede in fase di creazione, qui non viene mostrata

    private JPanel pannelloGestisciAule() {
        JPanel pannello = new JPanel(new BorderLayout(0, 15));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(Color.WHITE);
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

    private void aggiornaListaAule(JPanel lista) {
        lista.removeAll();
        try {
            List<String> aule = Controller.getInstance().getAule();
            if (aule.isEmpty()) lista.add(new JLabel("Nessuna aula inserita."));
            for (String nomeAula : aule) lista.add(rigaAula(nomeAula, lista));
        } catch (Exception errore) {
            lista.add(new JLabel("Errore nel recupero delle aule: " + errore.getMessage()));
        }
        lista.revalidate();
        lista.repaint();
    }

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

    // GESTIONE INSEGNAMENTI

    // aggiornaInsegnamento() fa sia l'attivazione (modificando anno e CFU) sia il cambio di docente

    private JPanel pannelloElencoInsegnamenti() {
        JPanel pannello = new JPanel();
        pannello.setLayout(new BoxLayout(pannello, BoxLayout.Y_AXIS));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        aggiornaElencoInsegnamenti(pannello);
        return pannello;
    }

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
            pannello.add(new JLabel("Errore nel recupero degli insegnamenti: " + errore.getMessage()));
        }
        pannello.revalidate();
        pannello.repaint();
    }

    //gestisco gli insegnamenti da getInsegnamenti()
    //split(",") spezza la stringa in un array ed usa la virgola come separatore
    //tipo: ["Basi di Dati"," anno 2"," 9 CFU"]
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

    // CREZIONE DELLE LEZIONI
    // controllo automatico sui vincoli del docente e sull'aula occupata
    // L'aula si scrive a mano

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
                LocalTime oraInizio = LocalTime.parse(campoOraInizio.getText().trim());
                LocalTime oraFine = LocalTime.parse(campoOraFine.getText().trim());

                for (String vincolo : Controller.getInstance().getVincoliDocente(campoEmailDocente.getText().trim())) {
                    if (vincoloInConflitto(vincolo, giorno, oraInizio, oraFine)) {
                        JOptionPane.showMessageDialog(finestra, "Orario in conflitto con un vincolo del docente:\n" + vincolo, "Lezione non creata", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                String aula = campoAula.getText().trim();
                if (aulaOccupata(giorno, aula, oraInizio, oraFine)) {
                    JOptionPane.showMessageDialog(finestra, "L'aula " + aula + " è già occupata in quel giorno/orario.", "Lezione non creata", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Controller.getInstance().creaLezione(nomeInsegnamento, giorno, campoOraInizio.getText().trim(), campoOraFine.getText().trim(), aula);
                JOptionPane.showMessageDialog(finestra, "Lezione creata.");
                finestra.dispose();
            } catch (Exception errore) {
                JOptionPane.showMessageDialog(finestra, errore.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridy = riga;
        form.add(pulsanteCrea, gbc);

        finestra.setContentPane(form);
        finestra.setVisible(true);
    }

    // Controlla se un vincolo (stringa "Giorno: LUNEDI, ore: 10:00-12:00") va in conflitto
    // con l'orario di una nuova lezione nello stesso giorno.
    private boolean vincoloInConflitto(String vincolo, String giorno, LocalTime nuovoInizio, LocalTime nuovoFine) {
        try {
            String[] parti = vincolo.replace("Giorno: ", "").replace("ore: ", "").split(", ");
            if (!parti[0].trim().equalsIgnoreCase(giorno)) return false;
            String[] orari = parti[1].split("-");
            LocalTime vincoloInizio = LocalTime.parse(orari[0].trim().substring(0, 5));
            LocalTime vincoloFine = LocalTime.parse(orari[1].trim().substring(0, 5));
            return nuovoInizio.isBefore(vincoloFine) && vincoloInizio.isBefore(nuovoFine);
        } catch (Exception errore) {
            return false; // formato imprevisto: non blocco per un vincolo che non riesco a leggere
        }
    }

    // Controlla se una data aula ha già una lezione sovrapposta in quel giorno/orario,
    // guardando TUTTE le lezioni del sistema (getLezioni() senza parametri), non solo
    // quelle di un anno o di un docente specifico.
    private boolean aulaOccupata(String giorno, String aula, LocalTime nuovoInizio, LocalTime nuovoFine) throws Exception {
        ArrayList<String>[] tutteLeLezioni = Controller.getInstance().getLezioni();
        int indiceGiorno = Arrays.asList(GIORNI).indexOf(giorno);
        ArrayList<String> lezioniGiorno = tutteLeLezioni[indiceGiorno];
        if (lezioniGiorno == null) return false;

        for (String lezione : lezioniGiorno) {
            String[] campi = lezione.split("\n"); // oraInizio, oraFine, insegnamento, aula
            if (!campi[3].equals(aula)) continue;

            LocalTime inizioEsistente = LocalTime.parse(campi[0].substring(0, 5));
            LocalTime fineEsistente = LocalTime.parse(campi[1].substring(0, 5));
            if (nuovoInizio.isBefore(fineEsistente) && inizioEsistente.isBefore(nuovoFine)) {
                return true;
            }
        }
        return false;
    }

    // GESTIONE RICHIESTE SPOSTAMENTO
    // l'indice nella lista viene usato come identificativo della richiesta, perché
    // getRegistroRichiesteSpostamento() non restituisce l'id insieme al testo

    private JPanel pannelloGestisciRichieste() {
        JPanel pannello = new JPanel();
        pannello.setLayout(new BoxLayout(pannello, BoxLayout.Y_AXIS));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        aggiornaListaRichieste(pannello);
        return pannello;
    }

    private void aggiornaListaRichieste(JPanel pannello) {
        pannello.removeAll();
        try {
            List<String> richieste = Controller.getInstance().getRegistroRichiesteSpostamento();
            if (richieste.isEmpty()) pannello.add(new JLabel("Nessuna richiesta in attesa."));
            for (int i = 0; i < richieste.size(); i++) {
                int indice = i;
                pannello.add(rigaRichiesta(richieste.get(i), indice, pannello));
                pannello.add(Box.createVerticalStrut(8));
            }
        } catch (Exception errore) {
            pannello.add(new JLabel("Errore nel recupero delle richieste: " + errore.getMessage()));
        }
        pannello.revalidate();
        pannello.repaint();
    }

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