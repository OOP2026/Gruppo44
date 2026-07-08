package implementazioneDao;
import dao.RichiestaDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.time.LocalDate;

public class RichiestaPostgresDAO implements RichiestaDAO {
    private Connection connessioneDatabase;

    public RichiestaPostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aggiunge una richiesta di spostamento di una lezione al database.
     * @param nomeInsegnamento Il nome dell'insegnamento relativo alla richiesta.
     * @param oraOriginale L'orario originale della lezione relativa.
     * @param dataOriginale La data originale della lezione relativa.
     * @param dataRichiesta La data richiesta dal docente.
     * @param oraInizioRichiesta L'ora di inizio richiesta dal docente.
     * @param oraFineRichiesta L'ora di fine richiesta dal docente.
     * @throws Exception In caso di errori nel database.
     */
    public void aggiungiRichiestaSpostamento(String nomeInsegnamento, String oraOriginale, String dataOriginale, String dataRichiesta, String oraInizioRichiesta, String oraFineRichiesta, String aula) throws Exception{
        String sql = "INSERT INTO richiesta (insegnamento, ora_inizio_originale, data_originale, data_richiesta, ora_inizio, ora_fine, giorno_settimana, aula) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        int giornoSettimana = LocalDate.parse(dataOriginale).getDayOfWeek().getValue() - 1;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, nomeInsegnamento);
            query.setTime(2, Time.valueOf(oraOriginale));
            query.setString(3, dataOriginale);
            query.setString(4, dataRichiesta);
            query.setTime(5, Time.valueOf(oraInizioRichiesta));
            query.setTime(6, Time.valueOf(oraFineRichiesta));
            query.setInt(7, giornoSettimana);
            query.setString(8, aula);
            query.executeUpdate();

        } catch (SQLException e) {throw new Exception("Non è possibile aggiungere richiesta!");
        }
    }

    /**
     * Restituisce tutte le richieste di spostamento delle lezioni presenti nel database.
     * @return ResultSet contenente i dati di tutte le richieste.
     * @throws Exception In caso di errori nel database.
     */
    public ResultSet getRegistroRichiesteSpostamento() throws Exception{
        String sql = "SELECT * FROM richiesta";
        ResultSet result;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql)){
            result = query.executeQuery();
        }
        catch (SQLException e){throw new Exception("Si è verificato un errore!");}
        return result;
    }

    /**
     * Cancella una richiesta di spostamento dal database e ne restituisce i dati.
     * @param id_richiesta l'identificativo della richiesta.
     * @return ResultSet contenente i dati della richiesta cancellata.
     * @throws Exception In caso di errori nel database.
     */
    public ResultSet cancellaRichiesta(int id_richiesta) throws Exception {
        String sql= "DELETE FROM richiesta WHERE id_richiesta = ? RETURNING *;";
        ResultSet result;

        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setInt(1, id_richiesta);
            result = query.executeQuery();
        }
        catch (SQLException e) {throw new Exception("Si è verificato un errore!");}
        return result;
    }


}
