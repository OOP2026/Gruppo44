package dao;
import java.sql.ResultSet;


public interface InsegnamentoDAO {
    void creaInsegnamento(String nome, int numeroCFU, int anno, String email ) throws Exception;
    ResultSet getInsegnamentiDocente(String emailDocente) throws Exception;
    ResultSet getInsegnamenti() throws Exception;
    void eliminaInsegnamento(String nomeInsegnamento) throws Exception;
    void aggiornaInsegnamento (String nomeInsegnamento, int numeroCFU, int anno, String emailDocente) throws Exception;
}
