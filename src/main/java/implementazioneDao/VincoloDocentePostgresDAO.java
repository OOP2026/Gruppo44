package implementazioneDao;
import dao.VincoloDocenteDAO;
import database_connection.ConnessioneDatabase;
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
     * @throws Exception In caso di errori nel database.
     */
    public void creaVincolo(String email, String giorno, LocalTime oraInizio, LocalTime oraFine) throws Exception
    {
        String sql = "INSERT INTO vincolo_docente(email_docente, giorno, ora_inizio, ora_fine) VALUES(?,?,?,?);";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, email);
            query.setString(2, giorno);
            query.setTime(3, Time.valueOf(oraInizio));
            query.setTime(4, Time.valueOf(oraFine));
            query.executeUpdate();
        } catch (SQLException e) {throw new Exception("Si è verificato un errore nel database.");}
    }

    public void eliminaVincoloDocente (String email, String giorno,LocalTime oraInizio ) throws Exception {
        String sql = "DELETE FROM vincolo_docente WHERE email_docente = ? AND giorno = ? AND  ora_inizio = ?;";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql)) {
            query.setString(1, email);
            query.setString(2, giorno);
            query.setTime(3, Time.valueOf(oraInizio));
            query.executeUpdate();
        }  catch (SQLException e) {throw new Exception("Si è verificato un errore nel database.");}
    }

    /**
     * Restituisce i vincoli inseriti da un docente.
     * @param email L'email del docente.
     * @return ResultSet contenente i dati dei vincoli selezionati.
     * @throws Exception In caso di errori nel database.
     */
    public ResultSet getVincoli(String email) throws Exception
    {
        String sql = "SELECT * FROM vincolo_docente WHERE email_docente LIKE ?;";
        ResultSet rs;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, email);
            rs = query.executeQuery(sql);
        } catch (SQLException e) {throw new Exception("Si è verificato un errore nel database.");}
        return rs;
    }

    /**
     * Restituisce tutti i vincoli inseriti nel database
     * @return ResultSet contenente i dati di tutti i vincoli.
     * @throws Exception In caso di errori nel database.
     */
    public ResultSet getVincoliR() throws Exception
    {
        String sql = "SELECT * FROM vincolo_docente;";
        ResultSet rs;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            rs = query.executeQuery(sql);
        } catch (SQLException e) {throw new Exception("Si è verificato un errore nel database.");}
        return rs;
    }

}
