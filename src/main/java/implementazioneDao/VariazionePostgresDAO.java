package implementazioneDao;

import dao.VariazioneDAO;
import database_connection.ConnessioneDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class VariazionePostgresDAO implements VariazioneDAO {
    private Connection connessioneDatabase;

    public VariazionePostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void creaVariazione(String insegnamento, LocalDate dataOriginale, LocalDate nuovaData, LocalTime oraInizioOriginale, LocalTime nuovaOraInizio, LocalTime nuovaOraFine) throws Exception{
        String sql = "INSERT INTO variazioni(insegnamento, data_originale, nuova_data, ora_inizio_originale, nuova_ora_inizio, nuova_ora_fine) VALUES (?,?,?,?,?,?);";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, insegnamento);
            query.setDate(2, Date.valueOf(dataOriginale));
            query.setDate(3, Date.valueOf(nuovaData));
            query.setTime(4, Time.valueOf(oraInizioOriginale));
            query.setTime(5, Time.valueOf(nuovaOraInizio));
            query.setTime(6, Time.valueOf(nuovaOraFine));
            query.executeUpdate();
        } catch (SQLException e) {throw new Exception("Errore nel database.");}
    }

    public ResultSet getVariazioni(int anno) throws Exception{
        String sql = "SELECT insegnamento, data_originale, nuova_data, ora_inizio_originale, nuova_ora_inizio, nuova_ora_fine FROM variazioni JOIN insegnamenti ON variazioni.insegnamento LIKE insegnamenti.nome WHERE insegnamenti.anno_accademico = ?;";
        ResultSet rs;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setInt(1, anno);
            rs = query.executeQuery(sql);
        } catch(SQLException e){throw new Exception("Errore nel database.");}
        return rs;
    }
}
