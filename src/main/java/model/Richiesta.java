package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Richiesta {

    private String insegnamento;
    private LocalDate giornoOriginale;
    private LocalDate giornoRichiesto;
    private LocalTime oraInizioRichiesta;
    private LocalTime oraInizioOriginale;
    private LocalTime oraFineRichiesta;
    private String aula;

    public int idRichiesta;


    public Richiesta(String insegnamento, LocalDate giornoOriginale, LocalDate giornoRichiesto, LocalTime oraInizioOriginale, LocalTime oraInizioRichiesta, LocalTime oraFineRichiesta, String aula, int idRichiesta){
        this.insegnamento = insegnamento;
        this.giornoOriginale = giornoOriginale;
        this.giornoRichiesto = giornoRichiesto;
        this.oraInizioOriginale = oraInizioOriginale;
        this.oraInizioRichiesta = oraInizioRichiesta;
        this.oraFineRichiesta = oraFineRichiesta;
        this.aula = aula;
        this.idRichiesta = idRichiesta;
    }

    public Richiesta(String insegnamento, LocalDate giornoOriginale, LocalDate giornoRichiesto, LocalTime oraInizioOriginale, LocalTime oraInizioRichiesta, LocalTime oraFineRichiesta, String aula){
        this.insegnamento = insegnamento;
        this.giornoOriginale = giornoOriginale;
        this.giornoRichiesto = giornoRichiesto;
        this.oraInizioOriginale = oraInizioOriginale;
        this.oraInizioRichiesta = oraInizioRichiesta;
        this.oraFineRichiesta = oraFineRichiesta;
        this.aula = aula;
    }


    /**
     * Recupera il nome dell'insegnamento coinvolto nella richiesta.
     * @return Il nome del corso.
     */
    public String getInsegnamento() {
        return insegnamento;
    }


    /**
     * Imposta il nome dell'insegnamento per la richiesta.
     * @param insegnamento Il nuovo nome dell'insegnamento.
     */
    public void setInsegnamento(String insegnamento) {
        this.insegnamento = insegnamento;
    }


    /**
     * Recupera la data originale della lezione.
     * @return Un oggetto {@link LocalDate} con la data originaria.
     */
    public LocalDate getGiornoOriginale() {
        return giornoOriginale;
    }


    /**
     * Imposta la data originale della lezione.
     * @param giornoOriginale La data da impostare.
     */
    public void setGiornoOriginale(LocalDate giornoOriginale) {
        this.giornoOriginale = giornoOriginale;
    }


    /**
     * Recupera la data richiesta per la lezione.
     * @return Un oggetto {@link LocalDate} con la nuova data.
     */
    public LocalDate getGiornoRichiesto() {
        return giornoRichiesto;
    }


    /**
     * Imposta la data richiesta per la lezione.
     * @param giornoRichiesto La nuova data da impostare.
     */
    public void setGiornoRichiesto(LocalDate giornoRichiesto) {
        this.giornoRichiesto = giornoRichiesto;
    }


    /**
     * Recupera l'orario di inizio richiesto per la lezione.
     * @return Un oggetto {@link LocalTime} con l'orario richiesto.
     */
    public LocalTime getOraInizioRichiesta() {
        return oraInizioRichiesta;
    }


    /**
     * Imposta l'orario di inizio richiesto per la lezione.
     * @param oraInizioRichiesta L'orario da impostare.
     */
    public void setOraInizioRichiesta(LocalTime oraInizioRichiesta) {
        this.oraInizioRichiesta = oraInizioRichiesta;
    }


    /**
     * Recupera l'orario di inizio originale della lezione.
     * @return Un oggetto {@link LocalTime} con l'orario originale.
     */
    public LocalTime getOraInizioOriginale() {
        return oraInizioOriginale;
    }


    /**
     * Imposta l'orario di inizio originale della lezione.
     * @param oraInizioOriginale L'orario originale da impostare.
     */
    public void setOraInizioOriginale(LocalTime oraInizioOriginale) {
        this.oraInizioOriginale = oraInizioOriginale;
    }


    /**
     * Recupera l'orario di fine richiesto per la lezione.
     * @return Un oggetto {@link LocalTime} con l'orario di fine richiesto.
     */
    public LocalTime getOraFineRichiesta() {
        return oraFineRichiesta;
    }


    /**
     * Imposta l'orario di fine richiesto per la lezione.
     * @param oraFineRichiesta L'orario di fine da impostare.
     */
    public void setOraFineRichiesta(LocalTime oraFineRichiesta) {
        this.oraFineRichiesta = oraFineRichiesta;
    }


    /**
     * Recupera l'identificativo univoco della richiesta.
     * @return L'ID della richiesta.
     */
    public int getIdRichiesta() {
        return idRichiesta;
    }


    /**
     * Imposta l'identificativo univoco della richiesta.
     * @param idRichiesta L'ID da assegnare.
     */
    public void setIdRichiesta(int idRichiesta) {
        this.idRichiesta = idRichiesta;
    }


    /**
     * Recupera l'aula associata alla richiesta di spostamento.
     * @return Il nome dell'aula.
     */
    public String getAula() {
        return aula;
    }


    /**
     * Imposta l'aula per la richiesta di spostamento.
     * @param aula Il nome dell'aula da impostare.
     */
    public void setAula(String aula) {
        this.aula = aula;
    }
}
