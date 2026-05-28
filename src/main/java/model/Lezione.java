package model;

import java.time.LocalTime;

public class Lezione {
    public GiornoSettimana giornoSettimana;
    public LocalTime oraInizio;
    public LocalTime oraFine;
    public String aula;
    public String insegnamento;

    public Lezione(GiornoSettimana g, LocalTime oraInizio, LocalTime oraFine, String aula, String insegnamento) {
        this.giornoSettimana = g;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.aula = aula;
        this.insegnamento = insegnamento;
    }

    //testing
    @Override
    public String toString(){
        return "Giorno: " + giornoSettimana + ", oraInizio: " + oraInizio + ", oraFine: " + oraFine + ", aula: " + aula + ", insegnamento: " + insegnamento;
    }

    public String toElementString(){
        return oraInizio+"\n" + oraFine + "\n" + insegnamento + "\n" + aula;

    }

}
