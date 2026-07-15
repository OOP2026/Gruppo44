package dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public interface VincoloDocenteDAO {
    void creaVincolo(String email, String giorno, LocalTime oraInizio, LocalTime oraFine) throws SQLException;
    ResultSet getVincoli(String email) throws SQLException;
    ResultSet getVincoliR() throws SQLException;
    void eliminaVincoliDocente (String email) throws SQLException;

}
