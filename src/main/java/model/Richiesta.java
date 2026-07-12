package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Richiesta {

    public String insegnamento;
    public LocalDate giornoOriginale;
    public LocalDate giornoRichiesto;
    public LocalTime oraInizioRichiesta;
    public LocalTime oraInizioOriginale;
    public LocalTime oraFineRichiesta;
    public String aula;


    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public Richiesta(String insegnamento, LocalDate giornoOriginale, LocalDate giornoRichiesto, LocalTime oraInizioOriginale, LocalTime oraInizioRichiesta, LocalTime oraFineRichiesta, String aula){
        this.insegnamento = insegnamento;
        this.giornoOriginale = giornoOriginale;
        this.giornoRichiesto = giornoRichiesto;
        this.oraInizioOriginale = oraInizioOriginale;
        this.oraInizioRichiesta = oraInizioRichiesta;
        this.oraFineRichiesta = oraFineRichiesta;
        this.aula = aula;
    }

    public String getInsegnamento() {
        return insegnamento;
    }

    public void setInsegnamento(String insegnamento) {
        this.insegnamento = insegnamento;
    }

    public LocalDate getGiornoOriginale() {
        return giornoOriginale;
    }

    public void setGiornoOriginale(LocalDate giornoOriginale) {
        this.giornoOriginale = giornoOriginale;
    }

    public LocalDate getGiornoRichiesto() {
        return giornoRichiesto;
    }

    public void setGiornoRichiesto(LocalDate giornoRichiesto) {
        this.giornoRichiesto = giornoRichiesto;
    }

    public LocalTime getOraInizioRichiesta() {
        return oraInizioRichiesta;
    }

    public void setOraInizioRichiesta(LocalTime oraInizioRichiesta) {
        this.oraInizioRichiesta = oraInizioRichiesta;
    }

    public LocalTime getOraInizioOriginale() {
        return oraInizioOriginale;
    }

    public void setOraInizioOriginale(LocalTime oraInizioOriginale) {
        this.oraInizioOriginale = oraInizioOriginale;
    }

    public LocalTime getOraFineRichiesta() {
        return oraFineRichiesta;
    }

    public void setOraFineRichiesta(LocalTime oraFineRichiesta) {
        this.oraFineRichiesta = oraFineRichiesta;
    }
}
