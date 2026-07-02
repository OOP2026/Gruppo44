package implementazioneDao;
import dao.RichiestaDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.time.LocalTime;

public class RichiestaPostgresDAO implements RichiestaDAO {
    private Connection connessioneDatabase;

    public RichiestaPostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void aggiungiRichiestaSpostamento(String nomeInsegnamento, String oraOriginale, String giornoOriginale, String giornoRichiesto, String oraInizioRichiesta, String oraFineRichiesta) throws Exception{
        String sql = "INSERT INTO richiesta (insegnamento, ora_inizio_originale, giorno_originale, giorno_richesto, ora_inizio_richiesta, ora_fine_richiesta) VALUES (?, ?, ?, ?, ?, ?);";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, nomeInsegnamento);
            query.setTime(2, Time.valueOf(oraOriginale));
            query.setString(3, giornoOriginale);
            query.setString(4, giornoRichiesto);
            query.setTime(5, Time.valueOf(oraInizioRichiesta));
            query.setTime(6, Time.valueOf(oraFineRichiesta));
            query.executeUpdate();

        } catch (SQLException e) {throw new Exception("Non è possibile aggiungere richiesta!");
        }
    }

    public ResultSet getRegistroRichiesteSpostamento() throws Exception{
        String sql = "SELECT * FROM richiesta";
        ResultSet result;
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql)){
            result = query.executeQuery();
        }
        catch (SQLException e){throw new Exception("Si è verificato un errore!");}
        return result;
    }

    public ResultSet cancellaRichiesta(int id_richiesta) throws Exception {
        String sql= "DELETE FROM richiesta WHERE id_richiesta = ? RETURNING *;";
        ResultSet result;

        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql);)
        {
            query.setInt(1, id_richiesta);
            result = query.executeQuery();
        }
        catch (SQLException e) {throw new Exception("Si è verificato un errore!");};
        return result;
    }


}
