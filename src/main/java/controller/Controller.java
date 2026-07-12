package controller;

//import jdk.vm.ci.meta.Local;
import dao.LezioneDAO;
import model.*;
import dao.*;
import implementazionedao.*;

import java.sql.*;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {

	//public Controller() {}
	private Docente docenteAttivo;
	private Studente studenteAttivo;
	private ArrayList<Docente> docentiLocale;
	private ArrayList<Studente> studentiLocale;
	private ArrayList<Aula> auleLocale;
	private ArrayList<Insegnamento> insegnamentiLocale;
	private ArrayList<Lezione> lezioniLocale;
	private ArrayList<Richiesta> richiesteLocale;
	private ArrayList<Variazione> variazioniLocale;
	private ArrayList<VincoloDocente> vincoliLocale;


	private static Controller instance;

	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

	//public static void main(String[] args){Controller controller = getInstance();}


	/**
	 * Accetta come parametri un'email e una password come {@link String}, e restituisce nome, cognome ed email del docente corrispondente, se esiste e la password è corretta.
	 * <p>
	 * @param email la email inserita dall'utente per eseguire l'accesso.
	 * @param password la password inserita dall'utente per eseguire l'accesso.
	 * @return Un array di {@link String} contenente, in ordine: nome, cognome, email dell'utente
	 * @throws Exception Se l'email non è valida, ls password è sbagliata o in caso di errori nel database.
	 * @see Docente
	 */
	public String[] loginDocente(String email, String password) throws Exception {
		// prende i dati dal database, li mette nel model, li riprende dal model e li manda alla GUI
		DocenteDAO d = new DocentePostgresDAO();
		try (ResultSet rs = d.login(email, password)){
			docenteAttivo = new Docente(rs.getString("nome"), rs.getString("cognome"), rs.getString("email"), rs.getString("password"));
			return new String[] {docenteAttivo.getNome(), docenteAttivo.getCognome(), docenteAttivo.getEmail()};
		}

	} // fine Login


	/**
	 * Accetta come parametri un'email e una password come {@link String}, e restituisce nome, cognome ed email dello studente corrispondente, se esiste e la password è corretta.
	 * <p>
	 * @param email la email inserita dall'utente per eseguire l'accesso.
	 * @param password la password inserita dall'utente per eseguire l'accesso.
	 * @return Un array di {@link String} contenente, in ordine: nome, cognome, email, matricola, anno accademico dell'utente
	 * @throws Exception Se l'email non è valida, ls password è sbagliata o in caso di errori nel database.
	 */
	public String[] loginStudente(String email, String password) throws Exception{

		StudenteDAO s = new StudentePostgresDAO();

		try (ResultSet rs = s.login(email, password)) {
			studenteAttivo = new Studente(rs.getString("nome"), rs.getString("cognome"), rs.getString("email"), rs.getString("password"), rs.getString("matricola"), rs.getInt("anno_accademico"));
			return new String[] {studenteAttivo.getNome(),studenteAttivo.getCognome(), studenteAttivo.getEmail(), studenteAttivo.getMatricola(), String.valueOf(studenteAttivo.getAnnoAccademico())};
		}
	}
		 // fine Login

	/**
	 * Restituisce tutte le lezioni erogate da un docente.
	 * @param email L'email del docente di cui si vogliono recuperare le lezioni.
	 * @return ArrayList di {@link String} nel format restituito da {@link #formatLezioni(ArrayList)}
	 * @throws Exception In caso di errori nel database.
	 * @see #formatLezioni(ArrayList)
	 */
	public ArrayList<String>[] getLezioni(String email) throws Exception{
		LezioneDAO l = new LezionePostgresDAO();
		ResultSet rs = l.getLezioni(email);
		istanziaLezioni(rs);
		return formatLezioni(lezioniLocale);
	}

	/**
	 * Restituisce tutte le lezioni erogate per un anno accademico.
	 * @param anno L'anno dello studente di cui si vogliono recuperare le lezioni.
	 * @return ArrayList di {@link String} nel format restituito da {@link #formatLezioni(ArrayList)}
	 * @throws Exception In caso di errori nel database.
	 * @see #formatLezioni(ArrayList)
	 */
	public ArrayList<String>[] getLezioni(int anno) throws Exception{
		LezioneDAO l = new LezionePostgresDAO();
		ResultSet rs= l.getLezioni(anno);
		istanziaLezioni(rs);
		return formatLezioni(lezioniLocale);
	}

	/**
	 *
	 */
	public ArrayList<String>[] getLezioni() throws Exception{
		LezioneDAO l = new LezionePostgresDAO();
		ResultSet rs = l.getLezioni();
		istanziaLezioni(rs);
		return formatLezioni(lezioniLocale);
	}

	/**
	 * Accetta un ResultSet di lezioni e lo formatta in stringhe da restituire a un'interfaccia.
	 * @return ArrayList di {@link String} della forma: "[oraInizio]\n[oraFine]\n[insegnamento]\n[aula]"
	 * @throws SQLException In caso di errori nel database.
	 */
	private ArrayList<String>[] formatLezioni(ArrayList<Lezione> lezioniArr) throws SQLException{
		ArrayList<String>[] stringArr = new ArrayList[5];
		for (int i = 0; i < stringArr.length; i++) {
			stringArr[i] = new ArrayList<>();
		}
			for (Lezione l : lezioniArr) {
				int giorno = l.getGiornoSettimana().ordinal();
				String oraInizio = l.getOraInizio().toString();
				String oraFine = l.getOraFine().toString();
				String aula = l.getAula();
				String insegnamento = l.getInsegnamento();

				stringArr[giorno].add(oraInizio + "\n" + oraFine + "\n" + insegnamento + "\n" + aula);
			}
			return stringArr;
	}


	private void istanziaLezioni(ResultSet rs) throws SQLException{

		ArrayList<Lezione> arrLezioni = new ArrayList<>();
		try {
			while (rs.next()) {
				GiornoSettimana giorno = GiornoSettimana.values()[rs.getInt("giorno_settimana") - 1];
				LocalTime oraInizio = rs.getTime("ora_inizio").toLocalTime();
				LocalTime oraFine = rs.getTime("ora_fine").toLocalTime();
				String aula = rs.getString("aula");
				String insegnamento = rs.getString("insegnamento");

				Lezione l = new Lezione(giorno, oraInizio, oraFine, aula, insegnamento );
				arrLezioni.add(l);
			}
			lezioniLocale = arrLezioni;
		} catch (SQLException e) {throw new SQLException("Si è verificato un errore nel database.");}

	}


	/**
	 * Aggiunge al database una richiesta di spostamento da parte di un docente. I parametri di orario e data sono in formato {@link LocalTime} e {@link LocalDate}.
	 * @param nomeInsegnamento Il nome dell'insegnamento relativo alla richiesta.
	 * @param oraOriginale L'orario originale della lezione relativa.
	 * @param giornoOriginale La data originale della lezione relativa.
	 * @param giornoRichiesto Il giorno richiesto dal docente.
	 * @param oraInizioRichiesta L'ora di inizio richiesta dal docente.
	 * @param oraFineRichiesta L'ora di fine richiesta dal docente.
	 * @param aula La nuova aula della lezione modificata.
	 * @throws Exception In caso di errori nel database.
	 */
	public void aggiungiRichiestaSpostamento(String nomeInsegnamento, String oraInizioOriginale, String dataOriginale, String dataRichiesta, String oraInizioRichiesta, String oraFineRichiesta, String aula) throws Exception
	{
		Richiesta richiesta = new Richiesta(nomeInsegnamento, LocalDate.parse(dataOriginale), LocalDate.parse(dataRichiesta), LocalTime.parse(oraInizioOriginale), LocalTime.parse(oraInizioRichiesta), LocalTime.parse(oraFineRichiesta), aula);
		richiesteLocale.add(richiesta);
		RichiestaDAO r = new RichiestaPostgresDAO();
		r.aggiungiRichiestaSpostamento(richiesta.getInsegnamento(), richiesta.getOraInizioOriginale(), richiesta.getGiornoOriginale(), richiesta.getGiornoRichiesto(), richiesta.getOraInizioRichiesta(), richiesta.getOraFineRichiesta(), aula);

	} // fine InviaRichiesta

	/**
	 * Aggiunge un utente di tipo {@link Docente} al database. La funzione esegue automaticamente il login chiamando {@link #loginDocente(String, String)}.
	 * @param nome Il nome dell'utente.
	 * @param cognome Il cognome dell'utente.
	 * @param email L'email usata dall'utente per registrarsi univocamente.
	 * @param password La password dell'utente per l'accesso.
	 * @return Un array di stringhe contenente, in ordine: nome, cognome, email dell'utente
	 * @throws Exception In caso di errori nel database.
	 */
	public String[] creaDocente(String nome, String cognome, String email, String password) throws Exception{
		Docente docente = new Docente(nome, cognome, email, password);
		docentiLocale.add(docente);
		DocenteDAO d = new DocentePostgresDAO();
		d.creaDocente(nome, cognome, email, password);
		return loginDocente(email, password);
	} // fine CreaUtente

	public void eliminaDocente(String email) throws Exception {
		DocenteDAO d = new DocentePostgresDAO();
		d.eliminaDocente(email);
        docentiLocale.removeIf(docente -> docente.getEmail().equals(email));
	}

	/**
	 * Aggiunge un utente di tipo {@link Studente} al database. La funzione esegue automaticamente il login chiamando {@link #loginStudente(String, String)}.
	 * @param nome Il nome dell'utente.
	 * @param cognome Il cognome dell'utente.
	 * @param email L'email usata dall'utente per registrarsi univocamente.
	 * @param password La password dell'utente per l'accesso.
	 * @param matricola La matricola dello studente.
	 * @param anno L'anno accademico dello studente.
	 * @return Un array di {@link String} contenente, in ordine: nome, cognome, email, matricola, anno accademico dell'utente
	 * @throws Exception In caso di errori nel database.
	 */
	public String[] creaStudente(String nome, String cognome, String email, String password, String matricola, int anno) throws Exception {
		Studente studente = new Studente(nome, cognome, email, password, matricola, anno);
		studentiLocale.add(studente);
		StudenteDAO s = new StudentePostgresDAO();
		s.creaStudente(studente.getNome(), studente.getCognome(), studente.getEmail(), studente.getPassword(), studente.getMatricola(), studente.getAnnoAccademico());
		return loginStudente(email, password);
	} // fine CreaUtente

	public void eliminaStudente(String email) throws Exception {
		StudenteDAO s = new StudentePostgresDAO();
		s.eliminaStudente(email);
		studentiLocale.removeIf(studente -> studente.getEmail().equals(email));
	}

	/**
	 * Aggiunge una {@link Lezione} settimanale al database.
	 * @param nomeInsegnamento Il nome dell'insegnamento della lezione.
	 * @param giornoSettimana Il giorno settimanale in cui la lezione viene erogata.
	 * @param oraInizio L'orario di inizio della lezione.
	 * @param oraFine L'orario di fine della lezione.
	 * @param nomeAula Il nome dell'aula in cui la lezione è erogata.
	 * @throws Exception In caso di errori nel database.
	 */
	public void creaLezione(String nomeInsegnamento, String giornoSettimana, String oraInizio, String oraFine, String nomeAula) throws Exception {
		Lezione lezione = new Lezione(GiornoSettimana.valueOf(giornoSettimana.toUpperCase()), LocalTime.parse(oraInizio), LocalTime.parse(oraFine), nomeAula, nomeInsegnamento);
		lezioniLocale.add(lezione);
		LezioneDAO l = new LezionePostgresDAO();
    	l.creaLezione(lezione.getGiornoSettimana().toString(), lezione.getOraInizio(), lezione.getOraFine(), lezione.getAula(), nomeInsegnamento);
	} // fine CreaLezione

	public void eliminaLezione(String insegnamento, String giornoSettimana, LocalTime oraInizio) throws Exception {
		LezioneDAO l = new LezionePostgresDAO();
		l.eliminaLezione(insegnamento,  giornoSettimana, oraInizio);
	}

	/**
	 * Aggiunge un {@link Insegnamento} al database.
	 * @param nome Il nome dell'insegnamento.
	 * @param numeroCFU Il numero di CFU accreditati dall'insegnamento.
	 * @param anno L'anno accademico in cui l'insegnamento è erogato.
	 * @param email l'email del docente che eroga l'insegnamento.
	 * @throws Exception In caso di errori nel database.
	 */
	public void creaInsegnamento(String nome, int numeroCFU, int anno, String email ) throws Exception {
		InsegnamentoDAO i = new InsegnamentoPostgresDAO();
		i.creaInsegnamento(nome, numeroCFU, anno, email);
	} //fine CreaInsegnamento

	public void aggiornaInsegnamento (String nomeInsegnamento, int numeroCFU, int anno, String emailDocente) throws Exception {
		InsegnamentoDAO i = new InsegnamentoPostgresDAO();
		i.aggiornaInsegnamento(nomeInsegnamento, numeroCFU, anno, emailDocente);
	}

	/**
	 * Aggiunge una {@link Variazione} al database.
	 * @param insegnamento Il nome dell'insegnamento relativo.
	 * @param dataOriginale La data(non settimanale) originale della lezione relativa.
	 * @param nuovaData La nuova data della lezione modificata.
	 * @param oraInizioOriginale L'orario di inizio originale della lezione.
	 * @param nuovaOraInizio Il nuovo orario di inizio della lezione modificata.
	 * @param nuovaOraFine Il nuovo orario di fine della lezione modificata.
	 * @param aula La nuova aula della lezione modificata.
	 * @throws Exception In caso di errori nel database.
	 */
	public void creaVariazione(String insegnamento, LocalDate dataOriginale, LocalDate nuovaData, LocalTime oraInizioOriginale, LocalTime nuovaOraInizio, LocalTime nuovaOraFine, String aula) throws  Exception {
		VariazioneDAO v = new VariazionePostgresDAO();
		v.creaVariazione(insegnamento, dataOriginale, nuovaData, oraInizioOriginale, nuovaOraInizio, nuovaOraFine, aula);
	} //fine CreaVariazione

	public void eliminaVariazione(String insegnamento, LocalDate dataOriginale, LocalTime oraInizioOriginale ) throws Exception {
		VariazioneDAO v = new VariazionePostgresDAO();
		v.eliminaVariazione(insegnamento, dataOriginale, oraInizioOriginale);
	}

	/**
	 * Aggiunge un vincolo di indisponibilità ({@link VincoloDocente}) di un docente al database.
	 * @param emailDocente L'email del docente che sta inviando il vincolo.
	 * @param giornoSettimana Il giorno dell'indisponibilità.
	 * @param oraInizio L'ora di inizio dell'indisponibilità.
	 * @param oraFine L'ora di fine dell'indisponibilità.
	 * @throws Exception In caso di errori nel database.
	 */
	public void aggiungiVincoloDocente(String emailDocente, String giornoSettimana, String oraInizio, String oraFine) throws Exception {
		VincoloDocenteDAO v = new VincoloDocentePostgresDAO();
		v.creaVincolo(emailDocente, giornoSettimana, LocalTime.parse(oraInizio), LocalTime.parse(oraFine));
	} //fine CreaVincolo

	public void eliminaVincoloDocente (String email, String giorno,LocalTime oraInizio ) throws Exception {
		VincoloDocenteDAO v = new VincoloDocentePostgresDAO();
		v.eliminaVincoloDocente(email, giorno, oraInizio);
	}

	/**
	 * Accetta un l'email di un docente e restituisce i suoi insegnamenti.
	 * @param emailDocente L'email del docente che eroga l'insegnamento.
	 * @return ArrayList di stringhe contenente gli insegnamenti di un determinato docente. Le stringhe sono della forma: "[nome], anno [X], [Y] CFU"
	 * @throws Exception In caso di errori nel database.
	 */
	public List<String> getInsegnamentiDocente(String emailDocente) throws Exception {
		InsegnamentoDAO i = new InsegnamentoPostgresDAO();
		ResultSet rs = i.getInsegnamentiDocente(emailDocente);

		ArrayList<String> insegnamenti = new ArrayList<>();
		while(rs.next()) {
			insegnamenti.add(rs.getString("nome") + ", anno " + rs.getString("anno_accademico") + ", " + rs.getString("numerocfu") + "CFU" );
		}
		return insegnamenti;
	}


	/**
	 *
	 */
	public List<String> getInsegnamenti() throws Exception {
		InsegnamentoDAO d = new InsegnamentoPostgresDAO();
		ResultSet rs = d.getInsegnamenti();

		ArrayList<String> listaInsegnamenti = new ArrayList<>();
		while(rs.next()) {
			listaInsegnamenti.add(rs.getString("nome"));
		}
		return listaInsegnamenti;
	}

	/**
	 *
	 */
	public void eliminaInsegnamento(String nomeInsegnamento) throws Exception {
		InsegnamentoDAO g = new InsegnamentoPostgresDAO();
		g.eliminaInsegnamento(nomeInsegnamento);
	}


	/**
	 * Restituisce tutte le richieste di spostamento attualmente in attesa di risposta.
	 * @return ArrayList di stringhe della forma: "Lezione di [insegnamento] giorno [gg/mm/yyyy] ore [hh:mm] -> giorno [gg/mm/yyyy] ore [hh:mm]"
	 * @throws Exception In caso di errori nel database.
	 */
	public List<String>getRegistroRichiesteSpostamento() throws Exception {
		RichiestaDAO r = new RichiestaPostgresDAO();
		ArrayList<String> registroRichiesteSpostamento = new ArrayList<>();
		try(ResultSet rs = r.getRegistroRichiesteSpostamento()) {

			while(rs.next()) {
				registroRichiesteSpostamento.add("Lezione di " + rs.getString("insegnamento") + " giorno " + rs.getString("giorno_originale") + " ore " + rs.getString("ora_inizio_originale") + " -> giorno " + rs.getString("giorno_richiesto") + " ore " + rs.getString("ora_inizio_richiesta") + " - " + rs.getString("ora_fine_richiesta") + "aula" + rs.getString("aula"));
			}

		} return registroRichiesteSpostamento;
	}

	/**
	 * Approva una richiesta di spostamento di una lezione. La richiesta viene eliminata dal database, e inserisce una variazione corrispondente chiamando {@link #creaVariazione}.
	 * @param idRichiesta L'identificativo della richiesta da approvare.
	 * @throws Exception In caso di errori nel database.
	 */
	public void approvaRichiesta(int idRichiesta) throws Exception {
		RichiestaDAO r = new RichiestaPostgresDAO();

		try(ResultSet rs = r.cancellaRichiesta(idRichiesta)){
			if(rs.next()) {
				creaVariazione(rs.getString ("insegnamento" ), rs.getDate("data_originale").toLocalDate(), rs.getDate("data_richiesta").toLocalDate(), rs.getTime("ora_inizio_originale").toLocalTime(), rs.getTime("ora_inizio").toLocalTime(), rs.getTime("ora_fine").toLocalTime(), rs.getString("aula"));
			}
			else{throw new SQLException("La richiesta non esiste!");
			}
		}
	}

	/**
	 * Rifiuta una richiesta di spostamento di una lezione. La richiesta viene eliminata dal database.
	 * @param idRichiesta L'identificativo della richiesta da rifiutare.
	 * @throws Exception In caso di errori nel database.
	 */
	public void rifiutaRichiesta(int idRichiesta) throws Exception {
		RichiestaDAO r = new RichiestaPostgresDAO();

		try (ResultSet rs = r.cancellaRichiesta(idRichiesta)){
			if(!rs.next()) {
				throw new SQLException("La richiesta non esiste!");
			}
		}
	}

	/**
	 * Restituisce tutti i vincoli di indisponibilità inseriti da ogni docente.
	 * @return Hashmap che usa come chiave l'email di un docente e come valore un ArrayList di stringhe contenente i suoi vincoli inseriti. Le stringhe sono della forma "Giorno: [gg/mm/yyyy], ore: [hh:mm] - [hh:mm]".
	 * @throws Exception In caso di errori nel database.
	 */
	public Map<String, ArrayList<String>> getRegistroVincoliDocenti() throws Exception{
		HashMap<String, ArrayList<String>> h = new HashMap<>();
		VincoloDocenteDAO v = new VincoloDocentePostgresDAO();

		try (ResultSet rs = v.getVincoliR()) {
			while(rs.next()) {
				String s = "Giorno: "+ rs.getString("giorno") + ", ore: " + rs.getString("ora_inizio") + " - " + rs.getString("ora_fine");

				h.get(rs.getString("email_docente")).add(s);
			}

		} return h;
	}

	/**
	 * Restituisce le variazioni relative alle lezioni di un docente.
	 * @param emailDocente L'email del docente considerato.
	 * @return Un ArrayList di stringhe contenente i vincoli di indisponibilità inseriti da un docente. Le stringhe sono della forma "Giorno: [gg/mm/yyyy], ore: [hh:mm] - [hh:mm]".
	 * @throws Exception In caso di errori nel database.
	 */
	public List<String> getVincoliDocente(String emailDocente) throws Exception{
		VincoloDocenteDAO v = new VincoloDocentePostgresDAO();
		ArrayList<String> arr = new ArrayList<>();

		try (ResultSet rs = v.getVincoli(emailDocente)){
			while(rs.next()) {
				arr.add("Giorno: "+ rs.getString("giorno") + ", ore: " + rs.getString("ora_inizio") + "-" + rs.getString("ora_fine"));
			}
		} return arr;
	}

	/**
	 * Restituisce l'anno accademico di uno studente.
	 * @param email L'email dello studente.
	 * @return {@link int} che rappresenta l'anno accademico dello studente.
	 * @throws Exception In caso di errori nel database.
	 */
	public int getAnnoStudente(String email) throws Exception{
		StudenteDAO s = new StudentePostgresDAO();
		ResultSet rs = s.getAnnoStudente(email);
		if (rs.next()) {
			return rs.getInt("anno_accademico");
		} else throw new SQLException("Lo studente non esiste!");
	}

	/**
	 * Restituisce le variazioni relative alle lezioni di un anno accademico.
	 * @param anno L'anno accademico delle cui lezioni si vogliono recuperare le variazioni.
	 * @return Un ArrayList di stringhe contenente tutte le variazioni. Le stringhe sono della forma "[insegnamento]: [gg/mm/yyyy] ore [hh:mm] spostata a [gg/mm/yyyy] ore [hh:mm]-[hh:mm] "
	 * @throws Exception In caso di errori nel database.
	 */
	public List<String> getVariazioni(int anno) throws Exception{
		VariazioneDAO v = new VariazionePostgresDAO();
		ResultSet rs = v.getVariazioni(anno);
		return formatVariazioni(rs);
	}

	public List<String> formatVariazioni (ResultSet rs) throws SQLException{

		ArrayList<String> stringArr = new ArrayList<>();
		while(rs.next()){
			//stringArr.add("La lezione di " + rs.getString("insegnamento") + " del "+rs.getString("data_originale")+" alle "+rs.getString("ora_originale")+" è spostata al giorno "+rs.getString("nuova_data")+" ore "+rs.getString("nuova_ora_inizio")+"-"+rs.getString("nuova_ora_fine")+".");
			stringArr.add(rs.getString("insegnamento") + ": "+rs.getString("data_originale")+" ore "+rs.getString("ora_originale")+" spostata a "+rs.getString("nuova_data")+" ore "+rs.getString("nuova_ora_inizio")+"-"+rs.getString("nuova_ora_fine")+" nell'aula " + rs.getString("aula"));
		}
		return stringArr;

	}


	/**
	 * Restituisce le variazioni relative alle lezioni di un docente.
	 * @param email L'email del docente relativo.
	 * @return Un ArrayList di stringhe contenente tutte le variazioni. Le stringhe sono della forma "[insegnamento]: [gg/mm/yyyy] ore [hh:mm] spostata a [gg/mm/yyyy] ore [hh:mm]-[hh:mm] "
	 * @throws Exception In caso di errori nel database.
	 */
	public List<String> getVariazioni(String email) throws Exception{
		VariazioneDAO v = new VariazionePostgresDAO();
		ResultSet rs = v.getVariazioni(email);
		return formatVariazioni(rs);
	}

	public void creaAula (String nomeAula, int capienzaMassima) throws Exception {
		AulaDAO a = new AulaPostgresDAO();
		a.creaAula(nomeAula, capienzaMassima);
	}

	public void eliminaAula (String nomeAula) throws Exception {
		AulaDAO a = new AulaPostgresDAO();
		a.eliminaAula(nomeAula);
	}

	public List<String> getAule() throws Exception {
		AulaDAO a = new AulaPostgresDAO();
		ArrayList<String> stringArr = new ArrayList<>();
		try(ResultSet rs = a.getAule()){
			while(rs.next()) {
				stringArr.add(rs.getString("nome_aula")); //+ ": " + rs.getString("capienza_massima")
			}
		}
		return stringArr;
	}

}






