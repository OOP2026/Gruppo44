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
     * Aggiunge una nuova richiesta di spostamento di una lezione nel database.
     * <p>
     * Il metodo registra i dettagli della lezione originale e le nuove specifiche temporali
     * richieste dal docente. Include la conversione dinamica del giorno della settimana
     * a partire dalla {@link LocalDate} originale, garantendo la compatibilità con il
     * tipo ENUM definito nello schema del database.
     *
     * @param nomeInsegnamento Il nome dell'insegnamento oggetto alla richiesta.
     * @param oraOriginale L'orario originale della lezione.
     * @param dataOriginale La data originale della lezione.
     * @param dataRichiesta La data richiesta dal docente.
     * @param oraInizioRichiesta L'ora di inizio richiesta dal docente.
     * @param oraFineRichiesta L'ora di fine richiesta dal docente.
     * @throws SQLException In caso di errori durante l'esecuzione della query di inserimento.
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

        } catch (SQLException e) {
            throw new SQLException("Errore: impossibile registrare la richiesta di spostamento nel database!");
        }
    }

    /**
     * Restituisce tutte le richieste di spostamento delle lezioni presenti nel database.
     * <p>
     * Il metodo esegue una query di selezione sull'intera tabella {@code richiesta}
     * e popola un {@link CachedRowSet}. L'utilizzo di questa struttura dati evita che la
     * connessione debba rimanere aperta dopo la chiusura del blocco del metodo.
     *
     * @return Un {@link ResultSet} (nello specifico un {@link CachedRowSet}) contenente i dati di tutte le richieste di spostamento.
     * @throws SQLException In caso di errori durante l'interazione con il database o
     * durante il popolamento del set di risultati
     */
    public ResultSet getRegistroRichiesteSpostamento() throws SQLException{
        String sql = "SELECT insegnamento, data_originale, data_richiesta, ora_inizio_originale, ora_inizio, ora_fine, id_richiesta, giorno_settimana, aula FROM richiesta";
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql)){
            ResultSet rs = query.executeQuery();
            crs.populate(rs);
        }
        catch (SQLException e){crs.close(); throw new SQLException("Errore: impossibile recuperare il registro delle richieste di spostamento!");}
        return crs;
    }

    /**
     * Cancella una richiesta di spostamento dal database e ne restituisce i dati eliminati.
     * <p>
     * Il metodo esegue una query {@code DELETE ... RETURNING *}, che rimuove il record
     * identificato da {@code idRichiesta} e simultaneamente restituisce i dati del record
     * appena eliminato. Il risultato viene caricato in un {@link CachedRowSet} per
     * permetterne la consultazione in modalità disconnessa.
     *
     * @param idRichiesta L'identificativo della richiesta da eliminare.
     * @return Un {@link ResultSet} (nello specifico un {@link CachedRowSet}) contenente i dati della richiesta cancellata.
     * @throws SQLException In caso di errori durante l'operazione di eliminazione o
     * nel popolamento della struttura dati.
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
        catch (SQLException e) {crs.close(); throw new SQLException("Errore: impossibile eliminare la richiesta di spostamento dal database!");}
        return crs;
    }
}
