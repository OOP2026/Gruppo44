package model;

import java.util.ArrayList;

public class Insegnamento {
    String nome;
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




}
