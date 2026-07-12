package dao;
import java.sql.ResultSet;
import java.sql.SQLException;


public interface InsegnamentoDAO {
    void creaInsegnamento(String nome, int numeroCFU, int anno, String email ) throws SQLException;
    ResultSet getInsegnamentiDocente(String emailDocente) throws SQLException;
    ResultSet getInsegnamenti() throws SQLException;
    void eliminaInsegnamento(String nomeInsegnamento) throws SQLException;
    void aggiornaInsegnamento (String nomeInsegnamento, int numeroCFU, int anno, String emailDocente) throws SQLException;
}
