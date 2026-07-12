package dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public interface VariazioneDAO {

    void creaVariazione(String insegnamento, LocalDate dataOriginale, LocalDate nuovaData, LocalTime oraInizioOriginale, LocalTime nuovaOraInizio, LocalTime nuovaOraFine, String aula) throws SQLException;
    ResultSet getVariazioni(int anno) throws SQLException;
    ResultSet getVariazioni(String email) throws SQLException;
    void eliminaVariazione(String insegnamento, LocalDate dataOriginale, LocalTime oraInizioOriginale ) throws SQLException;
}
