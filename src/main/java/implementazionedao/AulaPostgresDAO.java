package implementazionedao;

import database_connection.ConnessioneDatabase;
import dao.AulaDAO;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

public class AulaPostgresDAO implements AulaDAO{

    private final Connection connessioneDatabase;

    public AulaPostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserisce una nuova {@link model.Aula} nel database.
     * <p>
     * Il metodo registra un nuovo record nella tabella {@code aula} utilizzando
     * una {@link PreparedStatement} per garantire la sicurezza contro le
     * SQL Injection.
     *
     * @param nomeAula Il nome identificativo univoco dell'aula.
     * @param capienzaMassima Il numero massimo di posti disponibili.
     * @throws SQLException In caso di errori di comunicazione, fallimenti nell'esecuzione
     * della query o violazioni di integrità referenziale.
     */
    public void creaAula (String nomeAula, int capienzaMassima) throws SQLException {
        String sql = "INSERT INTO aula VALUES (?, ?)";
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql)){
            query.setString(1, nomeAula);
            query.setInt(2, capienzaMassima);
            query.executeUpdate();
        } catch (SQLException e){throw new SQLException("Errore: impossibile registrare la nuova aula nel database!");}
    }


    /**
     * Elimina una {@link model.Aula} dal database.
     * <p>
     * Il metodo rimuove il record corrispondente al {@code nomeAula} specificato
     * dalla tabella {@code aula}. L'operazione utilizza una {@link PreparedStatement}
     * per garantire la sicurezza contro le SQL Injection. In caso di errore durante
     * l'interazione con il database, viene sollevata una {@link SQLException} informativa.
     *
     * @param nomeAula Il nome identificativo dell'aula da eliminare.
     * @throws SQLException In caso di errori di comunicazione, fallimenti nell'esecuzione
     * della query o violazioni di integrità referenziale.
     */
    public void eliminaAula (String nomeAula) throws SQLException {
        String sql = "DELETE FROM aula WHERE nome_aula = ?";
        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql)){
            query.setString(1, nomeAula);
            query.executeUpdate();
        } catch (SQLException e) {throw new SQLException("Errore: si è verificato un errore nel database!");}

    }


    /**
     * Recupera l'elenco completo delle aule presenti nel database.
     * <p>
     * Il metodo esegue una query SQL di selezione sulla tabella {@code aula} e
     * popola un {@link CachedRowSet}. L'utilizzo di un {@link CachedRowSet} permette
     * di restituire un set di risultati disconnesso dal database, evitando che la
     * connessione debba rimanere aperta dopo la chiusura del blocco del metodo.
     * @return Un {@link ResultSet} (nello specifico, un {@link CachedRowSet})
     * contenente i dati di tutte le aule.
     * @throws SQLException In caso di errori durante l'esecuzione della query o
     * durante il popolamento della struttura dati.
     */
    public ResultSet getAule() throws SQLException {
        String sql = "SELECT nome_aula, capienza_massima FROM aula";
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();

        try (PreparedStatement query = connessioneDatabase.prepareStatement(sql)){
            ResultSet rs = query.executeQuery();
            crs.populate(rs);
        } catch (SQLException e) {crs.close(); throw new SQLException("Errore: impossibile recuperare l'elenco delle aule dal database!");}
        return crs;
    }
}
