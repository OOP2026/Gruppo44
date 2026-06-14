package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public interface VincoloDocenteDAO {
    void creaVincolo(String email, String giorno, LocalTime oraInizio, LocalTime oraFine) throws Exception;
    ResultSet getVincoli(String email) throws Exception;
    ResultSet getVincoliR() throws Exception;
}
