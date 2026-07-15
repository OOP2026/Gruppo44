package controller;

//import jdk.vm.ci.meta.Local;
import common.GiornoSettimana;
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


	private Docente docenteAttivo;
	private Studente studenteAttivo;
	private ArrayList<Aula> auleLocale = new ArrayList<>();
	private ArrayList<Insegnamento> insegnamentiLocale = new ArrayList<>();
	private ArrayList<Lezione> lezioniLocale = new ArrayList<>();
	private ArrayList<Richiesta> richiesteLocale = new ArrayList<>();
	private ArrayList<Variazione> variazioniLocale = new ArrayList<>();
	private ArrayList<VincoloDocente> vincoliLocale = new ArrayList<>();


	private static Controller instance;

	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}


	/**
	 * Accetta come parametri un'email e una password come {@link String}, e restituisce nome, cognome ed email del docente corrispondente, se esiste e la password è corretta.
	 * <p>
	 * @param email la email inserita dall'utente per eseguire l'accesso.
	 * @param password la password inserita dall'utente per eseguire l'accesso.
	 * @return Un array di {@link String} contenente, in ordine: nome, cognome, email dell'utente
	 * @throws SQLException Se l'email non è valida, ls password è sbagliata o in caso di errori nel database.
	 * @see Docente
	 */
	public String[] loginDocente(String email, String password) throws SQLException {
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
	 * @throws SQLException Se l'email non è valida, ls password è sbagliata o in caso di errori nel database.
	 */
	public String[] loginStudente(String email, String password) throws SQLException{

		StudenteDAO s = new StudentePostgresDAO();

			try (ResultSet rs = s.login(email, password)) {
			studenteAttivo = new Studente(rs.getString("nome"), rs.getString("cognome"), rs.getString("email"), rs.getString("password"), rs.getString("matricola"), rs.getInt("anno_accademico"));
			return new String[] {studenteAttivo.getNome(),studenteAttivo.getCognome(), studenteAttivo.getEmail(), studenteAttivo.getMatricola(), String.valueOf(studenteAttivo.getAnnoAccademico())};
		}
	}
		 // fine Login

	/**
	 * Restituisce tutte le lezioni erogate da un docente.
	 * @return ArrayList di {@link String} nel format restituito da {@link #formatLezioni(ArrayList)}
	 * @throws SQLException In caso di errori nel database.
	 * @see #formatLezioni(ArrayList)
	 */
	public ArrayList<String>[] getLezioniDocente() throws SQLException{
		LezioneDAO l = new LezionePostgresDAO();
		ResultSet rs = l.getLezioni(docenteAttivo.getEmail());
		istanziaLezioni(rs);
		return formatLezioni(lezioniLocale);
	}

	/**
	 * Restituisce tutte le lezioni erogate per un anno accademico.
	 * @return ArrayList di {@link String} nel format restituito da {@link #formatLezioni(ArrayList)}
	 * @throws SQLException In caso di errori nel database.
	 * @see #formatLezioni(ArrayList)
	 */
	public ArrayList<String>[] getLezioniStudente() throws SQLException{
		int anno = studenteAttivo.getAnnoAccademico();
		LezioneDAO l = new LezionePostgresDAO();
		ResultSet rs= l.getLezioni(anno);
		istanziaLezioni(rs);
		return formatLezioni(lezioniLocale);
	}

	/**
	 * Accetta un ResultSet di lezioni e lo formatta in stringhe da restituire a un'interfaccia.
	 * @return ArrayList di {@link String} della forma: "[oraInizio]\n[oraFine]\n[insegnamento]\n[aula]"
	 */
	private ArrayList<String>[] formatLezioni(ArrayList<Lezione> lezioniArr){
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

				stringArr[giorno].add(oraInizio + "-" + oraFine + ": " + insegnamento + " (aula " + aula + ")");
			}
			return stringArr;
	}


	private void istanziaLezioni(ResultSet rs) throws SQLException{

		ArrayList<Lezione> arrLezioni = new ArrayList<>();
			while (rs.next()) {
				GiornoSettimana giorno = GiornoSettimana.values()[GiornoSettimana.valueOf(rs.getString("giorno_settimana")).ordinal()];
				LocalTime oraInizio = rs.getTime("ora_inizio").toLocalTime();
				LocalTime oraFine = rs.getTime("ora_fine").toLocalTime();
				String aula = rs.getString("aula");
				String insegnamento = rs.getString("insegnamento");

				Lezione l = new Lezione(giorno, oraInizio, oraFine, aula, insegnamento );
				arrLezioni.add(l);
			}
			lezioniLocale = arrLezioni;

	}


	/**
	 * Aggiunge al database una richiesta di spostamento da parte di un docente. I parametri di orario e data sono in formato {@link LocalTime} e {@link LocalDate}.
	 * @param nomeInsegnamento Il nome dell'insegnamento relativo alla richiesta.
	 * @param oraInizioOriginale L'orario originale della lezione relativa.
	 * @param dataOriginale La data originale della lezione relativa.
	 * @param dataRichiesta Il giorno richiesto dal docente.
	 * @param oraInizioRichiesta L'ora di inizio richiesta dal docente.
	 * @param oraFineRichiesta L'ora di fine richiesta dal docente.
	 * @param aula La nuova aula della lezione modificata.
	 * @throws SQLException In caso di errori nel database.
	 */
	public void aggiungiRichiestaSpostamento(String nomeInsegnamento, String oraInizioOriginale, String dataOriginale, String dataRichiesta, String oraInizioRichiesta, String oraFineRichiesta, String aula) throws SQLException
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
	 * @throws SQLException In caso di errori nel database.
	 */
	public String[] creaDocente(String nome, String cognome, String email, String password) throws SQLException{
		DocenteDAO d = new DocentePostgresDAO();
		d.creaDocente(nome, cognome, email, password);
		return loginDocente(email, password);
	} // fine CreaUtente

	public void eliminaDocente(String email) throws SQLException {
		DocenteDAO d = new DocentePostgresDAO();
		d.eliminaDocente(email);
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
	 * @throws SQLException In caso di errori nel database.
	 */
	public String[] creaStudente(String nome, String cognome, String email, String password, String matricola, int anno) throws SQLException {
		Studente studente = new Studente(nome, cognome, email, password, matricola, anno);
		StudenteDAO s = new StudentePostgresDAO();
		s.creaStudente(studente.getNome(), studente.getCognome(), studente.getEmail(), studente.getPassword(), studente.getMatricola(), studente.getAnnoAccademico());
		return loginStudente(email, password);
	} // fine CreaUtente

	public void eliminaStudente(String email) throws SQLException {
		StudenteDAO s = new StudentePostgresDAO();
		s.eliminaStudente(email);
	}

	/**
	 * Aggiunge una {@link Lezione} settimanale al database.
	 * @param nomeInsegnamento Il nome dell'insegnamento della lezione.
	 * @param giornoSettimana Il giorno settimanale in cui la lezione viene erogata.
	 * @param oraInizio L'orario di inizio della lezione.
	 * @param oraFine L'orario di fine della lezione.
	 * @param nomeAula Il nome dell'aula in cui la lezione è erogata.
	 * @throws SQLException In caso di errori nel database.
	 */
	public void creaLezione(String nomeInsegnamento, String giornoSettimana, String oraInizio, String oraFine, String nomeAula) throws SQLException {
		Lezione lezione = new Lezione(GiornoSettimana.valueOf(giornoSettimana.toUpperCase()), LocalTime.parse(oraInizio), LocalTime.parse(oraFine), nomeAula, nomeInsegnamento);
		lezioniLocale.add(lezione);
		LezioneDAO l = new LezionePostgresDAO();
    	l.creaLezione(lezione.getGiornoSettimana().toString(), lezione.getOraInizio(), lezione.getOraFine(), lezione.getAula(), lezione.getInsegnamento());
	} // fine CreaLezione
	
	/**
	 * Aggiunge un {@link Insegnamento} al database.
	 * @param nome Il nome dell'insegnamento.
	 * @param numeroCFU Il numero di CFU accreditati dall'insegnamento.
	 * @param anno L'anno accademico in cui l'insegnamento è erogato.
	 * @throws SQLException In caso di errori nel database.
	 */
	public void creaInsegnamento(String nome, int numeroCFU, int anno) throws SQLException {
		Insegnamento insegnamento = new Insegnamento(nome, numeroCFU, anno, docenteAttivo.getEmail());
		insegnamentiLocale.add(insegnamento);
		InsegnamentoDAO i = new InsegnamentoPostgresDAO();
		i.creaInsegnamento(insegnamento.getNome(), insegnamento.getNumeroCFU(), insegnamento.getAnnoAccademico(), insegnamento.getDocente());
	} //fine CreaInsegnamento

	public void aggiornaInsegnamento (String nomeInsegnamento, int numeroCFU, int anno, String emailDocente) throws SQLException {
		InsegnamentoDAO i = new InsegnamentoPostgresDAO();
		for(Insegnamento ins : insegnamentiLocale) {
			if(ins.getNome().equals(nomeInsegnamento)) {
				ins.setNumeroCFU(numeroCFU);
				ins.setAnnoAccademico(anno);
				ins.setDocente(emailDocente);
				i.aggiornaInsegnamento(ins.getNome(), ins.getNumeroCFU(), ins.getAnnoAccademico(), ins.getDocente());
				LezioneDAO l = new LezionePostgresDAO();
				l.eliminaLezioniInsegnamento(nomeInsegnamento);
			}
		}

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
	 * @throws SQLException In caso di errori nel database.
	 */
	public void creaVariazione(String insegnamento, String dataOriginale, String nuovaData, String oraInizioOriginale, String nuovaOraInizio, String nuovaOraFine, String aula) throws  SQLException {
		Variazione variazione = new Variazione(LocalDate.parse(dataOriginale), LocalDate.parse(nuovaData), LocalTime.parse(nuovaOraInizio), LocalTime.parse(nuovaOraFine), LocalTime.parse(oraInizioOriginale), insegnamento, aula);
		variazioniLocale.add(variazione);
		VariazioneDAO v = new VariazionePostgresDAO();
		v.creaVariazione(variazione.getInsegnamento(), variazione.getDataOriginale(), variazione.getNuovaData(), variazione.getOraInizioOriginale(), variazione.getNuovaOraInizio(), variazione.getNuovaOraFine(), variazione.getAula());
	} //fine CreaVariazione

	public void eliminaVariazione(String insegnamento, String dataOriginale, String oraInizioOriginale ) throws SQLException {
		VariazioneDAO v = new VariazionePostgresDAO();
		v.eliminaVariazione(insegnamento, LocalDate.parse(dataOriginale), LocalTime.parse(oraInizioOriginale));
		variazioniLocale.removeIf(variaz -> variaz.getInsegnamento().equals(insegnamento)&&variaz.getDataOriginale().equals(LocalDate.parse(dataOriginale))&&variaz.getNuovaOraInizio().equals(LocalTime.parse(oraInizioOriginale)));
	}

	/**
	 * Aggiunge un vincolo di indisponibilità ({@link VincoloDocente}) da parte dell'account docente attuale.
	 * @param giornoSettimana Il giorno dell'indisponibilità.
	 * @param oraInizio L'ora di inizio dell'indisponibilità.
	 * @param oraFine L'ora di fine dell'indisponibilità.
	 * @throws SQLException In caso di errori nel database.
	 */
	public void aggiungiVincoloDocente(String giornoSettimana, String oraInizio, String oraFine) throws SQLException {
		VincoloDocente vincolo =  new VincoloDocente(docenteAttivo.getEmail(), GiornoSettimana.valueOf(giornoSettimana), LocalTime.parse(oraInizio), LocalTime.parse(oraFine));
		vincoliLocale.add(vincolo);
		VincoloDocenteDAO v = new VincoloDocentePostgresDAO();
		v.creaVincolo(vincolo.getDocente(), vincolo.getGiorno().toString(), vincolo.getOraInizio(), vincolo.getOraFine());
	} //fine CreaVincolo

	public void eliminaVincoloDocente (String email, String giorno, String oraInizio ) throws SQLException {
		VincoloDocenteDAO v = new VincoloDocentePostgresDAO();
		v.eliminaVincoloDocente(email, giorno, LocalTime.parse(oraInizio));
		vincoliLocale.removeIf(vincolo -> vincolo.getDocente().equals(email) && vincolo.getGiorno().equals(GiornoSettimana.valueOf(giorno))&&vincolo.getOraInizio().equals(LocalTime.parse(oraInizio)));
	}

	/**
	 * Accetta l'email di un docente e restituisce i suoi insegnamenti.
	 * @return ArrayList di stringhe contenente gli insegnamenti di un determinato docente. Le stringhe sono della forma: "[nome], anno [X], [Y] CFU"
	 * @throws SQLException In caso di errori nel database.
	 */
	public List<String> getInsegnamentiDocente() throws SQLException {
		InsegnamentoDAO i = new InsegnamentoPostgresDAO();
		ResultSet rs = i.getInsegnamentiDocente(docenteAttivo.getEmail());

		ArrayList<Insegnamento> insegnamentiArr = new ArrayList<>();
		while(rs.next()) {
			Insegnamento insegnamento = new Insegnamento(rs.getString("nome"), rs.getInt("numerocfu"), rs.getInt("anno_accademico"), docenteAttivo.getEmail() );
			insegnamentiArr.add(insegnamento);
		} insegnamentiLocale = insegnamentiArr;

		ArrayList<String> insegnamentiStr = new ArrayList<>();
		for(Insegnamento ins : insegnamentiArr) {
			insegnamentiStr.add(ins.getNome() + ", anno " + ins.getAnnoAccademico() + ", " + ins.getNumeroCFU() + "CFU" );
		}
		return insegnamentiStr;
	}


	public List<String> getInsegnamenti() throws SQLException {

		InsegnamentoDAO i = new InsegnamentoPostgresDAO();
		try(ResultSet rs = i.getInsegnamenti()) {
			istanziaInsegnamenti(rs);
		}
		return formatInsegnamenti(insegnamentiLocale);
	}

	void istanziaInsegnamenti(ResultSet rs) throws SQLException {
		ArrayList<Insegnamento> insegnamentiArr = new ArrayList<>();
		while(rs.next()) {
			Insegnamento insegnamento = new Insegnamento(rs.getString("nome"), rs.getInt("numerocfu"), rs.getInt("anno_accademico"), rs.getString("email_docente") );
			insegnamentiArr.add(insegnamento);
		} insegnamentiLocale = insegnamentiArr;
	}

	List<String> formatInsegnamenti(ArrayList<Insegnamento> insegnamentiArr) {

		ArrayList<String> insegnamentiStr = new ArrayList<>();
		for(Insegnamento ins : insegnamentiArr) {
			insegnamentiStr.add(ins.getNome() + ", anno " + ins.getAnnoAccademico() + ", " + ins.getNumeroCFU() + " CFU" );
		}
		return insegnamentiStr;
	}


	public void eliminaInsegnamento(String nomeInsegnamento) throws SQLException {
		InsegnamentoDAO g = new InsegnamentoPostgresDAO();
		g.eliminaInsegnamento(nomeInsegnamento);
	}


	/**
	 * Restituisce tutte le richieste di spostamento attualmente in attesa di risposta.
	 * @return ArrayList di stringhe della forma: "Lezione di [insegnamento] giorno [gg/mm/yyyy] ore [hh:mm] -> giorno [gg/mm/yyyy] ore [hh:mm]"
	 * @throws SQLException In caso di errori nel database.
	 */
	public Map<Integer, String>getRegistroRichiesteSpostamento() throws SQLException {
		RichiestaDAO r = new RichiestaPostgresDAO();
		ArrayList<Richiesta> richiesteArr = new ArrayList<>();
		try(ResultSet rs = r.getRegistroRichiesteSpostamento()) {
			while(rs.next()) {
				Richiesta richiesta = new Richiesta(rs.getString("insegnamento"), rs.getDate("data_originale").toLocalDate(), rs.getDate("data_richiesta").toLocalDate(), rs.getTime("ora_inizio_originale").toLocalTime(), rs.getTime("ora_inizio").toLocalTime(), rs.getTime("ora_fine").toLocalTime(), rs.getString("aula"), rs.getInt("id_richiesta"));
				richiesteArr.add(richiesta);
			}
		} richiesteLocale = richiesteArr;
		Map<Integer, String> richiesteStr = new HashMap<>();
		for(Richiesta richiesta : richiesteArr) {
			String formatRichiesta = "Lezione di " + richiesta.getInsegnamento() + " giorno " + richiesta.getGiornoOriginale() + " ore " + richiesta.getOraInizioOriginale() + " -> giorno " + richiesta.getGiornoRichiesto() + " ore " + richiesta.getOraInizioRichiesta() + " - " + richiesta.getOraFineRichiesta() + "aula" + richiesta.getAula();
			richiesteStr.put(richiesta.getIdRichiesta(), formatRichiesta);
		}

		return richiesteStr;
	}

	/**
	 * Approva una richiesta di spostamento di una lezione. La richiesta viene eliminata dal database, e inserisce una variazione corrispondente chiamando {@link #creaVariazione}.
	 * @param idRichiesta L'identificativo della richiesta da approvare.
	 * @throws SQLException In caso di errori nel database.
	 */
	public void approvaRichiesta(int idRichiesta) throws SQLException {
		RichiestaDAO r = new RichiestaPostgresDAO();

		try(ResultSet rs = r.cancellaRichiesta(idRichiesta)){
			if(rs.next()) {
				creaVariazione(rs.getString ("insegnamento" ), rs.getString("data_originale"), rs.getString("data_richiesta"), rs.getString("ora_inizio_originale"), rs.getString("ora_inizio"), rs.getString("ora_fine"), rs.getString("aula"));
			}
			else{throw new SQLException("La richiesta non esiste!");
			}
		}
		richiesteLocale.removeIf(richiesta ->  richiesta.getIdRichiesta() == (idRichiesta));

	}

	/**
	 * Rifiuta una richiesta di spostamento di una lezione. La richiesta viene eliminata dal database.
	 * @param idRichiesta L'identificativo della richiesta da rifiutare.
	 * @throws SQLException In caso di errori nel database.
	 */
	public void rifiutaRichiesta(int idRichiesta) throws SQLException {
		RichiestaDAO r = new RichiestaPostgresDAO();

		try (ResultSet rs = r.cancellaRichiesta(idRichiesta)){
			if(!rs.next()) {
				throw new SQLException("La richiesta non esiste!");
			}
		}
		richiesteLocale.removeIf(richiesta ->  richiesta.getIdRichiesta() == (idRichiesta));
	}


	/**
	 * Restituisce tutti i vincoli di indisponibilità inseriti da ogni docente.
	 * @return Hashmap che usa come chiave l'email di un docente e come valore un ArrayList di stringhe contenente i suoi vincoli inseriti. Le stringhe sono della forma "Giorno: [gg/mm/yyyy], ore: [hh:mm] - [hh:mm]".
	 * @throws SQLException In caso di errori nel database.
	 */
	public Map<String, ArrayList<String>> getRegistroVincoliDocenti() throws SQLException{
		HashMap<String, ArrayList<String>> h = new HashMap<>();
		VincoloDocenteDAO v = new VincoloDocentePostgresDAO();
		ArrayList<VincoloDocente> vincoliArr = new ArrayList<>();

		try (ResultSet rs = v.getVincoliR()) {
			while(rs.next()) {
				VincoloDocente vincolo = new VincoloDocente(rs.getString("email_docente"), GiornoSettimana.valueOf(rs.getString("giorno")), LocalTime.parse(rs.getString("ora_inizio")), LocalTime.parse(rs.getString("ora_fine")));
				vincoliArr.add(vincolo);
			}
			vincoliLocale = vincoliArr;

			for(VincoloDocente vincolo : vincoliArr) {
				String s = "Giorno: "+ vincolo.getGiorno() + ", ore: " + vincolo.getOraInizio() + " - " + vincolo.getOraFine();

				h.get(rs.getString("email_docente")).add(s);
			}

		} return h;
	}

	/**
	 * Restituisce le variazioni relative alle lezioni di un docente.
	 * @return Un ArrayList di stringhe contenente i vincoli di indisponibilità inseriti da un docente. Le stringhe sono della forma "Giorno: [gg/mm/yyyy], ore: [hh:mm] - [hh:mm]".
	 * @throws SQLException In caso di errori nel database.
	 */
	public List<String> getVincoliDocente() throws SQLException{
		ArrayList<VincoloDocente> vincoliArr = new ArrayList<>();

		VincoloDocenteDAO v = new VincoloDocentePostgresDAO();

		try (ResultSet rs = v.getVincoli(docenteAttivo.getEmail())){
			while(rs.next()) {
				VincoloDocente vincolo = new VincoloDocente(docenteAttivo.getEmail(), GiornoSettimana.valueOf(rs.getString("giorno")), rs.getTime("ora_inizio").toLocalTime(), rs.getTime("ora_fine").toLocalTime());
				vincoliArr.add(vincolo);
			}
		}
		vincoliLocale = vincoliArr;
		ArrayList<String> arr = new ArrayList<>();

		for(VincoloDocente vincolo : vincoliLocale) {
			arr.add("Giorno: "+ vincolo.getGiorno() + ", ore: " + vincolo.getOraInizio() + "-" + vincolo.getOraFine());
		}

		return arr;
	}

	/**
	 * Restituisce le variazioni relative alle lezioni di un anno accademico.
	 * @return Un ArrayList di stringhe contenente tutte le variazioni. Le stringhe sono della forma "[insegnamento]: [gg/mm/yyyy] ore [hh:mm] spostata a [gg/mm/yyyy] ore [hh:mm]-[hh:mm] "
	 * @throws SQLException In caso di errori nel database.
	 */
	public List<String> getVariazioniStudente() throws SQLException{
		VariazioneDAO v = new VariazionePostgresDAO();
		ResultSet rs = v.getVariazioni(studenteAttivo.getAnnoAccademico());
		istanziaVariazione(rs);
		return formatVariazioni(variazioniLocale);
	}



	private void istanziaVariazione(ResultSet rs) throws SQLException{


		ArrayList<Variazione> arrVariazione = new ArrayList<>();

			while (rs.next()) {
				String insegnamento = rs.getString("insegnamento");
				LocalDate dataOriginale = rs.getDate("data_originale").toLocalDate();
				LocalDate nuovaData = rs.getDate("nuova_data").toLocalDate();
				LocalTime nuovaOraInizio = rs.getTime("ora_inizio").toLocalTime();
				LocalTime nuovaOraFine = rs.getTime("ora_fine").toLocalTime();
				LocalTime oraInizioOriginale = rs.getTime("ora_inizio_originale").toLocalTime();
				String aula = rs.getString("aula");


				Variazione v = new Variazione(dataOriginale, nuovaData, nuovaOraInizio, nuovaOraFine, oraInizioOriginale, insegnamento, aula);
				arrVariazione.add(v);
			}
			variazioniLocale = arrVariazione;



	}




	public List<String> formatVariazioni (List<Variazione> variazioniArr){

		ArrayList<String> stringArr = new ArrayList<>();

		for(Variazione variazione : variazioniArr){
			stringArr.add(variazione.getInsegnamento() + ": "+ variazione.getDataOriginale() +" ore "+variazione.getOraInizioOriginale()+" spostata a "+ variazione.getNuovaData() +" ore "+ variazione.getNuovaOraInizio() + "-"+ variazione.getNuovaOraFine() +" nell'aula " + variazione.getAula());
			}
		return stringArr;

	}




	/**
	 * Restituisce le variazioni relative alle lezioni di un docente.
	 * @return Un ArrayList di stringhe contenente tutte le variazioni. Le stringhe sono della forma "[insegnamento]: [gg/mm/yyyy] ore [hh:mm] spostata a [gg/mm/yyyy] ore [hh:mm]-[hh:mm] "
	 * @throws SQLException In caso di errori nel database.
	 */
	public List<String> getVariazioniDocente() throws SQLException{
		String email = docenteAttivo.getEmail();
		VariazioneDAO v = new VariazionePostgresDAO();
		ResultSet rs = v.getVariazioni(email);
		istanziaVariazione(rs);
		return formatVariazioni(variazioniLocale);
	}


	public void creaAula (String nomeAula, int capienzaMassima) throws SQLException {
		Aula aula = new Aula(nomeAula, capienzaMassima);
		auleLocale.add(aula);
		AulaDAO a = new AulaPostgresDAO();
		a.creaAula(aula.getNomeAula(), aula.getCapienzaMassima());
	}


	public void eliminaAula (String nomeAula) throws SQLException {
		auleLocale.removeIf(aula ->  aula.getNomeAula().equals(nomeAula));
		AulaDAO a = new AulaPostgresDAO();
		a.eliminaAula(nomeAula);
	}


	public List<String> getAule() throws SQLException {
		ArrayList<Aula> auleArr = new ArrayList<>();

		AulaDAO a = new AulaPostgresDAO();
		ArrayList<String> stringArr = new ArrayList<>();
		try(ResultSet rs = a.getAule()){
			while(rs.next()) {
				Aula aula = new Aula(rs.getString("nome_aula"), rs.getInt("capienza_massima"));
				auleArr.add(aula);
			}
		}
		auleLocale = auleArr;
		for(Aula aula : auleLocale) {
			stringArr.add(aula.getNomeAula()); //+ ": " + rs.getString("capienza_massima")
		}
		return stringArr;
	}

}






