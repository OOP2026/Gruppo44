package implementazioneDao;

import dao.DocenteDAO;
import database_connection.ConnessioneDatabase;

import java.sql.*;

public class DocentePostgresDAO implements DocenteDAO {

    private Connection connessioneDatabase;

    public DocentePostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet login(String email, String password) throws Exception
    {
        String sql = "SELECT * FROM docenti WHERE email LIKE ? AND password LIKE ?;";
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

    public void creaDocente(String nome, String cognome, String email, String password) throws Exception
    {
        String sql = "INSERT INTO docenti(nome, cognome, email, password) VALUES(?,?,?,?);";
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, nome);
            query.setString(2, cognome);
            query.setString(3, email);
            query.setString(4, password);
            query.executeUpdate();
        } catch (SQLException e){throw new Exception("Errore: registrazione fallita.");}

    }

}
