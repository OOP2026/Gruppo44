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
	private HashMap<String, Docente> docenteTest = new HashMap<>();

	//diciamo che sono del primo anno
	private ArrayList<Lezione> lezioniPOO;
	private ArrayList<Lezione> lezioniAlgebra;

	//diciamo che sono del secondo anno
	private ArrayList<Lezione> lezioniAnalisi;
	private ArrayList<Lezione> lezioniADE;


	private ArrayList<Richiesta> richiesteSpostamento;

	private HashMap<String, ArrayList<VincoloDocente>> registroVincoliDocenti;

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
		Studente r = new Studente("Riccardo", "Formisano", "ric.formisano@studenti.unina.it", "PasswordR", "DE1000092", 1);
		studentiTest.put(r.email, r);
		System.out.println("Studente: " + r.toString());

		//considera un'email e password di ipotetico input e le stampa
		String currentEmail = "sar.formicola@studenti.unina.it";
		String currentPassword = "PasswordS";

		System.out.println("current email: " + currentEmail);
		System.out.println("current password: " + currentPassword);

		//effettua il login tramite i valori di input e stampa il risultato (vero o falso)
		boolean isLogined = login(currentEmail, currentPassword);
		System.out.println("login: " + isLogined);


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

		ArrayList<String>[] l = getLezioni("ric.formisano@studenti.unina.it");

		//stampa l'array temporaneo
		System.out.println("lezioni mostrate: ");
		for(ArrayList<String> arr : l){System.out.println(arr.toString());}

    } //fine Testing

	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

	public static void main(String[] args){Controller controller = getInstance();}

	public boolean login(String email, String password) {
		try{
		boolean result = studentiTest.get(email).login(email, password);;
		return result;}
		catch(Exception e){
			return false;
		}
	} // fine Login

	public void logout() {

	} // fine Logout

	public void signin(String username, String password) {

	} //fine Signin

	public void getLezioni() {
 		//restituisce una hashmap di lezioni

	}

	public ArrayList<String>[] getLezioni(String email) {

		int userType = 0;
		Studente studente = studentiTest.get(email);
		if (studente != null){userType = 1;}
		Docente docente = docenteTest.get(email);
		if (docente != null){userType = 2;}

		ArrayList<Lezione> l = new ArrayList<>();


		ArrayList<String>[] stringArr = new ArrayList[5];
		for(int i = 0; i < stringArr.length; i++){stringArr[i] = new ArrayList<String>();}

		switch(userType){
			case 0:
				stringArr[0].add("error:\ninvalid user");
				break;
			case 1:
				for (Insegnamento ins : insegnamentoTest.values())
                    if (ins.annoAccademico == studente.annoAccademico)
						for(Lezione lezione : ins.lezioni)
							stringArr[lezione.giornoSettimana.ordinal()].add(lezione.toElementString());

				break;
			case 2:
				for (Insegnamento ins : insegnamentoTest.values())
					if (ins.docente == docente)
						for(Lezione lezione : ins.lezioni)
							stringArr[lezione.giornoSettimana.ordinal()].add(lezione.toElementString());

				break;
		}
		return stringArr;
	}


	public boolean aggiungiRichiestaSpostamento(String nomeInsegnamento, String oraOriginale, String giornoOriginale, String giornoRichiesto, String oraInizioRichiesta, String oraFineRichiesta) {
return true;
	} // fine InviaRichiesta

	public void creaDocente(String nome, String cognome, String email, String password) {
		//controlli minimi (se manca un campo ecc), chiama il costruttore di docente o studente
		//chiama Login con gli stessi parametri

	} // fine CreaUtente

	public Studente creaStudente(String nome, String cognome, String email, String password, String matricola, int anno) {
		//controlli minimi (se manca un campo ecc), chiama il costruttore di docente o studente
		//chiama Login con gli stessi parametri

		//testing
		return new Studente(nome, cognome, email, password, matricola, anno);
	} // fine CreaUtente

	public Lezione creaLezione(String nomeInsegnamento, GiornoSettimana giornoSettimana, LocalTime oraInizio, LocalTime oraFine, String nomeAula) {
		Lezione lezione = new Lezione(giornoSettimana, oraInizio, oraFine, nomeAula, nomeInsegnamento);

		return lezione;
	} // fine CreaLezione

	public void creaInsegnamento(String nome, int numeroCFU, int anno, Docente docente ) {
		//cerca nel database lista di docenti

		Insegnamento i =  new Insegnamento(nome, numeroCFU, anno, docente);
	} //fine CreaInsegnamento

	public void creaVariazione(String idLezione, LocalDate giornoOriginale, LocalDate nuovaData, LocalTime oraInizio, LocalTime oraFine){
								      //idLezione è temporaneo, non so come identificare una singola lezione altrimenti
	} //fine CreaVariazione


	public void aggiungiVincoloDocente(String emailDocente, String giornoSettimana, String oraInizio, String oraFine){
		//controlli minimi, restituisce boh boolean
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


	public HashMap<String, ArrayList<String>> getRegistroVincoliDocenti(){
		HashMap<String, ArrayList<String>> h = new HashMap<String, ArrayList<String>>();

		h.forEach((k,v)-> h.get(k).add(v.toString()));

		return h;

	}

	public int getAnnoStudente(String email){
		return studentiTest.get(email).annoAccademico;
	}

	public ArrayList<String> getVariazioni(int anno){
		return null;
	}

}
