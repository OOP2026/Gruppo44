package model;

public class Docente extends Utente{
    public Docente(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password); //chiama il costruttore di Utente
    }
}
