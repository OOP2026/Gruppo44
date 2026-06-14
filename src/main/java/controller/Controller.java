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

<<<<<<< Updated upstream
=======

	private ArrayList<Richiesta> richiesteSpostamento;

	private HashMap<String, ArrayList<VincoloDocente>> registroVincoliDocenti;

	private Connection connessione;

>>>>>>> Stashed changes
	public Controller() {

		Testing();

		testingDB();
	}

	private static Controller instance;

	private void testingDB()
	{

	}

	private void sqlQuery(String query)
	{
		try {
			connessione = ConnessioneDatabase.getInstance().getConnection();
			PreparedStatement qs = connessione.prepareStatement(query);
			ResultSet resultSet = qs.executeQuery();

		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
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
		docenteTest.put("ptramont@unina.it", new Docente("Porfirio", "Tramontana(goat)", "ptramont@unina.it", "vabbuò"));
		docenteTest.put("raffaele.scandone@unina.it", new Docente("Raffaele", "Scandone", "rafafele.scandone@unina.it", "rigorosamente"));

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

	public void signin(String username, String password) {

	} //fine Signin

	public void getLezioni() {
 		//restituisce una hashmap di lezioni

	}

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

	public void aggiungiRichiestaSpostamento(String nomeInsegnamento, String oraOriginale, String giornoOriginale, String giornoRichiesto, String oraInizioRichiesta, String oraFineRichiesta)
	{

<<<<<<< Updated upstream
	public void inviaRichiesta(String nomeInsegnamento, String oraOriginale, LocalDate giornoOriginale, LocalDate giornoRichiesto, LocalTime oraInizioRichiesta, LocalTime oraFineRichiesta) {

	} // fine InviaRichiesta

	public void CreaDocente(String nome, String cognome, String email, String password) {
		//controlli minimi (se manca un campo ecc), chiama il costruttore di docente o studente
		//chiama Login con gli stessi parametri

=======
	} // fine InviaRichiesta

	public String[] creaDocente(String nome, String cognome, String email, String password) throws Exception{
		DocenteDAO d = new DocentePostgresDAO();
		d.creaDocente(nome, cognome, email, password);
		return loginDocente(email, password);
>>>>>>> Stashed changes
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

	public void creaInsegnamento(String nome, int numeroCFU, int anno, Docente docente ) {
		//cerca nel database lista di docenti

		Insegnamento i =  new Insegnamento(nome, numeroCFU, anno, docente);
	} //fine CreaInsegnamento

	public void creaVariazione(String idLezione, LocalDate giornoOriginale, LocalDate nuovaData, LocalTime oraInizio, LocalTime oraFine){
								      //idLezione è temporaneo, non so come identificare una singola lezione altrimenti
	} //fine CreaVariazione


<<<<<<< Updated upstream
	public void creaVincolo(GiornoSettimana giornoSettimana, LocalTime oraInizio, LocalTime oraFine){
		//controlli minimi, restituisce boh boolean
	} //fine CreaVincolo

=======
	public void aggiungiVincoloDocente(String emailDocente, String giornoSettimana, String oraInizio, String oraFine) throws Exception {
		VincoloDocenteDAO v = new VincoloDocentePostgresDAO();
		v.creaVincolo(emailDocente, giornoSettimana, LocalTime.parse(oraInizio), LocalTime.parse(oraFine));
	} //fine CreaVincolo

	public ArrayList<String> getInsegnamentiDocente(String emailDocente){return null;}

	public ArrayList<String>getRegistroRichiesteSpostamento(){
		ArrayList<String> s = new ArrayList<String>();

		for(Richiesta r : richiesteSpostamento){
			String string = "Richiesta spostamento per " + r.insegnamento.nome + ": lezione delle ore "+r.oraInizioOriginale.toString() +" di "+r.giornoOriginale.toString()+" a "+r.giornoRichiesto.toString()+ " ore "+r.oraInizioRichiesta.toString()+"-"+ r.oraFineRichiesta.toString();
		s.add(string);
		}

		//"Richiesta spostamento per [Algebra]: lezione delle ore [10:00] di [lun 10 mag] a [mar 11 mag] ore [11:00]-[12:00]"
		return s;
	}


	public boolean approvaRichiesta(int i){return true;}
	public void rifiutaRichiesta(int i){}


	public HashMap<String, ArrayList<String>> getRegistroVincoliDocenti() throws Exception{
		HashMap<String, ArrayList<String>> h = new HashMap<String, ArrayList<String>>();

		VincoloDocenteDAO v = new VincoloDocentePostgresDAO();
		ResultSet rs = v.getVincoliR();
		while(rs.next()) {
			String s = "Giorno: "+ rs.getString("giorno") + ", ore: " + rs.getString("ora_inizio") + "-" + rs.getString("ora_fine");

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


	public int getAnnoStudente(String email){
		return studentiTest.get(email).annoAccademico;
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

>>>>>>> Stashed changes
}
