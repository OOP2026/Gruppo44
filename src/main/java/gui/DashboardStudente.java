package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardStudente extends JPanel {

    private final MainPanel mainPanel;
    private String[] dati; // nome, cognome, email, matricola, anno

    // con registrazione == true mostra il form di iscrizione, false mostra il login
    public DashboardStudente(MainPanel mainPanel, boolean registrazione) {
        this.mainPanel = mainPanel;
        setBackground(Stile.AZZURRO);
        setLayout(new BorderLayout());

        if (registrazione) {
            mostraRegistrazione();
        } else {
            mostraLogin();
        }
    }

    // costruzione del login tramite LoginPanel con testo personalizzato per studente
    private void mostraLogin() {
        removeAll();
        LoginPanel login = new LoginPanel("ACCESSO STUDENTE", Stile.VERDE_CHIARO);

        login.getPulsanteAccedi().addActionListener(e -> {
            try {
                dati = Controller.getInstance().loginStudente(login.getEmail(), login.getPassword());
                mostraAreaPersonale();
            } catch (Exception errore) {
                JOptionPane.showMessageDialog(this, errore.getMessage(), "Accesso non riuscito", JOptionPane.ERROR_MESSAGE);
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
        form.setBorder(Stile.creaBordoTitolato("REGISTRAZIONE STUDENTE", Stile.VERDE_CHIARO, Stile.BLU_SCURO));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = Stile.INSET_STANDARD;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JTextField campoNome = new JTextField(18);
        JTextField campoCognome = new JTextField(18);
        JTextField campoEmail = new JTextField(18);
        JPasswordField campoPassword = new JPasswordField(18);
        JTextField campoMatricola = new JTextField(18);
        JComboBox<Integer> campoAnno = new JComboBox<>(new Integer[]{1, 2, 3});

        int riga = 0;
        riga = aggiungiCampo(form, gbc, riga, "Nome:", campoNome);
        riga = aggiungiCampo(form, gbc, riga, "Cognome:", campoCognome);
        riga = aggiungiCampo(form, gbc, riga, "Email:", campoEmail);
        riga = aggiungiCampo(form, gbc, riga, "Password:", campoPassword);
        riga = aggiungiCampo(form, gbc, riga, "Matricola:", campoMatricola);
        riga = aggiungiCampo(form, gbc, riga, "Anno accademico:", campoAnno);

        JButton pulsanteRegistrati = Stile.creaPulsante("REGISTRATI", Stile.VERDE_CHIARO);
        pulsanteRegistrati.addActionListener(e -> {
            try {
                dati = Controller.getInstance().creaStudente(
                        campoNome.getText().trim(), campoCognome.getText().trim(),
                        campoEmail.getText().trim(), new String(campoPassword.getPassword()),
                        campoMatricola.getText().trim(), (Integer) campoAnno.getSelectedItem());
                mostraAreaPersonale();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Registrazione non riuscita", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridy = riga;
        form.add(pulsanteRegistrati, gbc);

        add(centra(form), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // aggiunge una riga label + campo al form e restituisce la riga successiva (riga ++) libera
    private int aggiungiCampo(JPanel form, GridBagConstraints gbc, int riga, String etichetta, JComponent campo) {
        return DashboardUtils.aggiungiCampo(form, gbc, riga, etichetta, campo);
    }

    // centra il pannello di login/registrazione nella schermata invece di farlo occupare tutto lo spazio
    private JPanel centra(JPanel contenuto) {
        JPanel esterno = new JPanel(new GridBagLayout());
        esterno.setBackground(Stile.AZZURRO);
        esterno.add(contenuto);
        return esterno;
    }

    // schermata personale dopo il login con anagrafica, orario e variazioni per lo studente
    //creati separatamente sotto
    private void mostraAreaPersonale() {
        removeAll();

        String[] nomeScelta = {"Dati anagrafici", "Il mio orario", "Variazioni"};
        JPanel[] pannelli = {
                pannelloDatiAnagrafici(),
                pannelloOrario(),
                pannelloVariazioni()
        };

        add(new PannelloRiutilizzabileMenu(nomeScelta, pannelli), BorderLayout.CENTER);
        add(DashboardUtils.creaBarraSuperiore(dati, mainPanel), BorderLayout.NORTH);
        revalidate();
        repaint();
    }

    // dati anagrafici dello studente presi dall'array restituito dal Controller
    private JPanel pannelloDatiAnagrafici() {
        JPanel pannello = new JPanel(new GridLayout(5, 1, 0, 10));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        pannello.add(rigaDato("Nome:", dati[0]));
        pannello.add(rigaDato("Cognome:", dati[1]));
        pannello.add(rigaDato("Email:", dati[2]));
        pannello.add(rigaDato("Matricola:", dati[3]));
        pannello.add(rigaDato("Anno accademico:", dati[4]));
        return pannello;
    }

    private JLabel rigaDato(String etichetta, String valore) {
        JLabel label = new JLabel(etichetta + " " + valore);
        label.setFont(Stile.FONT_TESTO);
        return label;
    }

    // Orario settimanale dello studente, in base al suo anno accademico (dati[4]).
    private JPanel pannelloOrario() {
        JPanel pannello = new JPanel();
        pannello.setLayout(new BoxLayout(pannello, BoxLayout.Y_AXIS));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] giorni = {"LUNEDI", "MARTEDI", "MERCOLEDI", "GIOVEDI", "VENERDI"};

        try {
            ArrayList<String>[] lezioni = Controller.getInstance().getLezioniStudente();
            DashboardUtils.aggiornaLezioni(pannello, giorni, lezioni);
        } catch (Exception e) {
            pannello.add(new JLabel("Errore nel recupero dell'orario: " + e.getMessage()));
        }
        return pannello;
    }

    // variazioni delle lezioni dello studente definite come stringhe
    private JPanel pannelloVariazioni() {
        JPanel pannello = new JPanel();
        pannello.setLayout(new BoxLayout(pannello, BoxLayout.Y_AXIS));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {
            List<String> variazioni = Controller.getInstance().getVariazioniStudente();
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