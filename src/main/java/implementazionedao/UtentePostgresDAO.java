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
