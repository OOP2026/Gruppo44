package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Richiesta {
    enum StatoRichiesta {
        IN_ATTESA,
        APPROVATA,
        RIFIUTATA
    }

    LocalDate giornoRichiesto;
    LocalTime oraInizioRichiesta;
    LocalTime oraFineRichiesta;
    StatoRichiesta statoRichiesta;

    Richiesta(LocalDate giornoRichiesto, LocalTime oraInizioRichiesta, LocalTime oraFineRichiesta){
        this.giornoRichiesto = giornoRichiesto;
        this.oraInizioRichiesta = oraInizioRichiesta;
        this.oraFineRichiesta = oraFineRichiesta;
        this.statoRichiesta = StatoRichiesta.IN_ATTESA;
        //il costruttore non accetta parametri per statoRichiesta e la imposta sempre a IN_ATTESA perché una richiesta sarà sempre in attesa appena creata
    }
}
