package dao;

import java.sql.ResultSet;
import java.time.LocalTime;

public interface LezioneDAO {
    void creaLezioneDB(String giornoSettimana, LocalTime oraInizio, LocalTime oraFine, String aula, String insegnamento) throws Exception;
    ResultSet getLezioniDB(String email) throws Exception;
    ResultSet getLezioniDB(int anno) throws Exception;
}
