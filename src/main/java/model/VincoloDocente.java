package model;

import java.time.LocalTime;

public class VincoloDocente {
    public Docente docente;
    public GiornoSettimana giorno;
    public LocalTime oraInizio;
    public LocalTime oraFine;

public VincoloDocente(Docente d, GiornoSettimana g, LocalTime inizio, LocalTime fine) {
    this.docente = d;
    this.giorno = g;
    this.oraInizio = inizio;
    this.oraFine = fine;
}
}
