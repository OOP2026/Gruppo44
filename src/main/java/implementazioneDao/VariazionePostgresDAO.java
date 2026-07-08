package implementazioneDao;

import dao.VariazioneDAO;
import database_connection.ConnessioneDatabase;

import java.sql.*;
import java.time.LocalDate;

public class VariazionePostgresDAO implements VariazioneDAO {
    private Connection connessioneDatabase;

    public VariazionePostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aggiunge una variazione di una lezione al database.
     * @param insegnamento Il nome dell'insegnamento relativo.
     * @param dataOriginale La data(non settimanale) originale della lezione relativa.
     * @param nuovaData La nuova data della lezione modificata.
     * @param oraInizioOriginale L'orario di inizio originale della lezione.
     * @param nuovaOraInizio Il nuovo orario di inizio della lezione modificata.
     * @param nuovaOraFine Il nuovo orario di fine della lezione modificata.
     * @throws Exception In caso di errori nel database.
     */
    public void creaVariazione(String insegnamento, String dataOriginale, String nuovaData, String oraInizioOriginale, String nuovaOraInizio, String nuovaOraFine, String aula) throws Exception{
        String sql = "INSERT INTO variazione(insegnamento, data_originale, nuova_data, ora_inizio_originale, ora_inizio, ora_fine, giorno_settimana, aula) VALUES (?,?,?,?,?,?,?,?);";
        int giornoSettimana = LocalDate.parse(dataOriginale).getDayOfWeek().getValue() - 1;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, insegnamento);
            query.setDate(2, Date.valueOf(dataOriginale));
            query.setDate(3, Date.valueOf(nuovaData));
            query.setTime(4, Time.valueOf(oraInizioOriginale));
            query.setTime(5, Time.valueOf(nuovaOraInizio));
            query.setTime(6, Time.valueOf(nuovaOraFine));
            query.setInt(7, giornoSettimana);
            query.setString(8, aula);
            query.executeUpdate();
        } catch (SQLException e) {throw new Exception("Errore nel database.");}
    }

    /**
     * Restituisce le variazioni relative alle lezioni di un anno accademico.
     * @param anno L'anno accademico delle cui lezioni si vogliono recuperare le variazioni.
     * @return ResultSet contenente i dati delle lezioni selezionate.
     * @throws Exception In caso di errori nel database.
     */
    public ResultSet getVariazioni(int anno) throws Exception{
        String sql = "SELECT * FROM variazione JOIN insegnamento ON variazione.insegnamento LIKE insegnamento.nome WHERE insegnamento.anno_accademico = ?;";
        ResultSet rs;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setInt(1, anno);
            rs = query.executeQuery(sql);
        } catch(SQLException e){throw new Exception("Errore nel database.");}
        return rs;
    }

    /**
     * Restituisce le variazioni relative alle lezioni di un docente.
     * @param email L'email del docente relativo.
     * @return ResultSet contenente i dati delle lezioni selezionate.
     * @throws Exception In caso di errori nel database.
     */
    public ResultSet getVariazioni(String email) throws Exception{
        String sql = "SELECT * FROM variazione JOIN insegnamento ON variazione.insegnamento LIKE insegnamento.nome WHERE insegnamento.email_docente LIKE ?;";
        ResultSet rs;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, email);
            rs = query.executeQuery(sql);
        } catch(SQLException e){throw new Exception("Errore nel database.");}
        return rs;
    }
}
