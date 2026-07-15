package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DashboardUtils {

    private DashboardUtils() {
        /* This utility class should not be instantiated */
    }


    static JPanel creaBarraSuperiore(String[] dati, MainPanel mainPanel) {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(Stile.AZZURRO);
        barra.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblBenvenuto = new JLabel("Benvenuto, " + dati[0] + " " + dati[1]);
        lblBenvenuto.setFont(Stile.FONT_TITOLO_MEDIO);
        lblBenvenuto.setForeground(Stile.BLU_SCURO);

        JButton pulsanteLogout = Stile.creaPulsanteTestoBianco("LOGOUT", Stile.ARANCIONE1);
        pulsanteLogout.addActionListener(e -> mainPanel.mostraHome());

        barra.add(lblBenvenuto, BorderLayout.WEST);
        barra.add(pulsanteLogout, BorderLayout.EAST);
        return barra;
    }

    static JPanel creaPannelloGenerico(){
        JPanel pannello = new JPanel(new BorderLayout(0, 15));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        return pannello;
    }

    static JPanel creaLista(){
        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(Color.WHITE);
        return lista;
    }


    static void aggiornaLezioni(JPanel pannello, String[] giorni, ArrayList<String>[] lezioni) {
        for (int i = 0; i < giorni.length; i++) {
            JLabel titoloGiorno = new JLabel(giorni[i]);
            titoloGiorno.setFont(Stile.FONT_ETICHETTA);
            pannello.add(titoloGiorno);

            ArrayList<String> lezioniGiorno = lezioni[i];
            if (lezioniGiorno == null || lezioniGiorno.isEmpty()) {
                pannello.add(new JLabel("Nessuna lezione"));
            } else {
                for (String lezione : lezioniGiorno) {
                    JLabel riga = new JLabel(lezione);
                    riga.setFont(Stile.FONT_TESTO);
                    pannello.add(riga);
                }
            }
            pannello.add(Box.createVerticalStrut(10));
        }
    }

    static int aggiungiCampo(JPanel form, GridBagConstraints gbc, int riga, String etichetta, JComponent campo) {
        JLabel label = new JLabel(etichetta);
        label.setFont(Stile.FONT_ETICHETTA);
        gbc.gridy = riga++;
        form.add(label, gbc);
        gbc.gridy = riga++;
        form.add(campo, gbc);
        return riga;
    }

}
