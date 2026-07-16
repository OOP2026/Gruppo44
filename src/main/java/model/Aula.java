package model;

public class Aula {
    private int capienzaMassima;
    private String nomeAula;

    public Aula(String numeroAula, int capienzaMassima) {
        this.capienzaMassima = capienzaMassima;
        this.nomeAula = numeroAula;
    }

    public void setCapienzaMassima(int capienzaMassima) {
        this.capienzaMassima = capienzaMassima;
    }

    public void setNomeAula(String nomeAula) {
        this.nomeAula = nomeAula;
    }

    /**
     * Recupera la capienza massima definita per l'aula.
     * @return Un valore intero rappresentante il numero massimo di posti disponibili.
     */
    public int getCapienzaMassima() {
        return capienzaMassima;
    }

    /**
     * Recupera il nome (identificativo) dell'aula.
     * @return Una stringa contenente il nome dell'aula.
     */
    public String getNomeAula() {
        return nomeAula;
    }

}
