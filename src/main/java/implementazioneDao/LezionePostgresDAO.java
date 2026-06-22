package implementazioneDao;

import dao.LezioneDAO;
import database_connection.ConnessioneDatabase;

import java.sql.*;
import java.time.LocalTime;

public class LezionePostgresDAO implements LezioneDAO {

    private Connection connessioneDatabase;

    public LezionePostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void creaLezioneDB(String giornoSettimana, LocalTime oraInizio, LocalTime oraFine, String aula, String insegnamento) throws Exception{
        String sql = "INSERT INTO lezione(giorno_settimana, ora_inizio, ora_fine, aula, insegnamento) VALUES (?,?,?,?,?);";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, giornoSettimana);
            query.setTime(2, Time.valueOf(oraInizio));
            query.setTime(3, Time.valueOf(oraFine));
            query.setString(4, aula);
            query.setString(5, insegnamento);
            query.executeUpdate();
        } catch (SQLException e) {throw new Exception("Lezione non valida.");}
    }

    @Override
    public ResultSet getLezioniDB(String email) throws Exception {
        String sql = "SELECT giorno_settimana, ora_inizio, ora_fine, aula, insegnamento FROM lezione JOIN insegnamento ON lezione.insegnamento LIKE insegnamento.nome where insegnamento.email_docente LIKE ? ORDER BY ora_inizio;";
        ResultSet rs;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, email);
            rs = query.executeQuery(sql);
        } catch (SQLException e) {throw new Exception("Si è verificato un errore nel database.");}
        return rs;

    }
    @Override
    public ResultSet getLezioniDB(int anno) throws Exception {
        String sql = "SELECT giorno_settimana, ora_inizio, ora_fine, aula, insegnamento FROM lezione JOIN insegnamento ON lezione.insegnamento LIKE insegnamento.nome where insegnamento.anno_accademico = ? ORDER BY ora_inizio;";
        ResultSet rs;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setInt(1, anno);
            rs = query.executeQuery(sql);
        } catch (SQLException e) {throw new Exception("Si è verificato un errore nel database.");}
        return rs;

    }
}
