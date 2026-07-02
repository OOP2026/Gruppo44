package implementazioneDao;
import dao.InsegnamentoDAO;
import database_connection.ConnessioneDatabase;

import java.sql.*;

public class InsegnamentoPostgresDAO implements InsegnamentoDAO {
    private Connection connessioneDatabase;

    public InsegnamentoPostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

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

}
