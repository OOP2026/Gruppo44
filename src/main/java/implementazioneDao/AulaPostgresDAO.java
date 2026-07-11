package implementazioneDao;

import database_connection.ConnessioneDatabase;
import dao.AulaDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AulaPostgresDAO implements AulaDAO{

    private final Connection connessioneDatabase;

    public AulaPostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void creaAula (String nomeAula, int capienzaMassima) throws Exception {
        String sql = "INSERT INTO aula VALUES (?, ?)";
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql)){
            query.setString(1, nomeAula);
            query.setInt(2, capienzaMassima);
            query.executeUpdate();
        } catch (SQLException e){throw new Exception("Errore: si è verificato un errore nel database!");}
    }



    public void eliminaAula (String nomeAula) throws Exception {
        String sql = "DELETE FROM aula WHERE nome_aula = ?";
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql)){
            query.setString(1, nomeAula);
            query.executeUpdate();
        } catch (SQLException e) {throw new Exception("Errore: si è verificato un errore nel database!");}

    }

    public ResultSet getAule() throws Exception {
        String sql = "SELECT * FROM aula";
        ResultSet rs;
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql)){
            rs = query.executeQuery();
        } catch (SQLException e) {throw new Exception("Errore: si è verificato un errore nel database!");}
        return rs;
    }
}
