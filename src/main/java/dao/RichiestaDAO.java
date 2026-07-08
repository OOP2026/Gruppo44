package dao;
import java.sql.ResultSet;

public interface RichiestaDAO {
    void aggiungiRichiestaSpostamento(String nomeInsegnamento, String oraOriginale, String giornoOriginale, String giornoRichiesto, String oraInizioRichiesta, String oraFineRichiesta, String aula) throws Exception;
    ResultSet getRegistroRichiesteSpostamento() throws Exception;
    ResultSet cancellaRichiesta(int id_richiesta) throws Exception;
}
