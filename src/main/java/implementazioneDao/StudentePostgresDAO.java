package implementazioneDao;

import dao.StudenteDAO;
import database_connection.ConnessioneDatabase;

import java.sql.*;

public class StudentePostgresDAO implements StudenteDAO {

    private Connection connessioneDatabase;

    public StudentePostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Effettua il login di un {@link model.Studente} verificando le credenziali nel database
     * @param email L'indirizzo email dello studente
     * @param password La password associata all'account dello studente
     * @return Un oggetto {@link ResultSet} contenente i dati dello studente se l'autentificazione ha successo
     * @throws Exception Se le credenziali sono errate, se si verifica un errore di connessione o se non viene trovato alcun record corrispondente
     */
    public ResultSet login(String email, String password) throws Exception
    {
        String sql = "SELECT * FROM studente WHERE email LIKE ? AND password LIKE ?";
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
     * Inserisce un nuovo {@link model.Studente} nel database con i dati forniti
     * @param nome Il nome dello studente
     * @param cognome Il cognome dello studente
     * @param email L'indirizzo email dello studente
     * @param password La password per l'account dello studente
     * @throws Exception Se si verifica un errore durante l'esecuzione della query o se l'inserimento nel database non va a buon fine
     */
    public void creaStudente(String nome, String cognome, String email, String password, String matricola, int anno) throws Exception
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
        } catch (SQLException e){throw new Exception("Errore: registrazione fallita.");}


    }

    /**
     * Recupera l'anno accademico di iscrizione di uno specifico {@link model.Studente} effettuando una ricerca nella tabella {@code studente} attraverso l'indirizzo email
     * @param email L'indirizzo email del {@link model.Studente} di cui si vuole ottenere l'anno
     * @return Un {@link java.sql.ResultSet} contenente l'anno accademico associato allo studente
     * @throws Exception Se si verifica un errore durante l'esecuzione della query o l'accesso al database
     */
    public ResultSet getAnnoStudente(String email) throws  Exception
    {
        String sql = "SELECT anno_accademico FROM studente WHERE email LIKE ?";
        ResultSet rs;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, email);
            rs = query.executeQuery();
        }
        catch (SQLException e){throw new Exception("Si è verificato un errore!");}
        return rs;
    }
}
