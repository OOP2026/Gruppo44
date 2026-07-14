package gui;

import javax.swing.*;
import java.awt.*;

// uso una classe per definire UNA SOLA VOLTA la sezione di login uguale per Studente e Docente
// i campi sono solo email e password

public class LoginPanel extends JPanel {

    private final JTextField campoEmail;
    private final JPasswordField campoPassword;
    private final JButton pulsanteAccedi;

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

    public String getEmail() {
        return campoEmail.getText().trim();
    }

    public String getPassword() {
        return new String(campoPassword.getPassword());
    }

    public JButton getPulsanteAccedi() {
        return pulsanteAccedi;
    }
}