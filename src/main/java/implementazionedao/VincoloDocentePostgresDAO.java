package implementazionedao;
import dao.VincoloDocenteDAO;
import database_connection.ConnessioneDatabase;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.time.LocalTime;

public class VincoloDocentePostgresDAO implements VincoloDocenteDAO {

    private final Connection connessioneDatabase;

    public VincoloDocentePostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e) ;
        }
    }

    /**
     * Aggiunge un vincolo di indisponibilità al database per un {@link model.Docente}.
     * <p>
     * Il metodo inserisce un record nella tabella {@code vincolo_docente}, definendo
     * gli intervalli temporali in cui il docente non risulta disponibile. La query
     * utilizza un cast esplicito (`?::giorno_settimana`) per assicurare la
     * compatibilità con il tipo enumerato definito nello schema del database.
     *
     * @param email L'indirizzo email del docente.
     * @param giorno Il giorno della settimana in cui il docente è indisponibile.
     * @param oraInizio L'orario di inizio dell'indisponibilità
     * @param oraFine L'orario di fine dell'indisponibilità
     * @throws SQLException In caso di errori di persistenza o violazioni dei vincoli di database.
     */
    public void creaVincolo(String email, String giorno, LocalTime oraInizio, LocalTime oraFine) throws SQLException
    {
        String sql = "INSERT INTO vincolo_docente(email_docente, giorno, ora_inizio, ora_fine) VALUES(?,?::giorno_settimana,?,?);";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, email);
            query.setString(2, giorno);
            query.setTime(3, Time.valueOf(oraInizio));
            query.setTime(4, Time.valueOf(oraFine));
            query.executeUpdate();
        }  catch (SQLException e) {
            throw new SQLException("Errore: impossibile registrare il vincolo di indisponibilità!");
        }
    }


    /**
     * Rimuove tutti i vincoli di indisponibilità associati a un {@link model.Docente}.
     * <p>
     * Il metodo esegue un'operazione di {@code DELETE} sulla tabella {@code vincolo_docente},
     * eliminando tutti i record collegati all'indirizzo email del docente specificato.
     * L'utilizzo di una {@link PreparedStatement} assicura la corretta esecuzione della
     * query e la protezione da eventuali iniezioni SQL.
     *
     * @param email L'indirizzo email del docente di cui si vogliono eliminare i vincoli.
     * @throws SQLException In caso di errori durante l'esecuzione della query di eliminazione.
     */
    public void eliminaVincoliDocente (String email) throws SQLException {
        String sql = "DELETE FROM vincolo_docente WHERE email_docente = ?";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql)) {
            query.setString(1, email);
            query.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Errore: impossibile eliminare i vincoli di indisponibilità per il docente indicato!");
        }
    }

    /**
     * Restituisce l'elenco dei vincoli di indisponibilità inseriti da un {@link model.Docente}.
     * <p>
     * Il metodo esegue una query di selezione sulla tabella {@code vincolo_docente},
     * filtrando i record per l'indirizzo email del docente specificato. Il set di
     * risultati viene caricato in un {@link CachedRowSet}, permettendo la consultazione
     * dei vincoli in modalità disconnessa dal database.
     *
     * @param email L'indirizzo email del docente di cui si vogliono recuperare i vincoli.
     * @return Un {@link ResultSet} (nello specifico un {@link CachedRowSet}) contenente
     * i dati dei vincoli trovati.
     * @throws SQLException In caso di errori durante l'esecuzione della query o
     * durante il popolamento della struttura dati.
     */
    public ResultSet getVincoli(String email) throws SQLException
    {
        String sql = "SELECT email_docente, giorno, ora_inizio, ora_fine FROM vincolo_docente WHERE email_docente LIKE ?;";
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, email);
            ResultSet rs = query.executeQuery();
            crs.populate(rs);
        } catch (SQLException e) {crs.close(); throw new SQLException("Errore: impossibile recuperare i vincoli per il docente indicato!");}
        return crs;
    }

    /**
     * Restituisce l'elenco completo di tutti i vincoli di indisponibilità inseriti nel database.
     * <p>
     * Il metodo esegue una query di selezione sull'intera tabella {@code vincolo_docente},
     * restituendo un {@link ResultSet} che contiene i dettagli relativi a tutti i
     * docenti, ai giorni e agli intervalli temporali di indisponibilità.
     *
     * @return Un {@link ResultSet} contenente i dati di tutti i vincoli presenti.
     * @throws SQLException In caso di errori durante l'esecuzione della query di selezione.
     */
    public ResultSet getVincoliR() throws SQLException
    {
        String sql = "SELECT email_docente, giorno, ora_inizio, ora_fine FROM vincolo_docente;";
        ResultSet rs;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            rs = query.executeQuery();
        } catch (SQLException e) {throw new SQLException("Errore: impossibile recuperare l'elenco dei vincoli dal database!");}
        return rs;
    }

}
