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


    /**
     * Recupera la data originale della lezione.
     * @return La data originale.
     */
    public LocalDate getDataOriginale() {
        return dataOriginale;
    }


    /**
     * Imposta la data originale della lezione.
     * @param dataOriginale La data da impostare.
     */
    public void setDataOriginale(LocalDate dataOriginale) {
        this.dataOriginale = dataOriginale;
    }


    /**
     * Recupera la nuova data assegnata alla lezione.
     * @return La nuova data.
     */
    public LocalDate getNuovaData() {
        return nuovaData;
    }


    /**
     * Imposta la nuova data della lezione.
     * @param nuovaData La nuova data da impostare.
     */
    public void setNuovaData(LocalDate nuovaData) {
        this.nuovaData = nuovaData;
    }

    /**
     * Recupera l'orario di inizio originale.
     * @return L'orario di inizio originale.
     */
    public LocalTime getOraInizioOriginale() {
        return oraInizioOriginale;
    }


    /**
     * Imposta l'orario di inizio originale.
     * @param oraInizioOriginale L'orario da impostare.
     */
    public void setOraInizioOriginale(LocalTime oraInizioOriginale) {
        this.oraInizioOriginale = oraInizioOriginale;
    }


    /**
     * Recupera il nuovo orario di inizio della lezione.
     * @return Il nuovo orario di inizio.
     */
    public LocalTime getNuovaOraInizio() {
        return nuovaOraInizio;
    }


    /**
     * Imposta il nuovo orario di inizio della lezione.
     * @param nuovaOraInizio L'orario da impostare.
     */
    public void setNuovaOraInizio(LocalTime nuovaOraInizio) {
        this.nuovaOraInizio = nuovaOraInizio;
    }


    /**
     * Recupera il nuovo orario di fine della lezione.
     * @return Il nuovo orario di fine.
     */
    public LocalTime getNuovaOraFine() {
        return nuovaOraFine;
    }


    /**
     * Imposta il nuovo orario di fine della lezione.
     * @param nuovaOraFine L'orario di fine da impostare.
     */
    public void setNuovaOraFine(LocalTime nuovaOraFine) {
        this.nuovaOraFine = nuovaOraFine;
    }


    /**
     * Recupera l'aula associata alla variazione.
     * @return Il nome dell'aula.
     */
    public String getAula() {
        return aula;
    }


    /**
     * Imposta l'aula per la lezione variata.
     * @param aula Il nome dell'aula da impostare.
     */
    public void setAula(String aula) {
        this.aula = aula;
    }


    /**
     * Recupera l'insegnamento oggetto della variazione.
     * @return Il nome dell'insegnamento.
     */
    public String getInsegnamento() {
        return insegnamento;
    }


    /**
     * Imposta l'insegnamento oggetto della variazione.
     * @param insegnamento Il nome dell'insegnamento da impostare.
     */
    public void setInsegnamento(String insegnamento) {
        this.insegnamento = insegnamento;
    }
}
