package implementazionedao;
import dao.LezioneDAO;
import database_connection.ConnessioneDatabase;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
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
     * @throws SQLException Se i dati inseriti non sono validi o se si verifica un errore di persistenza nel database
     */


    public void creaLezione(String giornoSettimana, LocalTime oraInizio, LocalTime oraFine, String aula, String insegnamento) throws SQLException{
        String sql = "INSERT INTO lezione(giorno_settimana, ora_inizio, ora_fine, aula, insegnamento) VALUES (?::giorno_settimana,?,?,?,?);";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, giornoSettimana);
            query.setTime(2, Time.valueOf(oraInizio));
            query.setTime(3, Time.valueOf(oraFine));
            query.setString(4, aula);
            query.setString(5, insegnamento);
            query.executeUpdate();
        }
    }

    public void eliminaLezioniInsegnamento(String insegnamento) throws SQLException {
        String sql = "DELETE FROM lezione WHERE insegnamento = ? ;";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql)) {
            query.setString(1, insegnamento);
            query.executeUpdate();
        } catch (SQLException e) {throw new SQLException("Errore: si è verificato un errore nel database!");}
    }

    /**
     * Recupera l'elenco delle lezioni programmate per un docente specifico
     * Il metodo esegue una {@code JOIN} tra la tabella {@link model.Lezione} e {@link model.Insegnamento} per filtrare le lezioni in base al {@link model.Docente} responsabile identificato tramite la sua email
     * @param email L'indirizzo email del {@link model.Docente} di cui si vogliono visualizzare le lezioni
     * @return Un {@link java.sql.ResultSet} contenente i dati delle lezioni trovate
     * @throws SQLException Se si verifica un errore durante l'interazione con il database
     */

    public ResultSet getLezioni(String email) throws SQLException {
        String sql = "SELECT giorno_settimana, ora_inizio, ora_fine, aula, insegnamento FROM lezione JOIN insegnamento ON lezione.insegnamento LIKE insegnamento.nome where insegnamento.email_docente LIKE ? ORDER BY ora_inizio;";
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, email);
            ResultSet rs = query.executeQuery();
            crs.populate(rs);
        } catch (SQLException e) {crs.close(); throw e;}
        return crs;
    }

    /**
     * Recupera l'elenco delle lezioni programmate per un determinato anno accademico effettuando una {@code JOIN} tra {@link model.Lezione} e {@link model.Insegnamento}, filtrando i risultati in base all'anno accademico specificato e restituendo le lezioni ordinate per {@code ora_inizio}
     * @param anno L'anno accademico di riferimento
     * @return Un {@link java.sql.ResultSet} contenente il set di risultati filtrato
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query o l'accesso al database
     */

    public ResultSet getLezioni(int anno) throws SQLException {
        String sql = "SELECT giorno_settimana, ora_inizio, ora_fine, aula, insegnamento FROM lezione JOIN insegnamento ON lezione.insegnamento LIKE insegnamento.nome where insegnamento.anno_accademico = ? ORDER BY ora_inizio;";
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setInt(1, anno);
            ResultSet rs = query.executeQuery();
            crs.populate(rs);
        }
        catch (SQLException e) {crs.close();throw new SQLException("Si è verificato un errore nel database.");}
        return crs;

    }


    public ResultSet getLezioni() throws SQLException {
        String sql = "SELECT giorno_settimana, ora_inizio, ora_fine, aula, insegnamento FROM lezione ORDER BY ora_inizio;";
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql)){
            ResultSet rs = query.executeQuery();
            crs.populate(rs);
        } catch (SQLException e) {
            crs.close();
            throw e;
        }
        return crs;
    }
}
