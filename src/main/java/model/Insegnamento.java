package model;

import java.util.ArrayList;
import java.util.List;

public class Insegnamento {
    private String nome;
    private int numeroCFU;
    private List<Lezione> lezioni;
    private int annoAccademico;
    private String docente;

    public Insegnamento(String nome, int numeroCFU, int annoAccademico, String docente) {
        this.nome = nome;
        this.numeroCFU = numeroCFU;
        this.annoAccademico = annoAccademico;
        lezioni = new ArrayList<>();
        this.docente = docente;
    }


    /**
     * Imposta o modifica il nome dell'insegnamento.
     *
     * @param nome Il nuovo nome da assegnare al corso.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }


    /**
     * Imposta o modifica il numero di crediti formativi (CFU) dell'insegnamento.
     *
     * @param numeroCFU Il numero di CFU da assegnare al corso.
     */
    public void setNumeroCFU(int numeroCFU) {
        this.numeroCFU = numeroCFU;
    }


    /**
     * Imposta la lista delle lezioni per questo insegnamento.
     *
     * @param lezioni La lista di oggetti {@link Lezione} da associare al corso.
     */
    public void setLezioni(List<Lezione> lezioni) {
        this.lezioni = lezioni;
    }


    /**
     * Imposta l'anno accademico di riferimento per l'insegnamento.
     *
     * @param annoAccademico Il valore dell'anno accademico da assegnare.
     */
    public void setAnnoAccademico(int annoAccademico) {
        this.annoAccademico = annoAccademico;
    }


    /**
     * Imposta o modifica il docente titolare dell'insegnamento.
     *
     * @param docente Il nome del docente da assegnare al corso.
     */
    public void setDocente(String docente) {
        this.docente = docente;
    }



    /**
     * Recupera il nome dell'insegnamento.
     * @return Il nome del corso.
     */
    public String getNome() {
        return nome;
    }


    /**
     * Recupera il numero di CFU dell'insegnamento.
     * @return Il valore dei crediti formativi.
     */
    public int getNumeroCFU() {
        return numeroCFU;
    }



    /**
     * Recupera l'elenco delle lezioni associate all'insegnamento.
     * @return Una lista di oggetti {@link Lezione}.
     */
    public List<Lezione> getLezioni() {
        return lezioni;
    }


    /**
     * Recupera l'anno accademico di riferimento.
     * @return L'anno accademico.
     */
    public int getAnnoAccademico() {
        return annoAccademico;
    }


    /**
     * Recupera il nome del docente titolare.
     * @return Il nome del docente.
     */
    public String getDocente() {
        return docente;
    }
}
