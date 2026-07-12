package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface AulaDAO {
    void creaAula (String nomeAula, int capienzaMassima) throws SQLException;
    void eliminaAula (String nomeAula) throws SQLException;
    ResultSet getAule() throws SQLException;
}
