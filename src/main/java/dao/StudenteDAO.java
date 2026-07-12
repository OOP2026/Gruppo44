package dao;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface StudenteDAO {
    ResultSet login(String email, String password) throws SQLException;
    void creaStudente(String nome, String cognome, String email, String password, String matricola, int anno) throws SQLException;
    ResultSet getAnnoStudente(String email) throws SQLException;
    void eliminaStudente(String email) throws SQLException;
}
