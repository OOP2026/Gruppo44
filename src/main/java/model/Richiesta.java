package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Richiesta {

    public Insegnamento insegnamento;
    public LocalDate giornoOriginale;
    public LocalDate giornoRichiesto;
    public LocalTime oraInizioRichiesta;
    public LocalTime oraInizioOriginale;
    public LocalTime oraFineRichiesta;
    public String statoRichiesta;

    Richiesta(Insegnamento insegnamneto, LocalDate giornoOriginale, LocalDate giornoRichiesto, LocalTime oraInizioOriginale, LocalTime oraInizioRichiesta, LocalTime oraFineRichiesta){
        this.insegnamento = insegnamneto;
        this.giornoOriginale = giornoOriginale;
        this.giornoRichiesto = giornoRichiesto;
        this.oraInizioOriginale = oraInizioOriginale;
        this.oraInizioRichiesta = oraInizioRichiesta;
        this.oraFineRichiesta = oraFineRichiesta;
        this.statoRichiesta = "In attesa";
        //il costruttore non accetta parametri per statoRichiesta e la imposta sempre a IN_ATTESA perché una richiesta sarà sempre in attesa appena creata
    }

    public Insegnamento getInsegnamento() {
        return insegnamento;
    }

    public void setInsegnamento(Insegnamento insegnamento) {
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

    public String getStatoRichiesta() {
        return statoRichiesta;
    }

    public void setStatoRichiesta(String statoRichiesta) {
        this.statoRichiesta = statoRichiesta;
    }
}
