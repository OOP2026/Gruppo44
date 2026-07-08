package dao;


import java.sql.ResultSet;

public interface VariazioneDAO {

    void creaVariazione(String insegnamento, String dataOriginale, String nuovaData, String oraInizioOriginale, String nuovaOraInizio, String nuovaOraFine, String aula) throws Exception;
    ResultSet getVariazioni(int anno) throws Exception;
    ResultSet getVariazioni(String email) throws Exception;
}
