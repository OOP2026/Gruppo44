package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class LoginPanel extends JPanel {

    private final JTextField campoEmail;
    private final JPasswordField campoPassword;
    private final JButton pulsanteAccedi;



    /**
     * Inizializza il pannello di login personalizzato con il titolo e il colore
     * tematico specificati, riutilizzabile sia per il login dello Studente che del Docente
     * (dato che per entrambi i campi da inserire sono solo Email e Password).
     * <p>
     * Il pannello utilizza un {@link GridBagLayout} per disporre verticalmente i
     * componenti necessari all'autenticazione:
     * <ul>
     * <li>Etichetta e campo di ingresso per l'<b>Email</b>.</li>
     * <li>Etichetta e campo {@link JPasswordField} per la <b>Password</b>.</li>
     * <li>Un pulsante di azione "ACCEDI" personalizzato con il colore fornito.</li>
     * </ul>
     * <p>
     * Il layout è ottimizzato tramite {@link GridBagConstraints} per garantire
     * una disposizione coerente e spaziature standard ({@code Stile.INSET_STANDARD}).
     *
     * @param titolo Il testo da visualizzare nel bordo del pannello (es. "ACCESSO STUDENTE").
     * @param colore Il colore principale utilizzato per il bordo e il pulsante di accesso.
     */
    public LoginPanel(String titolo, Color colore) {
        setBackground(Color.WHITE);
        setBorder(Stile.creaBordoTitolato(titolo, colore, Stile.BLU_SCURO));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = Stile.INSET_STANDARD;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel etichettaEmail = new JLabel("Email:");
        etichettaEmail.setFont(Stile.FONT_ETICHETTA);
        campoEmail = new JTextField(18);

        JLabel etichettaPassword = new JLabel("Password:");
        etichettaPassword.setFont(Stile.FONT_ETICHETTA);
        campoPassword = new JPasswordField(18);

        pulsanteAccedi = Stile.creaPulsante("ACCEDI", colore);

        gbc.gridy = 0; add(etichettaEmail, gbc);
        gbc.gridy = 1; add(campoEmail, gbc);
        gbc.gridy = 2; add(etichettaPassword, gbc);
        gbc.gridy = 3; add(campoPassword, gbc);
        gbc.gridy = 4; add(pulsanteAccedi, gbc);
    }


    /**
     * Recupera il valore corrente inserito nel campo email, eliminando eventuali
     * spazi bianchi superflui all'inizio e alla fine della stringa.
     *
     * @return La stringa contenente l'indirizzo email inserito dall'utente.
     */
    public String getEmail() {
        return campoEmail.getText().trim();
    }


    /**
     * Recupera la password inserita nel componente {@link JPasswordField}.
     * <p>
     * Poiché il metodo {@code getPassword()} di Swing restituisce un array di caratteri
     * ({@code char[]}) per ragioni di sicurezza, questo metodo esegue la conversione
     * in una {@link String} per facilitarne l'utilizzo nelle operazioni di confronto
     * o invio al {@link controller.Controller}.
     *
     * @return La password inserita sotto forma di {@link String}.
     */
    public String getPassword() {
        return new String(campoPassword.getPassword());
    }


    /**
     * Fornisce accesso al pulsante di autenticazione del pannello.
     * <p>
     * Questo metodo permette alle classi esterne (il {@link MainPanel}
     * o un {@code Controller}) di registrare un {@link ActionListener} sul pulsante,
     * facilitando la gestione degli eventi di click per l'avvio della procedura di login.
     *
     * @return Il componente {@link JButton} utilizzato per eseguire il login.
     */
    public JButton getPulsanteAccedi() {
        return pulsanteAccedi;
    }
}