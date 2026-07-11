package model;

import java.time.LocalTime;

public class Lezione {
    public GiornoSettimana giornoSettimana;
    LocalTime oraInizio;
    LocalTime oraFine;
    String aula;
    String insegnamento;

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

    public GiornoSettimana getGiornoSettimana() {
        return giornoSettimana;
    }

    public void setGiornoSettimana(GiornoSettimana giornoSettimana) {
        this.giornoSettimana = giornoSettimana;
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }

    public LocalTime getOraFine() {
        return oraFine;
    }

    public void setOraFine(LocalTime oraFine) {
        this.oraFine = oraFine;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getInsegnamento() {
        return insegnamento;
    }

    public void setInsegnamento(String insegnamento) {
        this.insegnamento = insegnamento;
    }
}
