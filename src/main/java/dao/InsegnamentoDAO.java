package dao;

import model.Docente;

import java.sql.ResultSet;
import java.util.ArrayList;

public interface InsegnamentoDAO {
    public void creaInsegnamento(String nome, int numeroCFU, int anno, String email ) throws Exception;
    public ResultSet getInsegnamentiDocente(String emailDocente) throws Exception;
}
