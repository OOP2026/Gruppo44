package model;

public class Aula {
    private int capienzaMassima;
    private String nomeAula;

    public Aula(String numeroAula, int capienzaMassima) {
        this.capienzaMassima = capienzaMassima;
        this.nomeAula = numeroAula;
    }


    public int getCapienzaMassima() {
        return capienzaMassima;
    }

    public void setCapienzaMassima(int capienzaMassima) {
        this.capienzaMassima = capienzaMassima;
    }

    public String getNomeAula() {
        return nomeAula;
    }

    public void setNomeAula(String nomeAula) {
        this.nomeAula = nomeAula;
    }
}
