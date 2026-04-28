package model;

public class Insegnamento {
    enum CFU{
        TRE,
        SEI,
        NOVE,
        DODICI
    }
    String nome;
    CFU numeroCFU;

    Insegnamento(String nome, CFU numeroCFU){
        this.nome = nome;
        this.numeroCFU = numeroCFU;
    }

}
