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


}
