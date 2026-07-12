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


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumeroCFU() {
        return numeroCFU;
    }

    public void setNumeroCFU(int numeroCFU) {
        this.numeroCFU = numeroCFU;
    }

    public List<Lezione> getLezioni() {
        return lezioni;
    }

    public void setLezioni(List<Lezione> lezioni) {
        this.lezioni = lezioni;
    }

    public int getAnnoAccademico() {
        return annoAccademico;
    }

    public void setAnnoAccademico(int annoAccademico) {
        this.annoAccademico = annoAccademico;
    }

    public String getDocente() {
        return docente;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }
}
