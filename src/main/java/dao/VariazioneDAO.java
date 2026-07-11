package dao;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;

public interface VariazioneDAO {

    void creaVariazione(String insegnamento, LocalDate dataOriginale, LocalDate nuovaData, LocalTime oraInizioOriginale, LocalTime nuovaOraInizio, LocalTime nuovaOraFine, String aula) throws Exception;
    ResultSet getVariazioni(int anno) throws Exception;
    ResultSet getVariazioni(String email) throws Exception;
    void eliminaVariazione(String insegnamento, LocalDate dataOriginale, LocalTime oraInizioOriginale ) throws Exception;
}
