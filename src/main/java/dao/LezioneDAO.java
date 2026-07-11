package dao;
import java.sql.ResultSet;
import java.time.LocalTime;


public interface LezioneDAO {
    void creaLezione(String giornoSettimana, LocalTime oraInizio, LocalTime oraFine, String aula, String insegnamento) throws Exception;
    ResultSet getLezioni(String email) throws Exception;
    ResultSet getLezioni(int anno) throws Exception;
    ResultSet getLezioni() throws Exception;
    void eliminaLezione(String insegnamento, String giornoSettimana, LocalTime oraInizio) throws Exception;
}
