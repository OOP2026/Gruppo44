package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Richiesta {

    private String insegnamento;
    private LocalDate giornoOriginale;
    private LocalDate giornoRichiesto;
    private LocalTime oraInizioRichiesta;
    private LocalTime oraInizioOriginale;
    private LocalTime oraFineRichiesta;
    private String aula;

    public int idRichiesta;


    public Richiesta(String insegnamento, LocalDate giornoOriginale, LocalDate giornoRichiesto, LocalTime oraInizioOriginale, LocalTime oraInizioRichiesta, LocalTime oraFineRichiesta, String aula, int idRichiesta){
        this.insegnamento = insegnamento;
        this.giornoOriginale = giornoOriginale;
        this.giornoRichiesto = giornoRichiesto;
        this.oraInizioOriginale = oraInizioOriginale;
        this.oraInizioRichiesta = oraInizioRichiesta;
        this.oraFineRichiesta = oraFineRichiesta;
        this.aula = aula;
        this.idRichiesta = idRichiesta;
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

    public int getIdRichiesta() {
        return idRichiesta;
    }

    public void setIdRichiesta(int idRichiesta) {
        this.idRichiesta = idRichiesta;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }
}
