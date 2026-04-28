package model;

public class Responsabile extends Docente{
    //eredita da Docente senza nuovi attributi
    Responsabile(String nome, String cognome, String email, String login, String password) {
        super(nome, cognome, email, login, password); //chiama il costruttore di Docente senza variazioni di paramentri, perché Responsabile non ne ha di nuovi rispetto a Docente
    }
}
