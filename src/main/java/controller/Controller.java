package controller;

//import jdk.vm.ci.meta.Local;
import dao.LezioneDAO;
import database_connection.ConnessioneDatabase;
import model.*;
import dao.*;
import implementazioneDao.*;

import java.sql.*;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller {

	//testing
	private HashMap<String, Studente> studentiTest = new HashMap<>();
	private HashMap<String, Insegnamento> insegnamentoTest = new HashMap<>();
	private HashMap<String, Docente> docenteTest = new HashMap<>();

	//diciamo che sono del primo anno
	private ArrayList<Lezione> lezioniPOO;
	private ArrayList<Lezione> lezioniAlgebra;

	//diciamo che sono del secondo anno
	private ArrayList<Lezione> lezioniAnalisi;
	private ArrayList<Lezione> lezioniADE;


	private ArrayList<Richiesta> richiesteSpostamento;

	private HashMap<String, ArrayList<VincoloDocente>> registroVincoliDocenti;

	private Connection connessione;

	public Controller() {

		Testing();

		testingDB();
	}

	private static Controller instance;

	private void testingDB()
	{

	}

	private void Testing(){

		//aggiunge 2 studenti alla hashmap studentiTest
		//studentiTest.put("ric.formisano@studenti.unina.it", new Studente("Riccardo", "Formisano", "ric.formisano@studenti.unina.it", "PasswordR", "DE1000092"));
		studentiTest.put("sar.formicola@studenti.unina.it", new Studente("Sara", "Formicola", "sar.formicola@studenti.unina.it", "PasswordS", "DE1000292", 1));
		studentiTest.put("ele.godano@studenti.unina.it", new Studente("Elena", "Godano", "ele.godano@utenti.studenti.it", "PasswordE", "DE1000092", 2));

		//stampa l'insieme di studenti considerati
		System.out.println("studentiTest: " + studentiTest);

		//crea un singolo studente e lo stampa
		Studente r = new Studente("Riccardo", "Formisano", "ric.formisano@studenti.unina.it", "PasswordR", "DE1000092", 1);
		studentiTest.put(r.email, r);
		System.out.println("Studente: " + r.toString());

		//considera un'email e password di ipotetico input e le stampa
		String currentEmail = "sar.formicola@studenti.unina.it";
		String currentPassword = "PasswordS";

		System.out.println("current email: " + currentEmail);
		System.out.println("current password: " + currentPassword);

		//effettua il login tramite i valori di input e stampa il risultato (vero o falso)
		//boolean isLogined = login(currentEmail, currentPassword);
		//System.out.println("login: " + isLogined);


		//docenti
		docenteTest.put("ptramont@unina.it", new Docente("Porfirio", "Tramontana", "ptramont@unina.it", "docenteTramontana"));
		docenteTest.put("raffaele.scandone@unina.it", new Docente("Raffaele", "Scandone", "raffaele.scandone@unina.it", "docenteScandone"));

		//Crea insegnamenti e rispettive lezioni
		Insegnamento Analisi = new Insegnamento("Analisi I", 6, 1, docenteTest.get("raffaele.scandone@unina.it"));
		Insegnamento ADE = new Insegnamento("Architettura Degli Elaboratori", 6, 1);

		Insegnamento Algebra = new Insegnamento("Algebra I", 9, 2);
		Insegnamento POO = new Insegnamento("Programmazione Object-Oriented", 9, 2, docenteTest.get("ptramont@unina.it"));

		insegnamentoTest.put("Analisi I", Analisi);
		insegnamentoTest.put("Architettura Degli Elaboratori", ADE);
		insegnamentoTest.put("Algebra I", Algebra);
		insegnamentoTest.put("Programmazione Object-Oriented", POO);

		Analisi.lezioni.add(new Lezione(GiornoSettimana.LUNEDI, LocalTime.of(10,0), LocalTime.of(12,0), "A6", "Analisi"));
		Analisi.lezioni.add(new Lezione(GiornoSettimana.MERCOLEDI, LocalTime.of(10,0), LocalTime.of(12,0), "A6" ,"Analisi"));
		Analisi.lezioni.add(new Lezione(GiornoSettimana.VENERDI, LocalTime.of(14,0), LocalTime.of(16,0), "A5", "Analisi"));

		POO.lezioni.add(new Lezione(GiornoSettimana.LUNEDI, LocalTime.of(12,0), LocalTime.of(14,0), "A6", "POO"));
		POO.lezioni.add(new Lezione(GiornoSettimana.MERCOLEDI, LocalTime.of(12,0), LocalTime.of(14,0), "A6", "POO"));
		POO.lezioni.add(new Lezione(GiornoSettimana.MARTEDI, LocalTime.of(12,0), LocalTime.of(14,0), "A5", "POO"));


	

		//crea un array di liste da restituire alla GUI e le stampa

		//ArrayList<String>[] l = getLezioni("ric.formisano@studenti.unina.it");

		//stampa l'array temporaneo
		System.out.println("lezioni mostrate: ");
		//for(ArrayList<String> arr : l){System.out.println(arr.toString());}

    } //fine Testing

	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

	public static void main(String[] args){Controller controller = getInstance();}

	public String[] loginDocente(String email, String password) throws Exception {

		DocenteDAO d = new DocentePostgresDAO();
		ResultSet rs = d.login(email, password);
		return new String[] {rs.getString("nome"), rs.getString("cognome"), rs.getString("email")};

	} // fine Login

	public String[] loginStudente(String email, String password) throws Exception{

		StudenteDAO s = new StudentePostgresDAO();
		ResultSet rs = s.login(email, password);
		return new String[] {rs.getString("nome"), rs.getString("cognome"), rs.getString("email"), rs.getString("matricola"), rs.getString("anno_accademico")};

	}
		 // fine Login


	public ArrayList<String>[] getLezioni(String email) throws Exception{

		LezioneDAO l = new LezionePostgresDAO();

		ResultSet rs = l.getLezioniDB(email);

		return formatLezioni(rs);
	}

	public ArrayList<String>[] getLezioni(int anno) throws Exception{

		LezioneDAO l = new LezionePostgresDAO();

		ResultSet rs= l.getLezioniDB(anno);

		return formatLezioni(rs);
	}

	private ArrayList<String>[] formatLezioni(ResultSet rs) throws Exception{

		ArrayList<String>[] stringArr = new ArrayList[5];

		try {
			while (rs.next()) {
				int giorno = rs.getInt("giorno_settimana");
				String oraInizio = rs.getString("ora_inizio");
				String oraFine = rs.getString("ora_fine");
				String aula = rs.getString("aula");
				String insegnamento = rs.getString("insegnamento");

				stringArr[giorno].add(oraInizio + "\n" + oraFine + "\n" + insegnamento + "\n" + aula);
			}
		} catch (SQLException e) {throw new Exception("Si è verificato un errore nel database.");}
		return stringArr;
	}

	public void aggiungiRichiestaSpostamento(String nomeInsegnamento, String oraOriginale, String giornoOriginale, String giornoRichiesto, String oraInizioRichiesta, String oraFineRichiesta) throws Exception
	{
		RichiestaDAO r = new RichiestaPostgresDAO();
		r.aggiungiRichiestaSpostamento(nomeInsegnamento, oraOriginale,giornoOriginale, giornoRichiesto, oraInizioRichiesta, oraFineRichiesta);

	} // fine InviaRichiesta

	public String[] creaDocente(String nome, String cognome, String email, String password) throws Exception{
		DocenteDAO d = new DocentePostgresDAO();
		d.creaDocente(nome, cognome, email, password);
		return loginDocente(email, password);
	} // fine CreaUtente

	public String[] creaStudente(String nome, String cognome, String email, String password, String matricola, int anno) throws Exception {
		StudenteDAO s = new StudentePostgresDAO();
		s.creaStudente(nome, cognome, email, password, matricola, anno);
		return loginStudente(email, password);
	} // fine CreaUtente

	public void creaLezione(String nomeInsegnamento, String giornoSettimana, LocalTime oraInizio, LocalTime oraFine, String nomeAula) throws Exception {
		LezioneDAO l = new LezionePostgresDAO();
    	l.creaLezioneDB(giornoSettimana, oraInizio, oraFine, nomeAula, nomeInsegnamento);
	} // fine CreaLezione

	public void creaInsegnamento(String nome, int numeroCFU, int anno, String email ) throws Exception {
		InsegnamentoDAO i = new InsegnamentoPostgresDAO();
		i.creaInsegnamento(nome, numeroCFU, anno, email);
	} //fine CreaInsegnamento

	public void creaVariazione(String insegnamento, LocalDate dataOriginale, LocalDate nuovaData, LocalTime oraInizioOriginale, LocalTime nuovaOraInizio, LocalTime nuovaOraFine) throws  Exception {
		VariazioneDAO v = new VariazionePostgresDAO();
		v.creaVariazione(insegnamento, dataOriginale, nuovaData, oraInizioOriginale, nuovaOraInizio, nuovaOraFine );
	} //fine CreaVariazione

	public void aggiungiVincoloDocente(String emailDocente, String giornoSettimana, String oraInizio, String oraFine) throws Exception {
		VincoloDocenteDAO v = new VincoloDocentePostgresDAO();
		v.creaVincolo(emailDocente, giornoSettimana, LocalTime.parse(oraInizio), LocalTime.parse(oraFine));
	} //fine CreaVincolo

	public ArrayList<String> getInsegnamentiDocente(String emailDocente) throws Exception {
		InsegnamentoDAO i = new InsegnamentoPostgresDAO();
		ResultSet rs = i.getInsegnamentiDocente(emailDocente);

		ArrayList<String> insegnamenti = new ArrayList<>();
		while(rs.next()) {
			insegnamenti.add(rs.getString("nome") + ", anno " + rs.getString("anno_accademico") + ", " + rs.getString("numerocfu") + "CFU" );
		}
		return insegnamenti;
	}

	public ArrayList<String>getRegistroRichiesteSpostamento() throws Exception {
		RichiestaDAO r = new RichiestaPostgresDAO();
		ResultSet rs = r.getRegistroRichiesteSpostamento();

		ArrayList<String> registroRichiesteSpostamento = new ArrayList<>();
		while(rs.next()) {
			registroRichiesteSpostamento.add("Lezione di " + rs.getString("insegnamento") + " giorno " + rs.getString("giorno_originale") + " ore " + rs.getString("ora_inizio_originale") + " -> giorno " + rs.getString("giorno_richiesto") + " ore " + rs.getString("ora_inizio_richiesta") + " - " + rs.getString("ora_fine_richiesta"));
		}
		return registroRichiesteSpostamento;
	}


	public void approvaRichiesta(int id_richiesta) throws Exception {
		RichiestaDAO r = new RichiestaPostgresDAO();
		ResultSet rs = r.cancellaRichiesta(id_richiesta);

		if(rs.next()) {
			creaVariazione(rs.getString ("insegnamento" ), rs.getDate("giorno_originale").toLocalDate(), rs.getDate("giorno_richiesto").toLocalDate(), rs.getTime ("ora_inizio_originale").toLocalTime(), rs.getTime("ora_fine_richiesta").toLocalTime(), rs.getTime("ora_inizio_richiesta").toLocalTime());
		}
		else{throw new Exception("La richiesta non esiste!");
		}
	}


	public void rifiutaRichiesta(int id_richiesta) throws Exception {
		RichiestaDAO r = new RichiestaPostgresDAO();
		ResultSet rs = r.cancellaRichiesta(id_richiesta);

		if(!rs.next()) {
			throw new Exception("La richiesta non esiste!");
		}
	}

	public HashMap<String, ArrayList<String>> getRegistroVincoliDocenti() throws Exception{
		HashMap<String, ArrayList<String>> h = new HashMap<String, ArrayList<String>>();

		VincoloDocenteDAO v = new VincoloDocentePostgresDAO();
		ResultSet rs = v.getVincoliR();
		while(rs.next()) {
			String s = "Giorno: "+ rs.getString("giorno") + ", ore: " + rs.getString("ora_inizio") + " - " + rs.getString("ora_fine");

			h.get(rs.getString("email_docente")).add(s);
		}
		return h;
	}

	public ArrayList<String> getVariazioniDocente(String emailDocente) throws Exception{
		VincoloDocenteDAO v = new VincoloDocentePostgresDAO();
		ResultSet rs = v.getVincoli(emailDocente);
		ArrayList<String> arr = new ArrayList<String>();
		while(rs.next()) {
			arr.add("Giorno: "+ rs.getString("giorno") + ", ore: " + rs.getString("ora_inizio") + "-" + rs.getString("ora_fine"));
		}
		return arr;
	}

	public int getAnnoStudente(String email) throws Exception{
		StudenteDAO s = new StudentePostgresDAO();
		ResultSet rs = s.getAnnoStudente(email);
		if (rs.next()) {
			return rs.getInt("anno_accademico");
		} else throw new Exception("Lo studente non esiste!");
	}

	public ArrayList<String> getVariazioni(int anno) throws Exception{
		VariazioneDAO v = new VariazionePostgresDAO();
		ResultSet rs = v.getVariazioni(anno);

		ArrayList<String> stringArr = new ArrayList<String>();
		while(rs.next()){
			stringArr.add("La lezione di " + rs.getString("insegnamento") + " del "+rs.getString("data_originale")+" alle "+rs.getString("ora_originale")+" è spostata al giorno "+rs.getString("nuova_data")+" ore "+rs.getString("nuova_ora_inizio")+"-"+rs.getString("nuova_ora_fine")+".");
			stringArr.add(rs.getString("insegnamento") + ": "+rs.getString("data_originale")+" ore "+rs.getString("ora_originale")+" spostata a "+rs.getString("nuova_data")+" ore "+rs.getString("nuova_ora_inizio")+"-"+rs.getString("nuova_ora_fine")+".");
		}
		return stringArr;
	}
}


