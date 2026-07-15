package dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;


public interface LezioneDAO {
    void creaLezione(String giornoSettimana, LocalTime oraInizio, LocalTime oraFine, String aula, String insegnamento) throws SQLException;
    ResultSet getLezioni(String email) throws SQLException;
    ResultSet getLezioni(int anno) throws SQLException;
    ResultSet getLezioni() throws SQLException;
    void eliminaLezioniInsegnamento(String insegnamento) throws SQLException;
}
