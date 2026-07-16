package model;

import common.GiornoSettimana;

import java.time.LocalTime;

public class VincoloDocente {
    private String docente;
    private GiornoSettimana giorno;
    private LocalTime oraInizio;
    private LocalTime oraFine;

    public VincoloDocente(String docente, GiornoSettimana giorno, LocalTime oraInizio, LocalTime oraFine) {
        this.docente = docente;
        this.giorno = giorno;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
    }


    /**
     * Recupera il nome del docente associato al vincolo.
     * @return Il nome del docente.
     */
    public String getDocente() {
        return docente;
    }


    /**
     * Imposta il nome del docente per il vincolo.
     * @param docente Il nome da assegnare.
     */
    public void setDocente(String docente) {
        this.docente = docente;
    }


    /**
     * Recupera il giorno della settimana del vincolo.
     * @return Il giorno della settimana.
     */
    public GiornoSettimana getGiorno() {
        return giorno;
    }

    /**
     * Imposta il giorno della settimana per il vincolo.
     * @param giorno Il giorno da assegnare.
     */
    public void setGiorno(GiornoSettimana giorno) {
        this.giorno = giorno;
    }


    /**
     * Recupera l'orario di inizio del vincolo.
     * @return L'orario di inizio.
     */
    public LocalTime getOraInizio() {
        return oraInizio;
    }


    /**
     * Imposta l'orario di inizio del vincolo.
     * @param oraInizio L'orario da impostare.
     */
    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }


    /**
     * Recupera l'orario di fine del vincolo.
     * @return L'orario di fine.
     */
    public LocalTime getOraFine() {
        return oraFine;
    }


    /**
     * Imposta l'orario di fine del vincolo.
     * @param oraFine L'orario da impostare.
     */
    public void setOraFine(LocalTime oraFine) {
        this.oraFine = oraFine;
    }
}
