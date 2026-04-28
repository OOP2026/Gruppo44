package model;

import java.time.LocalTime;

public class Lezione {
    GiornoSettimana giornoSettimana;
    LocalTime oraInizio;
    LocalTime oraFine;

    Lezione(GiornoSettimana g, LocalTime oraInizio, LocalTime oraFine){
        this.giornoSettimana = g;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
    }
}
