package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Variazione {
    private LocalDate dataOriginale;
    private LocalDate nuovaData;
    private LocalTime oraInizioOriginale;
    private LocalTime nuovaOraInizio;
    private LocalTime nuovaOraFine;

    String insegnamento;

    String aula;

    public Variazione(LocalDate dataOriginale, LocalDate nuovaData, LocalTime nuovaOraInizio, LocalTime nuovaOraFine, LocalTime oraInizioOriginale, String insegnamento, String aula)  {
        this.dataOriginale = dataOriginale;
        this.nuovaData = nuovaData;
        this.nuovaOraInizio = nuovaOraInizio;
        this.nuovaOraFine = nuovaOraFine;
        this.oraInizioOriginale = oraInizioOriginale;
        this.aula = aula;
        this.insegnamento = insegnamento;
    }

    public LocalDate getDataOriginale() {
        return dataOriginale;
    }

    public void setDataOriginale(LocalDate dataOriginale) {
        this.dataOriginale = dataOriginale;
    }

    public LocalDate getNuovaData() {
        return nuovaData;
    }

    public void setNuovaData(LocalDate nuovaData) {
        this.nuovaData = nuovaData;
    }

    public LocalTime getOraInizioOriginale() {
        return oraInizioOriginale;
    }

    public void setOraInizioOriginale(LocalTime oraInizioOriginale) {
        this.oraInizioOriginale = oraInizioOriginale;
    }

    public LocalTime getNuovaOraInizio() {
        return nuovaOraInizio;
    }

    public void setNuovaOraInizio(LocalTime nuovaOraInizio) {
        this.nuovaOraInizio = nuovaOraInizio;
    }

    public LocalTime getNuovaOraFine() {
        return nuovaOraFine;
    }

    public void setNuovaOraFine(LocalTime nuovaOraFine) {
        this.nuovaOraFine = nuovaOraFine;
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
