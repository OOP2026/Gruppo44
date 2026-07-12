package implementazionedao;

import database_connection.ConnessioneDatabase;
import dao.AulaDAO;

import java.sql.*;

public class AulaPostgresDAO implements AulaDAO{

    private final Connection connessioneDatabase;

    public AulaPostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void creaAula (String nomeAula, int capienzaMassima) throws SQLException {
        String sql = "INSERT INTO aula VALUES (?, ?)";
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql)){
            query.setString(1, nomeAula);
            query.setInt(2, capienzaMassima);
            query.executeUpdate();
        } catch (SQLException e){throw new SQLException("Errore: si è verificato un errore nel database!");}
    }



    public void eliminaAula (String nomeAula) throws SQLException {
        String sql = "DELETE FROM aula WHERE nome_aula = ?";
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql)){
            query.setString(1, nomeAula);
            query.executeUpdate();
        } catch (SQLException e) {throw new SQLException("Errore: si è verificato un errore nel database!");}

    }

    public ResultSet getAule() throws SQLException {
        String sql = "SELECT nome_aula, capienza_massima FROM aula";
        ResultSet rs;
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql)){
            rs = query.executeQuery();
        } catch (SQLException e) {throw new SQLException("Errore: si è verificato un errore nel database!");}
        return rs;
    }
}
