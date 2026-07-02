package dao;
import java.sql.ResultSet;

public interface RichiestaDAO {
    public void aggiungiRichiestaSpostamento(String nomeInsegnamento, String oraOriginale, String giornoOriginale, String giornoRichiesto, String oraInizioRichiesta, String oraFineRichiesta) throws Exception;
    public ResultSet getRegistroRichiesteSpostamento() throws Exception;
    public ResultSet cancellaRichiesta(int id_richiesta) throws Exception;
}
