package dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public interface RichiestaDAO {
    void aggiungiRichiestaSpostamento(String nomeInsegnamento, LocalTime oraOriginale, LocalDate giornoOriginale, LocalDate giornoRichiesto, LocalTime oraInizioRichiesta, LocalTime oraFineRichiesta, String aula) throws SQLException;
    ResultSet getRegistroRichiesteSpostamento() throws SQLException;
    ResultSet cancellaRichiesta(int idRichiesta) throws SQLException;
}
