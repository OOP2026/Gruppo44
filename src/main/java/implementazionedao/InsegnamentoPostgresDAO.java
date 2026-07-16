package implementazionedao;
import dao.InsegnamentoDAO;
import database_connection.ConnessioneDatabase;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;


public class InsegnamentoPostgresDAO implements InsegnamentoDAO {
    private final Connection connessioneDatabase;

    public InsegnamentoPostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crea un nuovo {@link model.Insegnamento} associandolo a un docente tramite la sua email e lo memorizza nel database.
     * <p>
     * Il metodo esegue l'inserimento di un record nella tabella {@code insegnamento},
     * collegando l'insegnamento al docente responsabile tramite la sua email (chiave esterna).
     * L'operazione utilizza una {@link PreparedStatement} per garantire la massima
     * sicurezza contro le SQL Injection.
     *
     * @param nome Il nome dell'insegnamento.
     * @param numeroCFU  Il numero di crediti formativi universitari assegnati all'insegnamento.
     * @param anno L'anno accademico di riferimento per l'insegnamento.
     * @param email L'indirizzo email del {@link model.Docente} responsabile dell'insegnamento.
     * @throws SQLException In caso di errori di comunicazione, violazione di vincoli
     * di chiave esterna o fallimento nell'esecuzione della query.
     */
    public void creaInsegnamento(String nome, int numeroCFU, int anno, String email) throws SQLException {
        String sql = "INSERT INTO insegnamento (nome, numerocfu, anno_accademico, email_docente) VALUES (?, ?, ?, ?);";
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, nome);
            query.setInt(2, numeroCFU);
            query.setInt(3, anno);
            query.setString(4, email);
            query.executeUpdate();

        } catch (SQLException e) {throw new SQLException("Errore: impossibile aggiungere l'insegnamento al database!");
       }
    }

    /**
     * Recupera tutti gli insegnamenti associati a uno specifico {@link model.Docente} utilizzando l'email del docente fornita.
     * <p>
     * Il metodo esegue una query di selezione sulla tabella {@code insegnamento} filtrando
     * i risultati per l'email del docente fornita. I dati vengono estratti e caricati
     * in un {@link CachedRowSet}, permettendo di restituire un set di risultati
     * disconnesso dal database, evitando che la connessione debba rimanere aperta dopo
     * la chiusura del blocco del metodo.
     *
     * @param emailDocente L'email del {@link model.Docente} di cui si vuole visualizzare l'insegnamento.
     * @return Un {@link ResultSet} (nello specifico un {@link CachedRowSet}) contenente l'elenco d
     * egli oggetti {@link model.Insegnamento} trovati.
     * @throws SQLException in caso di errori durante l'interazione con il database o
     * durante il popolamento della struttura dati.
     */
    public ResultSet getInsegnamentiDocente(String emailDocente) throws SQLException {
        String sql = "SELECT nome, numerocfu, anno_accademico, email_docente FROM insegnamento WHERE email_docente = ?;";
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql)) {
            query.setString(1, emailDocente);
            ResultSet rs = query.executeQuery();
            crs.populate(rs);
        } catch (SQLException e) {
            crs.close();
            throw new SQLException("Errore: impossibile recuperare gli insegnamenti per il docente specificato!");
        }
        return crs;
    }


    /**
     * Recupera l'elenco completo di tutti gli insegnamenti presenti nel database.
     * <p>
     * Il metodo esegue una query di selezione sull'intera tabella {@code insegnamento}
     * e popola un {@link CachedRowSet}. Questo approccio permette di restituire un
     * set di risultati disconnesso, garantendo che le risorse di connessione al
     * database vengano rilasciate tempestivamente dopo il popolamento della struttura
     * dati in memoria.
     *
     * @return un {@link ResultSet} (nello specifico un {@link CachedRowSet}) contenente
     * tutti i record degli insegnamenti.
     * @throws SQLException In caso di errori durante l'interazione con il database o
     * il popolamento del set di risultati.
     */
    public ResultSet getInsegnamenti() throws SQLException {
        String sql = "SELECT nome, numerocfu, anno_accademico, email_docente FROM insegnamento;";

        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql)) {
            ResultSet rs = query.executeQuery();
            crs.populate(rs);
        } catch (SQLException e) {
            crs.close();
            throw new SQLException("Errore: impossibile recuperare l'elenco degli insegnamenti dal database!");
        }
        return crs;
    }


    /**
     * Rimuove un {@link model.Insegnamento} dal database in base al suo nome.
     * <p>
     * Il metodo esegue una operazione di eliminazione (DELETE) sulla tabella {@code insegnamento},
     * utilizzando il nome dell'insegnamento come filtro. L'uso di una {@link PreparedStatement}
     * garantisce l'esecuzione sicura della query contro le SQL Injection.
     * In caso di errore durante l'esecuzione, viene sollevata una {@link SQLException} informativa.
     *
     * @param nomeInsegnamento Il nome dell'insegnamento da rimuovere dal sistema.
     * @throws SQLException In caso di errori durante l'interazione con il database.
     */
    public void eliminaInsegnamento(String nomeInsegnamento) throws SQLException {
        String sql = "DELETE FROM insegnamento WHERE nome LIKE ?;";

        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql)) {
            query.setString(1, nomeInsegnamento);
            query.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Errore: impossibile eliminare l'insegnamento dal database!");
        }
    }


    /**
     * Aggiorna i dettagli di un {@link model.Insegnamento} esistente nel database.
     * <p>
     * Il metodo modifica i record nella tabella {@code insegnamento} corrispondenti al
     * nome fornito. Vengono aggiornati il numero di crediti (CFU), l'anno accademico
     * e l'email del docente responsabile. L'operazione utilizza una
     * {@link PreparedStatement} per garantire la sicurezza contro le SQL Injection.
     *
     * @param nomeInsegnamento Il nome dell'insegnamento da aggiornare (chiave di ricerca).
     * @param numeroCFU Il nuovo numero di crediti formativi da assegnare all'insegnamento.
     * @param anno Il nuovo anno accademico di riferimento da assegnare all'insegnamento.
     * @param emailDocente L'email del nuovo docente responsabile associato all'insegnamento.
     * @throws SQLException In caso di errori di comunicazione con il database o
     * violazione di vincoli referenziali.
     */
    public void aggiornaInsegnamento (String nomeInsegnamento, int numeroCFU, int anno, String emailDocente) throws SQLException {
        String sql = "UPDATE  insegnamento SET numeroCFU = ?, anno_accademico = ?, email_docente = ? WHERE nome = ?;";
         try(PreparedStatement query = connessioneDatabase.prepareStatement(sql)) {
             query.setInt(1,  numeroCFU);
             query.setInt (2, anno);
             query.setString(3, emailDocente);
             query.setString(4, nomeInsegnamento);
             query.executeUpdate();
         } catch (SQLException e) {
             throw new SQLException("Errore: impossibile aggiornare l'insegnamento nel database!");
         }
    }
}
