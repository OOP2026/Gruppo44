package model;

import java.util.ArrayList;

public class Insegnamento {
    String nome;
    int numeroCFU;
    public ArrayList<Lezione> lezioni;
    public int annoAccademico;

    public Insegnamento(String nome, int numeroCFU, int annoAccademico) {
        this.nome = nome;
        this.numeroCFU = numeroCFU;
        this.annoAccademico = annoAccademico;
        lezioni = new ArrayList<>();
    }

}
