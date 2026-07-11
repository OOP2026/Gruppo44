package dao;

import model.Aula;

import java.sql.ResultSet;

public interface AulaDAO {
    void creaAula (String nomeAula, int capienzaMassima) throws Exception;
    void eliminaAula (String nomeAula) throws Exception;
    ResultSet getAule() throws Exception;
}
