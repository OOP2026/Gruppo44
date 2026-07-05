package gui; // Appartiene al pacchetto view dedicato alle interfacce

import javax.swing.*; // Importa le librerie Swing per i pannelli
import java.awt.*; // Importa le librerie AWT per la gestione layout


              //
             //SEZIONE 2: IL MAIN PANEL fondamentale per lo switch tra i panel
           //3 metodi mostra (home, dashStud e dashDoc) ed uno cambiaSchermata
          //

public class MainPanel extends JPanel {
    // Estende JPanel per fungere da contenitore intercambiabile

    // Costruttore del pannello contenitore principale standard
    public MainPanel() {
        setLayout(new BorderLayout()); //border cosicché i panel Studente/Docente occupano tutto lo spazio

        // Chiamata al metodo interno per caricare la schermata iniziale di benvenuto
        mostraHome();
    }



    // Metodo specifico per ripulire lo schermo e caricare la DashboardHome iniziale
    public void mostraHome() {
        // Istanzia il pannello della Home passandogli il riferimento a questo MainPanel (this) per i futuri switch
        DashboardHome home = new DashboardHome(this);
        // Sfrutta il metodo generico di switch definito sotto per mostrare la Home
        cambiaSchermata(home); //Pulisce il MainPanel e aggiunge la Home
    }


    // METODO PER NAVIGARE VERSO IL PORTALE STUDENTE (Login + Registrazione integrati)
    public void mostraDashboardStudente() {
        cambiaSchermata(new DashboardStudente(this));
        //cambiaSchermata pulisce il mainPanel e mostra il portale studente
    }


    // METODO PER NAVIGARE VERSO IL PORTALE DOCENTE / RESPONSABILE (Login + Registrazione integrati)
    public void mostraDashboardDocente() {
        cambiaSchermata(new DashboardDocente(this));
        //cambiaSchermata pulisce il mainPanel e mostra il portale docente/responsabile
    }


    // Il metodo centralizzato che effettua la pulizia e il ridisegno dello schermo
    public void cambiaSchermata(JPanel nuovaSchermata) {
        // 1. Rimuove tutti i vecchi componenti (pulsanti, label, input) attualmente presenti nel pannello
        this.removeAll();

        // 2. Inserisce il nuovo pannello grafico passato come parametro nella zona centrale
        this.add(nuovaSchermata, BorderLayout.CENTER);

        // 3. Notifica il Layout Manager di ricalcolare posizioni e dimensioni della nuova struttura
        this.revalidate();

        // 4. Ordina al sottosistema grafico di ridisegnare lo schermo a livello visivo
        this.repaint();
    }
}