package gui;

import javax.swing.*;
import java.awt.*;

// Panel centrale
// cambiaSchermata() è il metodo che pulisce e ridisegna il panel, chiamando le altre Dashboard
public class MainPanel extends JPanel {

    public MainPanel() {
        setLayout(new BorderLayout());
        mostraHome();
    }

    public void mostraHome() {
        cambiaSchermata(new DashboardHome(this));
    }

    public void mostraLoginStudente() {
        cambiaSchermata(new DashboardStudente(this, false));
    }

    public void mostraRegistrazioneStudente() {
        cambiaSchermata(new DashboardStudente(this, true));
    }

    public void mostraLoginDocente() {
        cambiaSchermata(new DashboardDocente(this, false));
    }

    public void mostraRegistrazioneDocente() {
        cambiaSchermata(new DashboardDocente(this, true));
    }

    // Rimuove la schermata corrente e la sostituisce con quella passata come parametro
    public void cambiaSchermata(JPanel nuovaSchermata) {
        this.removeAll();
        this.add(nuovaSchermata, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }
}