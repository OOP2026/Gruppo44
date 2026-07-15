package implementazionedao;
import dao.RichiestaDAO;
import database_connection.ConnessioneDatabase;
import common.GiornoSettimana;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class RichiestaPostgresDAO implements RichiestaDAO {

    private final Connection connessioneDatabase;

    public RichiestaPostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
     * @throws SQLException In caso di errori nel database.
     */
    public void aggiungiRichiestaSpostamento(String nomeInsegnamento, LocalTime oraOriginale, LocalDate dataOriginale, LocalDate dataRichiesta, LocalTime oraInizioRichiesta, LocalTime oraFineRichiesta, String aula) throws SQLException{
        String sql = "INSERT INTO richiesta (insegnamento, ora_inizio_originale, data_originale, data_richiesta, ora_inizio, ora_fine, giorno_settimana, aula) VALUES (?, ?, ?, ?, ?, ?, ?::giorno_settimana, ?);";
        String giornoSettimana = GiornoSettimana.values()[dataOriginale.getDayOfWeek().getValue() - 1].name();
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, nomeInsegnamento);
            query.setTime(2, Time.valueOf(oraOriginale));
            query.setDate(3, Date.valueOf(dataOriginale));
            query.setDate(4, Date.valueOf (dataRichiesta));
            query.setTime(5, Time.valueOf(oraInizioRichiesta));
            query.setTime(6, Time.valueOf(oraFineRichiesta));
            query.setString(7, giornoSettimana);
            query.setString(8, aula);
            query.executeUpdate();

        } catch (SQLException e) {throw e;}
    }

    /**
     * Restituisce tutte le richieste di spostamento delle lezioni presenti nel database.
     * @return ResultSet contenente i dati di tutte le richieste.
     * @throws SQLException In caso di errori nel database.
     */
    public ResultSet getRegistroRichiesteSpostamento() throws SQLException{
        String sql = "SELECT insegnamento, data_originale, data_richiesta, ora_inizio_originale, ora_inizio, ora_fine, id_richiesta, giorno_settimana, aula FROM richiesta";
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql)){
            ResultSet rs = query.executeQuery();
            crs.populate(rs);
        }
        catch (SQLException e){crs.close(); throw new SQLException("Si è verificato un errore!");}
        return crs;
    }

    /**
     * Cancella una richiesta di spostamento dal database e ne restituisce i dati.
     * @param idRichiesta l'identificativo della richiesta.
     * @return ResultSet contenente i dati della richiesta cancellata.
     * @throws SQLException In caso di errori nel database.
     */
    public ResultSet cancellaRichiesta(int idRichiesta) throws SQLException {
        String sql= "DELETE FROM richiesta WHERE id_richiesta = ? RETURNING *;";
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();

        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setInt(1, idRichiesta);
            ResultSet rs = query.executeQuery();
            crs.populate(rs);
        }
        catch (SQLException e) {crs.close(); throw new SQLException("Si è verificato un errore!");}
        return crs;
    }


}
