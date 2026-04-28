package model;

public class Studente extends Utente {
    //eredita da Utente, con l'aggiunta di una matricola
    protected String matricola;

    Studente(String nome, String cognome, String email, String login, String password, String matricola) {
        super(nome, cognome, email, login, password); //chiama il costruttore di Utente
        this.matricola = matricola; //aggiunge la matricola
    }

}
