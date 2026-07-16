package implementazionedao;

import dao.DocenteDAO;
import database_connection.ConnessioneDatabase;

import java.sql.*;

public class DocentePostgresDAO extends UtentePostgresDAO implements DocenteDAO{

    private final Connection connessioneDatabase;

    public DocentePostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Effettua il login di un {@link model.Docente} verificando le credenziali nel database.
     * <p>
     * Il metodo utilizza una query SQL parametrizzata per cercare un record nella tabella
     * {@code docente} corrispondente all'email e alla password fornite. In caso di
     * successo, restituisce un {@link ResultSet} contenente i dati del docente.
     *
     * @param email L'indirizzo email del docente.
     * @param password La password associata all'account del docente.
     * @return Un {@link ResultSet} contenente i dati del docente se l'autenticazione ha successo; in caso contrario, il set di risultati sarà vuoto.
     * @throws SQLException Se le credenziali sono errate, se si verifica un errore di connessione o se non viene trovato alcun record corrispondente.
     */
    public ResultSet login(String email, String password) throws SQLException
    {
        String sql = "SELECT nome, cognome, email, password FROM docente WHERE email LIKE ? AND password LIKE ?;";
        return super.login(sql, email,password);
    }

    /**
     * Effettua il login del responsabile del server verificandone le credenziali nel database.
     * <p>
     * Il metodo utilizza una query SQL parametrizzata per cercare un record nella tabella
     * {@code responsabile} corrispondente all'email e alla password fornite. In caso di
     * successo, restituisce un {@link boolean} uguale a vero, altrimenti a falso.
     *
     * @param email L'indirizzo email del docente.
     * @param password La password associata all'account del docente.
     * @return Un {@link boolean} rappresentante l'esito del tentatico di accesso.
     * @throws SQLException Se si verifica un errore di connessione.
     */
    public boolean loginResponsabile(String email, String password) throws SQLException
    {
        String sql = "SELECT email, password FROM docente_responsabile WHERE email LIKE ? AND password LIKE ?;";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, email);
            query.setString(2, password);
            ResultSet rs = query.executeQuery();
            return rs.next();
        }
    }

    /**
     * Inserisce un nuovo {@link model.Docente} nel database con i dati forniti.
     * <p>
     * Il metodo esegue l'inserimento di un nuovo record nella tabella {@code docente}
     * utilizzando una {@link PreparedStatement} per garantire l'integrità dei dati
     * e prevenire L'SQL injection. Se l'operazione di inserimento fallisce, viene
     * sollevata un'eccezione {@link SQLException} con un messaggio descrittivo.
     *
     * @param nome Il nome del docente.
     * @param cognome Il cognome del docente.
     * @param email L'indirizzo email del docente (chiave univoca).
     * @param password La password associata all'account.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query o se l'inserimento nel database non va a buon fine.
     */
    public void creaDocente(String nome, String cognome, String email, String password) throws SQLException
    {
        String sql = "INSERT INTO docente(nome, cognome, email, password) VALUES(?,?,?,?);";
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, nome);
            query.setString(2, cognome);
            query.setString(3, email);
            query.setString(4, password);
            query.executeUpdate();
        } catch (SQLException e){throw new SQLException("Errore: registrazione del docente fallita!");}

    }


    /**
     * Rimuove un {@link model.Docente} dal database in base alla sua email.
     * <p>
     * Il metodo esegue una operazione di eliminazione (DELETE) sulla tabella {@code docente},
     * utilizzando l'email come filtro univoco. L'uso di una {@link PreparedStatement}
     * garantisce l'esecuzione sicura della query, prevenendo potenziali tentativi
     * di manipolazione SQL. In caso di errore durante l'esecuzione, viene sollevata
     * una {@link SQLException} con un messaggio descrittivo.
     *
     * @param email L'indirizzo email del docente da rimuovere dal sistema.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query o se la cancellazione dal database non va a buon fine.
     */
    public void eliminaDocente(String email) throws SQLException {
        String sql = "DELETE FROM docente WHERE email LIKE ?;";
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql)){
            query.setString(1, email);
            query.executeUpdate();
        } catch (SQLException e){throw new SQLException("Errore: impossibile eliminare il docente dal database!");}
    }
}
