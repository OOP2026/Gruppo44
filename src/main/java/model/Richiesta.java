package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Richiesta {

    LocalDate giornoRichiesto;
    LocalTime oraInizioRichiesta;
    LocalTime oraFineRichiesta;
    String statoRichiesta;

    Richiesta(LocalDate giornoRichiesto, LocalTime oraInizioRichiesta, LocalTime oraFineRichiesta){
        this.giornoRichiesto = giornoRichiesto;
        this.oraInizioRichiesta = oraInizioRichiesta;
        this.oraFineRichiesta = oraFineRichiesta;
        this.statoRichiesta = "In attesa";
        //il costruttore non accetta parametri per statoRichiesta e la imposta sempre a IN_ATTESA perché una richiesta sarà sempre in attesa appena creata
    }
}
