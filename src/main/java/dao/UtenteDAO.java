package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface UtenteDAO {
    ResultSet login(String sql, String email, String password) throws SQLException;
}