package model;

import common.GiornoSettimana;

import java.time.LocalTime;

public class Lezione {
    private GiornoSettimana giornoSettimana;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private String aula;
    private String insegnamento;

    public Lezione(GiornoSettimana g, LocalTime oraInizio, LocalTime oraFine, String aula, String insegnamento) {
        this.giornoSettimana = g;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.aula = aula;
        this.insegnamento = insegnamento;
    }

    //testing
    @Override
    public String toString(){
        return "Giorno: " + giornoSettimana + ", oraInizio: " + oraInizio + ", oraFine: " + oraFine + ", aula: " + aula + ", insegnamento: " + insegnamento;
    }

    public String toElementString(){
        return oraInizio+"\n" + oraFine + "\n" + insegnamento + "\n" + aula;

    }


    /**
     * Imposta il giorno della settimana in cui si tiene la lezione.
     *
     * @param giornoSettimana Il valore {@link common.GiornoSettimana} da assegnare.
     */
    public void setGiornoSettimana(GiornoSettimana giornoSettimana) {
        this.giornoSettimana = giornoSettimana;
    }


    /**
     * Imposta l'orario di inizio della lezione.
     *
     * @param oraInizio L'orario di inizio da assegnare ({@link java.time.LocalTime}).
     */
    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }


    /**
     * Imposta l'orario di fine della lezione.
     *
     * @param oraFine L'orario di fine da assegnare ({@link java.time.LocalTime}).
     */
    public void setOraFine(LocalTime oraFine) {
        this.oraFine = oraFine;
    }


    /**
     * Imposta l'aula in cui si terrà la lezione.
     *
     * @param aula Il nome dell'aula da assegnare.
     */
    public void setAula(String aula) {
        this.aula = aula;
    }


    /**
     * Imposta o modifica l'insegnamento di riferimento per la lezione.
     *
     * @param insegnamento Il nome dell'insegnamento da associare.
     */
    public void setInsegnamento(String insegnamento) {
        this.insegnamento = insegnamento;
    }



    /**
     * Recupera il giorno della settimana in cui si svolge la lezione.
     * @return Un valore di tipo {@link common.GiornoSettimana} rappresentante il giorno.
     */
    public GiornoSettimana getGiornoSettimana() {
        return giornoSettimana;
    }


    /**
     * Recupera l'orario di inizio della lezione.
     * @return Un oggetto {@link java.time.LocalTime} rappresentante l'orario di inizio.
     */
    public LocalTime getOraInizio() {
        return oraInizio;
    }

    /**
     * Recupera l'orario di fine della lezione.
     * @return Un oggetto {@link java.time.LocalTime} rappresentante l'orario di fine.
     */
    public LocalTime getOraFine() {
        return oraFine;
    }


    /**
     * Recupera l'aula in cui si svolge la lezione.
     * @return Una stringa rappresentante il nome dell'aula.
     */
    public String getAula() {
        return aula;
    }


    /**
     * Recupera il nome dell'insegnamento relativo alla lezione.
     * @return Una stringa rappresentante il nome dell'insegnamento.
     */
    public String getInsegnamento() {
        return insegnamento;
    }

}
