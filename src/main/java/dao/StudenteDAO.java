package dao;

import java.sql.ResultSet;

public interface StudenteDAO {
    ResultSet login(String email, String password) throws Exception;
    void creaStudente(String nome, String cognome, String email, String password, String matricola, int anno) throws Exception;
}
