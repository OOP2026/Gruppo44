package implementazionedao;

import dao.VariazioneDAO;
import database_connection.ConnessioneDatabase;
import common.GiornoSettimana;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class VariazionePostgresDAO implements VariazioneDAO {
    private final Connection connessioneDatabase;

    public VariazionePostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Aggiunge una nuova {@link model.Variazione} di una lezione nel database.
     * <p>
     * Il metodo registra le specifiche di una lezione modificata, mantenendo traccia
     * sia dei parametri originali che di quelli nuovi. Viene effettuata la conversione
     * automatica del {@code giornoSettimana} a partire dalla {@link LocalDate} originale
     * per garantire la conformità con il tipo enumerato definito nello schema del database.
     *
     * @param insegnamento Il nome dell'insegnamento relativo.
     * @param dataOriginale La data originale della lezione relativa.
     * @param nuovaData La nuova data della lezione modificata.
     * @param oraInizioOriginale L'orario di inizio originale della lezione.
     * @param nuovaOraInizio Il nuovo orario di inizio della lezione modificata.
     * @param nuovaOraFine Il nuovo orario di fine della lezione modificata.
     * @param aula L'aula in cui si terrà la lezione variata.
     * @throws SQLException In caso di errori durante l'esecuzione dell'inserimento.
     */
    public void creaVariazione(String insegnamento, LocalDate dataOriginale, LocalDate nuovaData, LocalTime oraInizioOriginale, LocalTime nuovaOraInizio, LocalTime nuovaOraFine, String aula) throws SQLException{
        String sql = "INSERT INTO variazione(insegnamento, data_originale, nuova_data, ora_inizio_originale, ora_inizio, ora_fine, giorno_settimana, aula) VALUES (?,?,?,?,?,?,?::giorno_settimana,?);";
        String giornoSettimana = GiornoSettimana.values()[dataOriginale.getDayOfWeek().getValue() - 1].name();

        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, insegnamento);
            query.setDate(2, Date.valueOf(dataOriginale));
            query.setDate(3, Date.valueOf(nuovaData));
            query.setTime(4, Time.valueOf(oraInizioOriginale));
            query.setTime(5, Time.valueOf(nuovaOraInizio));
            query.setTime(6, Time.valueOf(nuovaOraFine));
            query.setString(7, giornoSettimana);
            query.setString(8, aula);
            query.executeUpdate();
        } catch (SQLException e) {throw new SQLException("Errore: impossibile registrare la variazione nel database!");}
    }


    /**
     * Elimina una specifica {@link model.Variazione} di lezione dal database.
     * <p>
     * Il metodo rimuove il record corrispondente dalla tabella {@code variazione}
     * utilizzando come criteri di filtro l'insegnamento di riferimento, la data
     * originale e l'orario di inizio originale.
     *
     * @param insegnamento Il nome dell'insegnamento relativo.
     * @param dataOriginale La data originale associata alla variazione.
     * @param oraInizioOriginale L'orario di inizio originale della lezione.
     * @throws SQLException In caso di errori durante l'esecuzione della query di eliminazione.
     */
    public void eliminaVariazione(String insegnamento, LocalDate dataOriginale, LocalTime oraInizioOriginale ) throws SQLException {
        String sql = "DELETE FROM variazione WHERE insegnamento = ? AND data_originale = ? AND ora_inizio_originale = ?;";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql)){
            query.setString(1, insegnamento);
            query.setDate(2, Date.valueOf(dataOriginale));
            query.setTime(3, Time.valueOf(oraInizioOriginale));
            query.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Errore: impossibile eliminare la variazione dal database!");
        }
    }

    /**
     * Restituisce le variazioni relative alle lezioni programmate per uno specifico anno accademico.
     * <p>
     * Il metodo esegue una {@code JOIN} tra la tabella {@code variazione} e {@code insegnamento},
     * filtrando le variazioni in base all'anno accademico associato all'insegnamento.
     * I risultati vengono caricati in un {@link CachedRowSet} per consentire la consultazione
     * dei dati in modalità disconnessa.
     *
     * @param anno L'anno accademico di riferimento per il filtro.
     * @return Un {@link ResultSet} (nello specifico un {@link CachedRowSet}) contenente il set di risultati delle variazioni trovate.
     * @throws SQLException In caso di errori durante l'esecuzione della query o
     * durante il popolamento del set di risultati.
     */
    public ResultSet getVariazioni(int anno) throws SQLException{
        String sql = "SELECT insegnamento, data_originale, nuova_data, ora_inizio_originale, ora_inizio, ora_fine, giorno_settimana, aula, nome, numerocfu, anno_accademico, email_docente FROM variazione JOIN insegnamento ON variazione.insegnamento LIKE insegnamento.nome WHERE insegnamento.anno_accademico = ?;";
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setInt(1, anno);
            ResultSet rs = query.executeQuery();
            crs.populate(rs);
        } catch(SQLException e){crs.close(); throw new SQLException("Errore: impossibile recuperare le variazioni per l'anno accademico indicato!");}
        return crs;
    }

    /**
     * Restituisce l'elenco delle variazioni relative alle lezioni di uno specifico docente.
     * <p>
     * Il metodo esegue una {@code JOIN} tra la tabella {@code variazione} e {@code insegnamento},
     * filtrando i risultati in base all'email del docente responsabile. I dati vengono
     * caricati in un {@link CachedRowSet} per consentire la consultazione in modalità
     * disconnessa dal database.
     *
     * @param email L'email del docente di riferimento.
     * @return Un {@link ResultSet} (nello specifico un {@link CachedRowSet}) contenente il set di risultati delle variazioni trovate.
     * @throws SQLException In caso di errori durante l'esecuzione della query o
     * il popolamento del set di risultati.
     */
    public ResultSet getVariazioni(String email) throws SQLException{
        String sql = "SELECT insegnamento, data_originale, nuova_data, ora_inizio_originale, ora_inizio, ora_fine, giorno_settimana, aula, nome, numerocfu, anno_accademico, email_docente FROM variazione JOIN insegnamento ON variazione.insegnamento LIKE insegnamento.nome WHERE insegnamento.email_docente LIKE ?;";
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, email);
            ResultSet rs = query.executeQuery();
            crs.populate(rs);
        } catch(SQLException e){crs.close(); throw new SQLException("Errore: impossibile recuperare le variazioni per il docente indicato!");
        }
        return crs;
    }
}
