package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DashboardUtils {


    /**
     * Costruttore privato per impedire l'istanziazione di questa classe di utilità.
     * <p>
     * Poiché {@code DashboardUtils} contiene esclusivamente metodi statici,
     * l'istanziazione non è necessaria né desiderata. Questo costruttore
     * impedisce la creazione accidentale di oggetti di questa classe.
     */
    private DashboardUtils() {

    }

    /**
     * Crea una barra di navigazione superiore personalizzata per l'area
     * riservata dell'utente.
     * <p>
     * La barra fornisce un feedback visivo immediato sull'identità dell'utente
     * (tramite il nome e cognome estratti dall'array {@code dati}) e offre un
     * controllo rapido per terminare la sessione.
     * <p>
     * Componenti della barra:
     * <ul>
     * <li><b>Etichetta di benvenuto:</b> Visualizzata a sinistra ({@code WEST}),
     * utilizza uno stile grafico di tipo titolo medio.</li>
     * <li><b>Pulsante di Logout:</b> Visualizzato a destra ({@code EAST}),
     * attiva il metodo {@link MainPanel#mostraHome()} per riportare l'applicazione
     * allo stato iniziale.</li>
     * </ul>
     *
     * @param dati Array contenente i dati dell'utente, dove {@code dati[0]} è il
     * nome e {@code dati[1]} è il cognome.
     * @param mainPanel Riferimento al pannello principale dell'applicazione, utilizzato
     * per gestire la navigazione post-logout.
     * @return Un {@link JPanel} configurato come barra superiore.
     */
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



    /**
     * Crea e restituisce un pannello base configurato con uno stile standardizzato
     * utilizzato in tutta l'applicazione.
     * <p>
     * Il metodo centralizza la configurazione estetica e di layout per i pannelli
     * secondari, garantendo uniformità grafica:
     * <ul>
     * <li><b>Layout:</b> {@link BorderLayout} con uno spazio verticale tra i componenti di 15 pixel.</li>
     * <li><b>Sfondo:</b> Bianco.</li>
     * <li><b>Bordo:</b> Bordo vuoto di 20 pixel su ogni lato.</li>
     * </ul>
     *
     * @return Un {@link JPanel} preconfigurato e pronto per essere popolato con ulteriori componenti.
     */
    static JPanel creaPannelloGenerico(){
        JPanel pannello = new JPanel(new BorderLayout(0, 15));
        pannello.setBackground(Color.WHITE);
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        return pannello;
    }



    /**
     * Crea e restituisce un pannello configurato come contenitore per liste di elementi.
     * <p>
     * Il metodo centralizza la configurazione per i contenitori che devono visualizzare
     * elementi disposti verticalmente, garantendo coerenza estetica:
     * <ul>
     * <li><b>Layout:</b> {@link BoxLayout} orientato verticalmente ({@code Y_AXIS}).</li>
     * <li><b>Sfondo:</b> Bianco, per mantenere la pulizia visiva.</li>
     * </ul>
     *
     * @return Un {@link JPanel} pronto per ospitare componenti in sequenza verticale.
     */
    static JPanel creaLista(){
        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(Color.WHITE);
        return lista;
    }




    /**
     * Popola il pannello specificato con l'orario settimanale delle lezioni, organizzato per giorni.
     * <p>
     * Il metodo itera attraverso l'array dei giorni e, per ciascuno, visualizza
     * il relativo titolo e l'elenco delle lezioni associate, recuperate tramite
     * l'array di {@link ArrayList}.
     * <p>
     * Logica di rendering:
     * <ul>
     * <li><b>Titolo:</b> Aggiunge una label formattata con {@code Stile.FONT_ETICHETTA} per ogni giorno.</li>
     * <li><b>Dettaglio lezioni:</b> Se la lista delle lezioni è vuota o nulla, visualizza
     * un messaggio "Nessuna lezione". Altrimenti, aggiunge ogni lezione come {@link JLabel}
     * formattata con {@code Stile.FONT_TESTO}.</li>
     * <li><b>Spaziatura:</b> Aggiunge un separatore verticale di 10 pixel ({@link Box#createVerticalStrut(int)})
     * dopo ogni blocco giornaliero per migliorare la leggibilità.</li>
     * </ul>
     *
     * @param pannello Il {@link JPanel} (tipicamente con {@code BoxLayout}) in cui aggiungere gli elementi.
     * @param giorni Un array di {@link String} contenente i nomi dei giorni.
     * @param lezioni Un array di {@link ArrayList} dove ogni indice corrisponde al giorno
     * nella stessa posizione dell'array {@code giorni}.
     */
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



    /**
     * Aggiunge una coppia etichetta-campo al pannello di layout specificato
     * utilizzando le impostazioni di {@link GridBagConstraints}.
     * <p>
     * Questo metodo standardizza la creazione di form, posizionando l'etichetta
     * su una riga e il campo di ingresso subito sotto, incrementando
     * automaticamente l'indice di riga per i componenti successivi.
     *
     * @param form Il {@link JPanel} di destinazione dove aggiungere gli elementi.
     * @param gbc L'oggetto {@link GridBagConstraints} che definisce le regole di layout.
     * @param riga L'indice della riga corrente in cui iniziare l'inserimento.
     * @param etichetta Il testo dell'etichetta da visualizzare sopra il campo.
     * @param campo Il componente {@link JComponent} da aggiungere.
     * @return L'indice della riga successiva libera ({@code riga + 2}).
     */
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
