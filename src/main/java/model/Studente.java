package model;

public class Studente extends Utente {
    //eredita da Utente, con l'aggiunta di una matricola
    private String matricola;
    private int annoAccademico;

    public Studente(String nome, String cognome, String email, String password, String matricola, int annoAccademico) {
        super(nome, cognome, email, password); //chiama il costruttore di Utente
        this.matricola = matricola; //aggiunge la matricola
        this.annoAccademico = annoAccademico;
    }


    /**
     * Recupera l'anno accademico di iscrizione dello studente.
     * @return L'anno accademico.
     */
    public int getAnnoAccademico() {
        return annoAccademico;
    }


    /**
     * Imposta l'anno accademico dello studente.
     * @param annoAccademico L'anno da impostare.
     */
    public void setAnnoAccademico(int annoAccademico) {
        this.annoAccademico = annoAccademico;
    }


    /**
     * Recupera il numero di matricola dello studente.
     * @return La matricola come stringa.
     */
    public String getMatricola() {
        return matricola;
    }


    /**
     * Imposta il numero di matricola dello studente.
     * @param matricola La matricola da assegnare.
     */
    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }
}
