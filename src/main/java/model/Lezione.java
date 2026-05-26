package model;

import java.time.LocalTime;

public class Lezione {
    GiornoSettimana giornoSettimana;
    LocalTime oraInizio;
    LocalTime oraFine;
    String aula;
    Insegnamento insegnamento;

    public Lezione(GiornoSettimana g, LocalTime oraInizio, LocalTime oraFine, String aula, Insegnamento insegnamento) {
        this.giornoSettimana = g;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.aula = aula;
        this.insegnamento = insegnamento;
    }

    //testing
    public String ToString(){
        return "Giorno: " + giornoSettimana + ", oraInizio: " + oraInizio + ", oraFine: " + oraFine + ", aula: " + aula + ", insegnamento: " + insegnamento.nome;
    }

}
