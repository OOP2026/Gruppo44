package model;

public class Docente extends Utente{
    Docente(String nome, String cognome, String email, String login, String password) {
        super(nome, cognome, email, login, password); //chiama il costruttore di Utente
    }
}
