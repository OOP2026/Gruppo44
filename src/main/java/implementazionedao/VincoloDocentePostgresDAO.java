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
     * Aggiunge un vincolo di indisponibilità al database.
     * @param email L'email del docente.
     * @param giorno Il giorno dell'indisponibilità.
     * @param oraInizio L'orario di inizio dell'indisponibilità
     * @param oraFine L'orario di fine dell'indisponibilità
     * @throws SQLException In caso di errori nel database.
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
        }
    }

    public void eliminaVincoliDocente (String email) throws SQLException {
        String sql = "DELETE FROM vincolo_docente WHERE email_docente = ?";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql)) {
            query.setString(1, email);
            query.executeUpdate();
        }
    }

    /**
     * Restituisce i vincoli inseriti da un docente.
     * @param email L'email del docente.
     * @return ResultSet contenente i dati dei vincoli selezionati.
     * @throws SQLException In caso di errori nel database.
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
        } catch (SQLException e) {crs.close(); throw new SQLException("Si è verificato un errore nel database.");}
        return crs;
    }

    /**
     * Restituisce tutti i vincoli inseriti nel database
     * @return ResultSet contenente i dati di tutti i vincoli.
     * @throws SQLException In caso di errori nel database.
     */
    public ResultSet getVincoliR() throws SQLException
    {
        String sql = "SELECT email_docente, giorno, ora_inizio, ora_fine FROM vincolo_docente;";
        ResultSet rs;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            rs = query.executeQuery();
        } catch (SQLException e) {throw new SQLException("Si è verificato un errore nel database.");}
        return rs;
    }

}
