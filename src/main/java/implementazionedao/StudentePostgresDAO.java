package implementazionedao;

import dao.StudenteDAO;
import database_connection.ConnessioneDatabase;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

public class StudentePostgresDAO extends UtentePostgresDAO implements StudenteDAO {

    private final Connection connessioneDatabase;

    public StudentePostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Effettua il login di un {@link model.Studente} verificando le credenziali nel database
     * @param email L'indirizzo email dello studente
     * @param password La password associata all'account dello studente
     * @return Un oggetto {@link ResultSet} contenente i dati dello studente se l'autenticazione ha successo
     * @throws SQLException Se le credenziali sono errate, se si verifica un errore di connessione o se non viene trovato alcun record corrispondente
     */
    public ResultSet login(String email, String password) throws SQLException
    {
        String sql = "SELECT nome, cognome, email, password, matricola, anno_accademico FROM studente WHERE email LIKE ? AND password LIKE ?";
        return super.login(sql, email,password);
    }

    /**
     * Inserisce un nuovo {@link model.Studente} nel database con i dati forniti
     * @param nome Il nome dello studente
     * @param cognome Il cognome dello studente
     * @param email L'indirizzo email dello studente
     * @param password La password per l'account dello studente
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query o se l'inserimento nel database non va a buon fine
     */
    public void creaStudente(String nome, String cognome, String email, String password, String matricola, int anno) throws SQLException
    {
        String sql = "INSERT INTO studente(nome, cognome, email, password, matricola, anno_accademico) VALUES(?,?,?,?,?,?);";
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, nome);
            query.setString(2, cognome);
            query.setString(3, email);
            query.setString(4, password);
            query.setString(5, matricola);
            query.setInt(6, anno);
            query.executeUpdate();
        } catch (SQLException e){throw new SQLException("Errore: registrazione fallita.");}


    }

    public void eliminaStudente(String email) throws SQLException {
        String sql = "DELETE FROM studente WHERE email = ?;";
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql)) {
            query.setString(1, email);
            query.executeUpdate();
        } catch (SQLException e){throw new SQLException("Errore: si è verificato un errore nel database!");}
    }

    /**
     * Recupera l'anno accademico di iscrizione di uno specifico {@link model.Studente} effettuando una ricerca nella tabella {@code studente} attraverso l'indirizzo email
     * @param email L'indirizzo email del {@link model.Studente} di cui si vuole ottenere l'anno
     * @return Un {@link java.sql.ResultSet} contenente l'anno accademico associato allo studente
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query o l'accesso al database
     */
    public ResultSet getAnnoStudente(String email) throws  SQLException
    {
        String sql = "SELECT anno_accademico FROM studente WHERE email LIKE ?";
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, email);
            ResultSet rs = query.executeQuery();
            crs.populate(rs);
        }
        catch (SQLException e){crs.close(); throw new SQLException("Si è verificato un errore!");}
        return crs;
    }
}
