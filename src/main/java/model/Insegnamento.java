package model;

import java.util.ArrayList;

public class Insegnamento {
    public String nome;
    int numeroCFU;
    public ArrayList<Lezione> lezioni;
    public int annoAccademico;
    public Docente docente;

    public Insegnamento(String nome, int numeroCFU, int annoAccademico, Docente docente) {
        this.nome = nome;
        this.numeroCFU = numeroCFU;
        this.annoAccademico = annoAccademico;
        lezioni = new ArrayList<>();
        this.docente = docente;
    }


    public Insegnamento(String nome, int numeroCFU, int annoAccademico) {
        this.nome = nome;
        this.numeroCFU = numeroCFU;
        this.annoAccademico = annoAccademico;
        lezioni = new ArrayList<>();
        this.docente = null;
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

    public ArrayList<Lezione> getLezioni() {
        return lezioni;
    }

    public void setLezioni(ArrayList<Lezione> lezioni) {
        this.lezioni = lezioni;
    }

    public int getAnnoAccademico() {
        return annoAccademico;
    }

    public void setAnnoAccademico(int annoAccademico) {
        this.annoAccademico = annoAccademico;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }
}
