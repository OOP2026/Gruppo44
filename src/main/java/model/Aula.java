package model;

public class Aula {
    int capienzaMassima;
    String numeroAula;

    Aula(int capienzaMassima, String numeroAula){
        this.capienzaMassima = capienzaMassima;
        this.numeroAula = numeroAula;
    }


    public int getCapienzaMassima() {
        return capienzaMassima;
    }

    public void setCapienzaMassima(int capienzaMassima) {
        this.capienzaMassima = capienzaMassima;
    }

    public String getNumeroAula() {
        return numeroAula;
    }

    public void setNumeroAula(String numeroAula) {
        this.numeroAula = numeroAula;
    }
}
