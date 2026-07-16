package implementazionedao;

import dao.UtenteDAO;
import database_connection.ConnessioneDatabase;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtentePostgresDAO implements UtenteDAO {

    private final Connection connessioneDatabase;

    public UtentePostgresDAO() {
        try{
            connessioneDatabase = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Esegue l'autenticazione generica nel sistema utilizzando una query personalizzata.
     * <p>
     * Il metodo prepara la query SQL fornita, esegue il bind dei parametri {@code email}
     * e {@code password} e tenta di popolare un {@link CachedRowSet}. Se l'autenticazione
     * ha successo (ovvero se viene restituito almeno un record), il metodo restituisce
     * il {@code CachedRowSet} contenente i dati dell'utente; in caso contrario, solleva
     * una {@link SQLException}.
     *
     * @param sql La stringa SQL contenente la query di selezione con i placeholder per email e password.
     * @param email L'indirizzo email dell'utente.
     * @param password La password dell'utente.
     * @return Un {@link ResultSet} (nello specifico un {@link CachedRowSet}) con i dati dell'utente autenticato.
     * @throws SQLException Se le credenziali non sono valide, se si verifica un errore di
     * connessione con il database o se non viene trovato alcun record.
     */
    public ResultSet login(String sql, String email, String password) throws SQLException{

        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        try(PreparedStatement query = connessioneDatabase.prepareStatement(sql))
        {
            query.setString(1, email);
            query.setString(2, password);
            ResultSet rs = query.executeQuery();
            crs.populate(rs);
            if(crs.next()){return crs;}
            else{throw new SQLException("Credenziali errate.");}
        } catch (SQLException e) {crs.close(); throw new SQLException("Credenziali errate.");}
    }
}
