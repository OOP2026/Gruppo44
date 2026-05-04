package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Variazione {
    LocalDate dataOriginale;
    LocalDate nuovaData;
    LocalTime nuovaOraInizio;
    LocalTime nuovaOraFine;

    Variazione(LocalDate dataOriginale, LocalDate nuovaData, LocalTime nuovaOraInizio, LocalTime nuovaOraFine)  {
        this.dataOriginale = dataOriginale;
        this.nuovaData = nuovaData;
        this.nuovaOraInizio = nuovaOraInizio;
        this.nuovaOraFine = nuovaOraFine;
    }
}
