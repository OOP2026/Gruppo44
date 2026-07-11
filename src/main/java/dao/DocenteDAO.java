package dao;
import java.sql.*;

public interface DocenteDAO {
   ResultSet login(String email, String password) throws Exception;
   void creaDocente(String nome, String cognome, String email, String password) throws Exception;
   void eliminaDocente(String email) throws Exception;
}
