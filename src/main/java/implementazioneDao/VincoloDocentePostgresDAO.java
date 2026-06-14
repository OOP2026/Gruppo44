package implementazioneDao;

import dao.VincoloDocenteDAO;
import database_connection.ConnessioneDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class VincoloDocentePostgresDAO implements VincoloDocenteDAO {

    private Connection connessioneDatabase;

    public VincoloDocentePostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void creaVincolo(String email, String giorno, LocalTime oraInizio, LocalTime oraFine) throws Exception
    {
        String sql = "INSERT INTO vincoli_docente(email_docente, giorno, ora_inizio, ora_fine) VALUES(?,?,?,?);";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, email);
            query.setString(2, giorno);
            query.setTime(3, Time.valueOf(oraInizio));
            query.setTime(4, Time.valueOf(oraFine));
            query.executeUpdate();
        } catch (SQLException e) {throw new Exception("Si è verificato un errore nel database.");}
    }

    public ResultSet getVincoli(String email) throws Exception
    {
        String sql = "SELECT * FROM vincoli_docente WHERE email_docente LIKE ?;";
        ResultSet rs;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, email);
            rs = query.executeQuery(sql);
        } catch (SQLException e) {throw new Exception("Si è verificato un errore nel database.");}
        return rs;
    }

    public ResultSet getVincoliR() throws Exception
    {
        String sql = "SELECT * FROM vincoli_docente;";
        ResultSet rs;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            rs = query.executeQuery(sql);
        } catch (SQLException e) {throw new Exception("Si è verificato un errore nel database.");}
        return rs;
    }

}
