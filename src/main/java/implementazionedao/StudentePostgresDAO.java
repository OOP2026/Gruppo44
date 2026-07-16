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
     * Effettua il login di un {@link model.Studente} verificando le credenziali nel database.
     * <p>
     * Il metodo utilizza una query SQL parametrizzata per cercare un record nella tabella
     * {@code studente} corrispondente all'email e alla password fornite. In caso di
     * successo, restituisce un {@link ResultSet} contenente i dati dello studente.
     *
     * @param email L'indirizzo email dello studente.
     * @param password La password associata all'account dello studente.
     * @return Un {@link ResultSet} contenente i dati dello studente se l'autenticazione ha successo; in caso contrario, il set di risultati sarà vuoto.
     * @throws SQLException Se le credenziali sono errate, se si verifica un errore di connessione o se non viene trovato alcun record corrispondente.
     */
    public ResultSet login(String email, String password) throws SQLException
    {
        String sql = "SELECT nome, cognome, email, password, matricola, anno_accademico FROM studente WHERE email LIKE ? AND password LIKE ?";
        return super.login(sql, email,password);
    }


    /**
     * Inserisce un nuovo {@link model.Studente} nel database con i dati forniti.
     * <p>
     * Il metodo esegue l'inserimento dei dati anagrafici e accademici nella tabella
     * {@code studente}. L'utilizzo di una {@link PreparedStatement} assicura che
     * l'operazione sia protetta contro SQL Injection.
     *
     * @param nome Il nome dello studente.
     * @param cognome Il cognome dello studente.
     * @param email L'indirizzo email dello studente.
     * @param password La password associata all'account dello studente.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query o se l'inserimento nel database non va a buon fine.
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
        } catch (SQLException e){throw new SQLException("Errore: registrazione dello studente fallita!");}


    }


    /**
     * Rimuove un {@link model.Studente} dal database utilizzando il suo indirizzo email.
     * <p>
     * Il metodo esegue una operazione di eliminazione (DELETE) sulla tabella {@code studente},
     * filtrando il record tramite l'email univoca fornita. L'utilizzo di una
     * {@link PreparedStatement} garantisce che la query sia eseguita in modo sicuro
     * e protetto da SQL injection.
     *
     * @param email L'indirizzo email identificativo dello studente da rimuovere.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query o se la cancellazione dal database non va a buon fine.
     */
    public void eliminaStudente(String email) throws SQLException {
        String sql = "DELETE FROM studente WHERE email = ?;";
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql)) {
            query.setString(1, email);
            query.executeUpdate();
        } catch (SQLException e){throw new SQLException("Errore: impossibile eliminare lo studente dal database!");}
    }

    /**
     * Recupera l'anno accademico di iscrizione di uno specifico {@link model.Studente}.
     * <p>
     * Il metodo esegue una query di selezione sulla tabella {@code studente},
     * filtrando il record per l'indirizzo email fornito. Il risultato viene caricato
     * in un {@link CachedRowSet} per consentire l'accesso ai dati in modalità
     * disconnessa, liberando le risorse di connessione al database.
     *
     * @param email L'indirizzo email del {@link model.Studente} di cui si vuole ottenere l'anno.
     * @return Un {@link ResultSet} (nello specifico un {@link CachedRowSet}) contenente l'anno accademico associato allo studente.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query o durante il popolamento della struttura dati.
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
        catch (SQLException e){crs.close(); throw new SQLException("Errore: non è possibile recuperare l'anno accademico dello studente specificato!");}
        return crs;
    }
}
