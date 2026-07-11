package dao;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;

public interface RichiestaDAO {
    void aggiungiRichiestaSpostamento(String nomeInsegnamento, LocalTime oraOriginale, LocalDate giornoOriginale, LocalDate giornoRichiesto, LocalTime oraInizioRichiesta, LocalTime oraFineRichiesta, String aula) throws Exception;
    ResultSet getRegistroRichiesteSpostamento() throws Exception;
    ResultSet cancellaRichiesta(int id_richiesta) throws Exception;
}
