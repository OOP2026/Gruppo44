package implementazioneDao;
import dao.LezioneDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.time.LocalTime;


public class LezionePostgresDAO implements LezioneDAO {

    private final Connection connessioneDatabase;

    public LezionePostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crea una nuova {@link model.Lezione} e la salva nel database
     * @param giornoSettimana Il giorno in cui si svolge la lezione
     * @param oraInizio L'orario di inizio della lezione, come {@link java.time.LocalTime}
     * @param oraFine L'orario di fine della lezione, come {@link java.time.LocalTime}
     * @param aula Il nome o il codice dell'aula in cui si tiene la {@link model.Lezione}
     * @param insegnamento Il nome dell'{@link model.Insegnamento} a cui la lezione fa riferimento
     * @throws Exception Se i dati inseriti non sono validi o se si verifica un errore di persistenza nel database
     */


    public void creaLezione(String giornoSettimana, LocalTime oraInizio, LocalTime oraFine, String aula, String insegnamento) throws Exception{
        String sql = "INSERT INTO lezione(giorno_settimana, ora_inizio, ora_fine, aula, insegnamento) VALUES (?,?,?,?,?);";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, giornoSettimana);
            query.setTime(2, Time.valueOf(oraInizio));
            query.setTime(3, Time.valueOf(oraFine));
            query.setString(4, aula);
            query.setString(5, insegnamento);
            query.executeUpdate();
        } catch (SQLException e) {throw new Exception("Lezione non valida.");}
    }

    public void eliminaLezione(String insegnamento, String giornoSettimana, LocalTime oraInizio) throws Exception {
        String sql = "DELETE FROM lezione WHERE insegnamento = ? AND giorno_settimana = ? AND ora_inizio = ?;";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql)) {
            query.setString(1, insegnamento);
            query.setString (2, giornoSettimana);
            query.setTime(3, Time.valueOf(oraInizio));
            query.executeUpdate();
        } catch (SQLException e) {throw new Exception("Errore: si è verificato un errore nel database!");}
    }

    /**
     * Recupera l'elenco delle lezioni programmate per un docente specifico
     * Il metodo esegue una {@code JOIN} tra la tabella {@link model.Lezione} e {@link model.Insegnamento} per filtrare le lezioni in base al {@link model.Docente} responsabile identificato tramite la sua email
     * @param email L'indirizzo email del {@link model.Docente} di cui si vogliono visualizzare le lezioni
     * @return Un {@link java.sql.ResultSet} contenente i dati delle lezioni trovate
     * @throws Exception Se si verifica un errore durante l'interazione con il database
     */

    public ResultSet getLezioni(String email) throws Exception {
        String sql = "SELECT giorno_settimana, ora_inizio, ora_fine, aula, insegnamento FROM lezione JOIN insegnamento ON lezione.insegnamento LIKE insegnamento.nome where insegnamento.email_docente LIKE ? ORDER BY ora_inizio;";
        ResultSet rs;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, email);
            rs = query.executeQuery(sql);
        } catch (SQLException e) {throw new Exception("Si è verificato un errore nel database.");}
        return rs;

    }

    /**
     * Recupera l'elenco delle lezioni programmate per un determinato anno accademico effettuando una {@code JOIN} tra {@link model.Lezione} e {@link model.Insegnamento}, filtrando i risultati in base all'anno accademico specificato e restituendo le lezioni ordinate per {@code ora_inizio}
     * @param anno L'anno accademico di riferimento
     * @return Un {@link java.sql.ResultSet} contenente il set di risultati filtrato
     * @throws Exception Se si verifica un errore durante l'esecuzione della query o l'accesso al database
     */

    public ResultSet getLezioni(int anno) throws Exception {
        String sql = "SELECT giorno_settimana, ora_inizio, ora_fine, aula, insegnamento FROM lezione JOIN insegnamento ON lezione.insegnamento LIKE insegnamento.nome where insegnamento.anno_accademico = ? ORDER BY ora_inizio;";
        ResultSet rs;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setInt(1, anno);
            rs = query.executeQuery(sql);
        } catch (SQLException e) {throw new Exception("Si è verificato un errore nel database.");}
        return rs;

    }


    public ResultSet getLezioni() throws Exception {
        String sql = "SELECT * FROM lezione ORDER BY ora_inizio;";
        ResultSet rs;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql)){
            rs = query.executeQuery();
        } catch (SQLException e) {
            throw new Exception("Si è verificato un errore nel database.");
        }
        return rs;
    }
}
