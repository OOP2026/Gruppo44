package model;

public class Studente extends Utente {
    //eredita da Utente, con l'aggiunta di una matricola
    protected String matricola;
    public int annoAccademico;

    public Studente(String nome, String cognome, String email, String password, String matricola, int annoAccademico) {
        super(nome, cognome, email, password); //chiama il costruttore di Utente
        this.matricola = matricola; //aggiunge la matricola
        this.annoAccademico = annoAccademico;
    }

    public int getAnnoAccademico() {
        return annoAccademico;
    }

    public void setAnnoAccademico(int annoAccademico) {
        this.annoAccademico = annoAccademico;
    }

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }
}
