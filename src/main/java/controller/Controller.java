package controller;

//import jdk.vm.ci.meta.Local;
import model.*;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller {

	//testing
	private HashMap<String, Studente> studentiTest = new HashMap<>();
	private HashMap<String, Insegnamento> insegnamentoTest = new HashMap<>();

	//diciamo che sono del primo anno
	private ArrayList<Lezione> lezioniPOO;
	private ArrayList<Lezione> lezioniAlgebra;

	//diciamo che sono del secondo anno
	private ArrayList<Lezione> lezioniAnalisi;
	private ArrayList<Lezione> lezioniADE;

	public Controller() {
		Testing();

	}

	private static Controller instance;

	private void Testing(){

		//aggiunge 2 studenti alla hashmap studentiTest
		//studentiTest.put("ric.formisano@studenti.unina.it", new Studente("Riccardo", "Formisano", "ric.formisano@studenti.unina.it", "PasswordR", "DE1000092"));
		studentiTest.put("sar.formicola@studenti.unina.it", new Studente("Sara", "Formicola", "sar.formicola@studenti.unina.it", "PasswordS", "DE1000292", 1));
		studentiTest.put("ele.godano@studenti.unina.it", new Studente("Elena", "Godano", "ele.godano@utenti.studenti.it", "PasswordE", "DE1000092", 2));

		//stampa l'insieme di studenti considerati
		System.out.println("studentiTest: " + studentiTest);

		//crea un singolo studente e lo stampa
		Studente r = new Studente("Riccardo", "Formisano", "ric.formisano@utenti.unina.it", "PasswordR", "DE1000092", 1);
		System.out.println("Studente: " + r.ToString());

		//considera un'email e password di ipotetico input e le stampa
		String currentEmail = "sar.formicola@studenti.unina.it";
		String currentPassword = "PasswordS";

		System.out.println("current email: " + currentEmail);
		System.out.println("current password: " + currentPassword);

		//effettua il login tramite i valori di input e stampa il risultato (vero o falso)
		boolean isLogined = Login(currentEmail, currentPassword);
		System.out.println("login: " + isLogined);

		//Crea insegnamenti e rispettive lezioni
		Insegnamento Analisi = new Insegnamento("Analisi I", 6, 1);
		Insegnamento ADE = new Insegnamento("Architettura Degli Elaboratori", 6, 1);

		Insegnamento Algebra = new Insegnamento("Algebra I", 9, 2);
		Insegnamento POO = new Insegnamento("Programmazione Object-Oriented", 9, 2);

		insegnamentoTest.put("Analisi I", Analisi);
		insegnamentoTest.put("Architettura Degli Elaboratori", ADE);
		insegnamentoTest.put("Algebra I", Algebra);
		insegnamentoTest.put("Programmazione Object-Oriented", POO);

		Analisi.lezioni.add(new Lezione(GiornoSettimana.LUNEDI, LocalTime.of(10,0), LocalTime.of(12,0), "A6", Analisi));
		Analisi.lezioni.add(new Lezione(GiornoSettimana.MERCOLEDI, LocalTime.of(10,0), LocalTime.of(12,0), "A6" ,Analisi));
		Analisi.lezioni.add(new Lezione(GiornoSettimana.VENERDI, LocalTime.of(14,0), LocalTime.of(16,0), "A5", Analisi));

		POO.lezioni.add(new Lezione(GiornoSettimana.LUNEDI, LocalTime.of(12,0), LocalTime.of(14,0), "A6", POO));
		POO.lezioni.add(new Lezione(GiornoSettimana.MERCOLEDI, LocalTime.of(12,0), LocalTime.of(14,0), "A6", POO));
		POO.lezioni.add(new Lezione(GiornoSettimana.MARTEDI, LocalTime.of(12,0), LocalTime.of(14,0), "A5", POO));

		//crea un array di liste da restituire alla GUI e le stampa
		ArrayList<Lezione> l = new ArrayList<>();

		//se l'anno dell'insegnamento è uguale all'anno dello studente, aggiunge le lezioni all'array temporaneo
        for (Insegnamento i : insegnamentoTest.values()) {
            if (i.annoAccademico == r.annoAccademico) l.addAll(i.lezioni);
        }

		//stampa l'array temporaneo
		System.out.println("lezioni mostrate: ");
		for(Lezione lezione : l){System.out.println(lezione.ToString());}

    } //fine Testing

	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

	public static void main(String[] args){Controller controller = getInstance();}

	public boolean Login(String email, String password) {
		try{
		boolean result = studentiTest.get(email).login(email, password);;
		return result;}
		catch(Exception e){
			return false;
		}
	} // fine Login

	public void Logout() {

	} // fine Logout

	public void Signin(String username, String password) {

	} //fine Signin

	public void GetLezioni() {
 		//restituisce una hashmap di lezioni

	}


	public void InviaRichiesta(String nomeInsegnamento, String oraOriginale, LocalDate giornoOriginale, LocalDate giornoRichiesto, LocalTime oraInizioRichiesta, LocalTime oraFineRichiesta) {

	} // fine InviaRichiesta

	public void CreaDocente(String nome, String cognome, String email, String password) {
		//controlli minimi (se manca un campo ecc), chiama il costruttore di docente o studente
		//chiama Login con gli stessi parametri

	} // fine CreaUtente

	public Studente CreaStudente(String nome, String cognome, String email, String password, String matricola, int anno) {
		//controlli minimi (se manca un campo ecc), chiama il costruttore di docente o studente
		//chiama Login con gli stessi parametri

		//testing
		return new Studente(nome, cognome, email, password, matricola, anno);
	} // fine CreaUtente

	public void CreaLezione(String nomeInsegnamento, GiornoSettimana giornoSettimana, LocalTime oraInizio, LocalTime oraFine, String nomeAula) {

	} // fine CreaLezione

	public void CreaInsegnamento(String nome, int numeroCFU, int anno, Docente docente ) {

	} //fine CreaInsegnamento

	public void CreaVariazione(String idLezione, LocalDate giornoOriginale, LocalDate nuovaData, LocalTime oraInizio, LocalTime oraFine){
								      //idLezione è temporaneo, non so come identificare una singola lezione altrimenti
	} //fine CreaVariazione


	public void CreaVincolo(GiornoSettimana giornoSettimana, LocalTime oraInizio, LocalTime oraFine){
		//controlli minimi, restituisce boh boolean
	} //fine CreaVincolo

}
