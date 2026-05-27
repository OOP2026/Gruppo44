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
}
