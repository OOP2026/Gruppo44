package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Variazione {
    LocalDate dataOriginale;
    LocalDate nuovaData;
    LocalTime oraInizioOriginale;
    LocalTime nuovaOraInizio;
    LocalTime nuovaOraFine;

    Variazione(LocalDate dataOriginale, LocalDate nuovaData, LocalTime nuovaOraInizio, LocalTime nuovaOraFine, LocalTime oraInizioOriginale)  {
        this.dataOriginale = dataOriginale;
        this.nuovaData = nuovaData;
        this.nuovaOraInizio = nuovaOraInizio;
        this.nuovaOraFine = nuovaOraFine;
        this.oraInizioOriginale = oraInizioOriginale;
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
}
