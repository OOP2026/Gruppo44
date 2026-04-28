package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Variazione {
    LocalDate dataOriginale;
    LocalTime nuovaData;
    LocalTime nuovaOraInizio;
    LocalTime nuovaOraFine;

    Variazione(LocalDate dataOriginale, LocalTime nuovaData, LocalTime nuovaOraInizio, LocalTime nuovaOraFine)  {
        this.dataOriginale = dataOriginale;
        this.nuovaData = nuovaData;
        this.nuovaOraFine = nuovaOraInizio;
        this.nuovaOraFine = nuovaOraFine;
    }
}
