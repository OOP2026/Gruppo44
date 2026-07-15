package implementazionedao;
import dao.InsegnamentoDAO;
import database_connection.ConnessioneDatabase;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;


public class InsegnamentoPostgresDAO implements InsegnamentoDAO {
    private final Connection connessioneDatabase;

    public InsegnamentoPostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crea un nuovo {@link model.Insegnamento} associandolo a un docente tramite la sua email e lo memorizza nel database
     * @param nome Il nome dell'insegnamento
     * @param numeroCFU  Il numero di crediti formativi universitari assegnati all'insegnamento
     * @param anno L'anno accademico di riferimento per l'insegnamento
     * @param email L'indirizzo email del {@link model.Docente} responsabile dell'insegnamento
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query o se l'inserimento nel database non va a buon fine
     */
    public void creaInsegnamento(String nome, int numeroCFU, int anno, String email) throws SQLException {
        String sql = "INSERT INTO insegnamento (nome, numerocfu, anno_accademico, email_docente) VALUES (?, ?, ?, ?);";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, nome);
            query.setInt(2, numeroCFU);
            query.setInt(3, anno);
            query.setString(4, email);
            query.executeUpdate();

        } catch (SQLException e) {throw new SQLException("Non è possibile aggiungere insegnamento!");
    }
    }

    /**
     * Recupera tutti gli insegnamenti associati a uno specifico {@link model.Docente} utilizzando l'email del docente fornita
     * * @param emailDocente L'email del {@link model.Docente} di cui si vuole visualizzare l'insegnamento
     * @return Un oggetto {@link java.sql.ResultSet} contenente l'elenco degli oggetti {@link model.Insegnamento} trovati
     * @throws SQLException Se si verifica un errore durante la comunicazione con il database
     */
    public ResultSet getInsegnamentiDocente(String emailDocente) throws SQLException {
        String sql = "SELECT nome, numerocfu, anno_accademico, email_docente FROM insegnamento WHERE email_docente = ?;";
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql)) {
            query.setString(1, emailDocente);
            ResultSet rs = query.executeQuery();
            crs.populate(rs);
        } catch (SQLException e) {
            crs.close();
            throw new SQLException("Si è verificato un errore nel recupero degli insegnamenti.");
        }
        return crs;
    }

    /**
     *
     */

    public ResultSet getInsegnamenti() throws SQLException {
        String sql = "SELECT nome, numerocfu, anno_accademico, email_docente FROM insegnamento;";

        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql)) {
            ResultSet rs = query.executeQuery();
            crs.populate(rs);
        } catch (SQLException e) {
            crs.close();
            throw new SQLException("Si è verificato un errore nel database.");
        }
        return crs;
    }

    public void eliminaInsegnamento(String nomeInsegnamento) throws SQLException {
        String sql = "DELETE FROM insegnamento WHERE nome LIKE ?;";

        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql)) {
            query.setString(1, nomeInsegnamento);
            query.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Si è verificato un errore nel database.");
        }
    }

    /**
     *
     */
    public void aggiornaInsegnamento (String nomeInsegnamento, int numeroCFU, int anno, String emailDocente) throws SQLException {
        String sql = "UPDATE  insegnamento SET numeroCFU = ?, anno_accademico = ?, email_docente = ? WHERE nome = ?;";
         try(PreparedStatement query = connessioneDatabase.prepareStatement(sql)) {
             query.setInt(1,  numeroCFU);
             query.setInt (2, anno);
             query.setString(3, emailDocente);
             query.setString(4, nomeInsegnamento);
             query.executeUpdate();
         } catch (SQLException e) {
             throw new SQLException("Si è verificato un errore nel database.");
         }
    }
}
