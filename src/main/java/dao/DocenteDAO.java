package dao;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DocenteDAO {
   ResultSet login(String email, String password) throws SQLException;
   void creaDocente(String nome, String cognome, String email, String password) throws SQLException;
   void eliminaDocente(String email) throws SQLException;
}
