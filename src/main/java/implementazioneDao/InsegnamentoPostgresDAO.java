package implementazioneDao;
import dao.InsegnamentoDAO;
import database_connection.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;

public class InsegnamentoPostgresDAO implements InsegnamentoDAO {
    private Connection connessioneDatabase;

    public InsegnamentoPostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Crea un nuovo {@link model.Insegnamento} associandolo a un docente tramite la sua email e lo memorizza nel database
     * @param nome Il nome dell'insegnamento
     * @param numeroCFU  Il numero di crediti formativi universitari assegnati all'insegnamento
     * @param anno L'anno accademico di riferimento per l'insegnamento
     * @param email L'indirizzo email del {@link model.Docente} responsabile dell'insegnamento
     * @throws Exception Se si verifica un errore durante l'esecuzione della query o se l'inserimento nel database non va a buon fine
     */
    public void creaInsegnamento(String nome, int numeroCFU, int anno, String email) throws Exception {
        String sql = "INSERT INTO insegnamento (nome, numerocfu, anno_accademico, email_docente) VALUES (?, ?, ?, ?);";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, nome);
            query.setInt(2, numeroCFU);
            query.setInt(3, anno);
            query.setString(4, email);
            query.executeUpdate();

        } catch (SQLException e) {throw new Exception("Non è possibile aggiungere insegnamento!");
    }
    }

    /**
     * Recupera tutti gli insegnamenti associati a uno specifico {@link model.Docente} utilizzando l'email del docente fornita
     * * @param emailDocente L'email del {@link model.Docente} di cui si vuole visualizzare l'insegnamento
     * @return Un oggetto {@link java.sql.ResultSet} contenente l'elenco degli oggetti {@link model.Insegnamento} trovati
     * @throws Exception Se si verifica un errore durante la comunicazione con il database
     */
    public ResultSet getInsegnamentiDocente(String emailDocente) throws Exception {
        String sql = "SELECT * FROM insegnamento WHERE email_docente = ?;";
        ResultSet rs;
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql)) {
            query.setString(1, emailDocente);
            rs = query.executeQuery(sql);
        } catch (SQLException e) {
            throw new Exception("Si è verificato un errore nel database.");
        }
        return rs;
    }

    /**
     *
     */

    public ResultSet getInsegnamenti() throws Exception {
        String sql = "SELECT * FROM insegnamento;";
        ResultSet rs;
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql)) {
            rs = query.executeQuery(sql);
        } catch (SQLException e) {
            throw new Exception("Si è verificato un errore nel database.");
        }
        return rs;
    }
}
