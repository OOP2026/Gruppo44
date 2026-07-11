package implementazioneDao;

import dao.DocenteDAO;
import database_connection.ConnessioneDatabase;

import java.sql.*;

public class DocentePostgresDAO implements DocenteDAO {

    private final Connection connessioneDatabase;

    public DocentePostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Effettua il login di un {@link model.Docente} verificando le credenziali nel database
     * @param email L'indirizzo email del docente
     * @param password La password associata all'account del docente
     * @return Un oggetto {@link ResultSet} contenente i dati del docente se l'autenticazione ha successo
     * @throws Exception Se le credenziali sono errate, se si verifica un errore di connessione o se non viene trovato alcun record corrispondente
     */
    public ResultSet login(String email, String password) throws Exception
    {
        String sql = "SELECT * FROM docente WHERE email LIKE ? AND password LIKE ?;";
        ResultSet rs;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, email);
            query.setString(2, password);
            rs = query.executeQuery(sql);
        } catch (SQLException e) {throw new Exception("Credenziali errate.");}
        if(rs.next())
        {
            return rs;
        }
        else{throw new Exception("Credenziali errate.");}
    }

    /**
     * Inserisce un nuovo {@link model.Docente} nel database con i dati forniti
     * @param nome Il nome del docente
     * @param cognome Il cognome del docente
     * @param email L'indirizzo email del docente
     * @param password La password per l'account del docente
     * @throws Exception Se si verifica un errore durante l'esecuzione della query o se l'inserimento nel database non va a buon fine
     */
    public void creaDocente(String nome, String cognome, String email, String password) throws Exception
    {
        String sql = "INSERT INTO docente(nome, cognome, email, password) VALUES(?,?,?,?);";
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, nome);
            query.setString(2, cognome);
            query.setString(3, email);
            query.setString(4, password);
            query.executeUpdate();
        } catch (SQLException e){throw new Exception("Errore: registrazione fallita.");}

    }

    public void eliminaDocente(String email) throws Exception {
        String sql = "DELETE FROM docente WHERE email LIKE ?;";
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql)){
            query.setString(1, email);
            query.executeUpdate();
        } catch (SQLException e){throw new Exception("Errore: si è verificato un errore nel database!");}
    }
}
