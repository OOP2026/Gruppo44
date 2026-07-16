package implementazionedao;
import dao.LezioneDAO;
import database_connection.ConnessioneDatabase;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.time.LocalTime;


public class LezionePostgresDAO implements LezioneDAO {

    private final Connection connessioneDatabase;

    public LezionePostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crea una nuova {@link model.Lezione} e la salva nel database.
     * <p>
     * Il metodo esegue l'inserimento di un nuovo record nella tabella {@code lezione},
     * mappando accuratamente i tipi {@link java.time.LocalTime} in oggetti {@link java.sql.Time}.
     * La query utilizza un cast esplicito (`?::giorno_settimana`) per garantire la
     * compatibilità con il tipo enumerato definito nello schema del database.
     *
     * @param giornoSettimana Il giorno della settimana in cui si svolge la lezione.
     * @param oraInizio L'orario di inizio della lezione, come {@link java.time.LocalTime}
     * @param oraFine L'orario di fine della lezione, come {@link java.time.LocalTime}
     * @param aula Il nome o il codice dell'aula in cui si tiene la lezione.
     * @param insegnamento Il nome dell'{@link model.Insegnamento} a cui la lezione fa riferimento.
     * @throws SQLException In caso di errori di persistenza nel database o di invalidità dei dati inseriti.
     */
    public void creaLezione(String giornoSettimana, LocalTime oraInizio, LocalTime oraFine, String aula, String insegnamento) throws SQLException{
        String sql = "INSERT INTO lezione(giorno_settimana, ora_inizio, ora_fine, aula, insegnamento) VALUES (?::giorno_settimana,?,?,?,?);";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, giornoSettimana);
            query.setTime(2, Time.valueOf(oraInizio));
            query.setTime(3, Time.valueOf(oraFine));
            query.setString(4, aula);
            query.setString(5, insegnamento);
            query.executeUpdate();
        } catch (SQLException e) {throw new SQLException("Errore: impossibile registrare la lezione nel database!");}
    }


    /**
     * Rimuove tutte le lezioni associate a uno specifico {@link model.Insegnamento}.
     * <p>
     * Il metodo esegue una operazione di eliminazione (DELETE) sulla tabella
     * {@code lezione}, filtrando i record in base al nome dell'insegnamento fornito.
     * L'utilizzo di una {@link PreparedStatement} garantisce che la query sia
     * eseguita in modo sicuro e protetto da SQL injection.
     *
     * @param insegnamento Il nome dell'insegnamento di cui eliminare tutte le lezioni associate.
     * @throws SQLException In caso di errori di comunicazione o violazioni di integrità durante la rimozione dei record.
     */
    public void eliminaLezioniInsegnamento(String insegnamento) throws SQLException {
        String sql = "DELETE FROM lezione WHERE insegnamento = ? ;";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql)) {
            query.setString(1, insegnamento);
            query.executeUpdate();
        } catch (SQLException e) {throw new SQLException("Errore: impossibile eliminare le lezioni associate all'insegnamento indicato!");}
    }

    /**
     * Recupera l'elenco delle lezioni programmate per un docente specifico.
     * <p>
     * Il metodo esegue una {@code JOIN} tra la tabella {@code lezione} e {@code insegnamento}
     * per filtrare le lezioni in base al docente responsabile (identificato tramite email).
     * I risultati vengono ordinati per orario di inizio e caricati in un {@link CachedRowSet}
     * per consentire un utilizzo disconnesso dei dati al di fuori dello strato di persistenza.
     *
     * @param email L'indirizzo email del {@link model.Docente} di cui si vogliono visualizzare le lezioni.
     * @return Un {@link ResultSet} (nello specifico un {@link CachedRowSet}) contenente le lezioni trovate.
     * @throws SQLException In caso di errori durante l'esecuzione della query o il popolamento del set di risultati.
     */

    public ResultSet getLezioni(String email) throws SQLException {
        String sql = "SELECT giorno_settimana, ora_inizio, ora_fine, aula, insegnamento FROM lezione JOIN insegnamento ON lezione.insegnamento LIKE insegnamento.nome where insegnamento.email_docente LIKE ? ORDER BY ora_inizio;";
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, email);
            ResultSet rs = query.executeQuery();
            crs.populate(rs);
        } catch (SQLException e) {crs.close(); throw new SQLException("Errore: impossibile recuperare le lezioni programmate per il docente indicato!");}
        return crs;
    }

    /**
     * Recupera l'elenco delle lezioni programmate per un determinato anno accademico.
     * <p>
     * Il metodo esegue una {@code JOIN} tra la tabella {@code lezione} e {@code insegnamento}
     * per filtrare le lezioni in base all'anno accademico di riferimento definito nell'insegnamento.
     * I risultati vengono ordinati cronologicamente per {@code ora_inizio} e restituiti tramite
     * un {@link CachedRowSet}, garantendo l'accesso ai dati anche dopo la chiusura della
     * connessione al database.
     *
     * @param anno L'anno accademico di riferimento per il filtro.
     * @return Un {@link ResultSet} (nello specifico un {@link CachedRowSet}) contenente il set di risultati filtrato e ordinato.
     * @throws SQLException In caso di errori durante l'esecuzione della query o
     *  * nel popolamento della struttura dati.
     */
    public ResultSet getLezioni(int anno) throws SQLException {
        String sql = "SELECT giorno_settimana, ora_inizio, ora_fine, aula, insegnamento FROM lezione JOIN insegnamento ON lezione.insegnamento LIKE insegnamento.nome where insegnamento.anno_accademico = ? ORDER BY ora_inizio;";
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setInt(1, anno);
            ResultSet rs = query.executeQuery();
            crs.populate(rs);
        }
        catch (SQLException e) {crs.close(); throw new SQLException("Errore: impossibile recuperare le lezioni programmate per l'anno accademico indicato!");}
        return crs;

    }


    /**
     * Recupera l'elenco completo di tutte le lezioni programmate nel database.
     * <p>
     * Il metodo esegue una query di selezione sull'intera tabella {@code lezione},
     * ordinando i risultati per {@code ora_inizio} per garantire una visualizzazione
     * cronologica coerente. Il set di risultati viene caricato in un {@link CachedRowSet},
     * permettendo di gestire i dati in modo disconnesso ed efficiente.
     *
     * @return Un {@link ResultSet} (nello specifico un {@link CachedRowSet}) contenente
     * l'intero orario delle lezioni.
     * @throws SQLException In caso di errori durante l'esecuzione della query o
     * il popolamento del set di risultati.
     */
    public ResultSet getLezioni() throws SQLException {
        String sql = "SELECT giorno_settimana, ora_inizio, ora_fine, aula, insegnamento FROM lezione ORDER BY ora_inizio;";
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql)){
            ResultSet rs = query.executeQuery();
            crs.populate(rs);
        } catch (SQLException e) {
            crs.close();
            throw new SQLException("Errore: impossibile recuperare tutte le lezioni programmate!");}
        return crs;
    }
}
