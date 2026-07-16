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
	 * Esegue la procedura di identificazione per un docente nel sistema verificando le credenziali fornite: accetta
	 * come parametri un'email e una password come {@link String}, e restituisce nome, cognome ed
	 * email del docente corrispondente, se esiste e la password è corretta.
	 * <p>
	 * Il metodo verifica la validità delle credenziali interagendo con il database tramite
	 * {@link DocentePostgresDAO#login(String, String)}. Se le credenziali sono valide, il database restituisce
	 * un {@link ResultSet} contenente i dati del profilo che vengono utilizzati per inizializzare
	 * un'istanza di {@link Docente}, rappresentante l'utente loggato ({@code docenteAttivo}). Infine restituisce i
	 * dati anagrafici essenziali in un array di stringhe per la visualizzazione nell'interfaccia utente.
	 *
	 * @param email L'indirizzo email inserito dall'utente come identificativo per eseguire l'accesso.
	 * @param password La password associata all'account del docente inserita dall'utente per eseguire l'accesso.
	 * @return Un array di {@link String} contenente nell'ordine: nome, cognome, email del docente.
	 * @throws SQLException Se si verifica un errore durante l'interazione con il database
	 * o se le credenziali non sono valide o sono sbagliate.
	 * @see Docente
	 * @see DocentePostgresDAO#login(String, String)
	 */
	public String[] loginDocente(String email, String password) throws SQLException {
		DocenteDAO d = new DocentePostgresDAO();
		try (ResultSet rs = d.login(email, password)){
			docenteAttivo = new Docente(rs.getString("nome"), rs.getString("cognome"), rs.getString("email"), rs.getString("password"));
			return new String[] {docenteAttivo.getNome(), docenteAttivo.getCognome(), docenteAttivo.getEmail()};
		}

	} // fine Login


	/**
	 * Esegue la procedura di identificazione per uno studente nel sistema verificando le credenziali fornite: accetta
	 * come parametri un'email e una password come {@link String}, e restituisce nome, cognome,
	 * email, matricola e anno accademico dello studente corrispondente, se esiste e la password è corretta.
	 * <p>
	 * Il metodo verifica la validità delle credenziali interagendo con il database tramite
	 * {@link StudentePostgresDAO#login(String, String)}. Se le credenziali sono valide, il database restituisce
	 * un {@link ResultSet} contenente i dati del profilo che vengono utilizzati per inizializzare
	 * un'istanza di {@link Studente}, rappresentante l'utente loggato ({@code studenteAttivo}). Infine restituisce i
	 * dati anagrafici e accademici essenziali in un array di stringhe per la visualizzazione nell'interfaccia utente.
	 *
	 * @param email L'indirizzo email inserito dall'utente come identificativo per eseguire l'accesso.
	 * @param password La password associata all'account del docente inserita dall'utente per eseguire l'accesso.
	 * @return Un array di {@link String} contenente, in ordine: nome, cognome, email, matricola e anno accademico dello studente.
	 * @throws SQLException Se si verifica un errore durante l'interazione con il database
	 * o se le credenziali non sono valide o sono sbagliate.
	 * @see Studente
	 * @see StudentePostgresDAO#login(String, String)
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
	 * Recupera l'elenco completo di tutte le lezioni erogate da un docente.
	 * <p>
	 * Il metodo interagisce con il database tramite {@link LezionePostgresDAO#getLezioni(String)} utilizzando l'email
	 * dell'utente corrente ({@code docenteAttivo}) come chiave di ricerca. Viene restituito un {@link ResultSet}
	 * con i dati delle lezioni e il sistema procede all'istanza degli oggetti della {@link Lezione} in locale ({@code istanziaLezioni}
	 * e alla successiva formattazione dei dati (tramite {@link #formatLezioni(ArrayList)}) per la visualizzazione nell'interfaccia utente.
	 *
	 * @return Un {@link ArrayList} di {@link String} contenente le lezioni formattate secondo le
	 * specifiche definite in {@link #formatLezioni(ArrayList)}.
	 * @throws SQLException Se si verifica un errore durante l'interazione con il database.
	 * @see Lezione
	 * @see LezionePostgresDAO#getLezioni(String)
	 * @see #formatLezioni(ArrayList)
	 */
	public ArrayList<String>[] getLezioniDocente() throws SQLException{
		LezioneDAO l = new LezionePostgresDAO();
		ResultSet rs = l.getLezioni(docenteAttivo.getEmail());
		istanziaLezioni(rs);
		return formatLezioni(lezioniLocale);
	}

	/**
	 * Recupera l'elenco completo delle lezioni disponibili per lo studente in base al suo anno accademico.
	 * <p>
	 * Il metodo interroga il database tramite {@link LezionePostgresDAO#getLezioni(int)} utilizzando l'anno
	 * accademico dell'utente corrente ({@code studenteAttivo}) come filtro di ricerca. Viene restituito un
	 * {@link ResultSet} con i dati delle lezioni e il sistema procede all'istanza degli oggetti {@link Lezione} in locale ({@code istanziaLezioni}) e
	 * alla successiva formattazione dei dati (tramite {@link #formatLezioni(ArrayList)}) necessari per la visualizzazione nell'interfaccia utente.
	 *
	 * @return Un {@link ArrayList} di {@link String} contenente le lezioni formattate secondo le
	 * specifiche definite in {@link #formatLezioni(ArrayList)}.
	 * @throws SQLException Se si verifica un errore durante l'interazione con il database.
	 * @see Lezione
	 * @see LezionePostgresDAO#getLezioni(int)
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
	 * Trasforma una collezione di oggetti {@link Lezione} in una struttura formattata in stringhe per la visualizzazione nell'interfaccia utente.
	 * <p>
	 * Il metodo organizza le lezioni in un array di {@link ArrayList}, dove ogni indice corrisponde a
	 * un giorno della settimana. Per ogni {@link Lezione}, il metodo estrae i dati
	 * relativi all'orario, all'insegnamento e all'aula, concatenandoli in una stringa dal
	 * formato standard: "[oraInizio]-[oraFine]: [insegnamento] (aula [aula])".
	 * Ogni stringa così generata viene inserita nella lista corrispondente al giorno in cui si tiene la lezione.
	 *
	 * @param lezioniArr La lista di oggetti {@link Lezione} da elaborare.
	 * @return Un array di {@link ArrayList} di {@link String}, dove ogni posizione corrisponde a un
	 * giorno della settimana contenente le relative stringhe formattate nel pattern:
	 * "[oraInizio]-[oraFine]: [insegnamento] (aula [aula])".
	 * @see Lezione
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


	/**
	 * Elabora i dati contenuti in un {@link ResultSet} per popolare la lista locale ({@code istanziaLezioni}) delle lezioni.
	 * <p>
	 * Il metodo scorre i record, presenti nel {@link ResultSet}, ricevuti dalla query al database, estraendo i campi relativi al
	 * giorno della settimana, agli orari, all'aula e all'insegnamento. Per ogni record, viene
	 * istanziato un oggetto {@link Lezione}, che viene poi aggiunto alla collezione locale
	 * ({@code lezioniLocale}) per rendere i dati disponibili per le successive operazioni di formattazione e
	 * visualizzazione.
	 *
	 * @param rs Il {@link ResultSet} ottenuto dall'interazione con il database, contenente
	 * i dati delle lezioni da processare.
	 * @throws SQLException Se si verifica un errore durante la lettura dei dati dal {@link ResultSet}.
	 * @see Lezione
	 */
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
	 * Aggiunge al database una richiesta di spostamento lezione da parte di un docente.
	 * <p>
	 * Il metodo crea un'istanza di {@link Richiesta} utilizzando i parametri forniti, la aggiunge
	 * alla collezione locale ({@code richiesteLocale}) e successivamente interagisce con il database
	 * tramite {@link RichiestaPostgresDAO#aggiungiRichiestaSpostamento(String, LocalTime, LocalDate, LocalDate, LocalTime, LocalTime, String)}
	 * per persistere il nuovo record. I parametri temporali vengono convertiti da {@link String} ai
	 * formati {@link LocalDate} e {@link LocalTime} per garantire la validità formale delle date e degli orari, consentire
	 * operazioni di confronto logico (come la verifica di sovrapposizioni) e assicurare la
	 *  corretta persistenza nel database, che richiede formati tipizzati e non semplici stringhe.
	 *
	 * @param nomeInsegnamento Il nome dell'insegnamento relativo alla richiesta.
	 * @param oraInizioOriginale L'orario di inizio originale della lezione (formato: HH:MM).
	 * @param dataOriginale La data originale della lezione (formato: YYYY-MM-DD).
	 * @param dataRichiesta La nuova data proposta per la lezione dal docente (formato: YYYY-MM-DD).
	 * @param oraInizioRichiesta La nuova ora di inizio richiesta dal docente (formato: HH:MM).
	 * @param oraFineRichiesta La nuova ora di fine richiesta dal docente (formato: HH:MM).
	 * @param aula La nuova aula assegnata alla lezione modificata.
	 * @throws SQLException Se si verifica un errore durante l'interazione con il database.
	 * @see Richiesta
	 * @see RichiestaPostgresDAO#aggiungiRichiestaSpostamento(String, LocalTime, LocalDate, LocalDate, LocalTime, LocalTime, String)
	 */
	public void aggiungiRichiestaSpostamento(String nomeInsegnamento, String oraInizioOriginale, String dataOriginale, String dataRichiesta, String oraInizioRichiesta, String oraFineRichiesta, String aula) throws SQLException
	{
		Richiesta richiesta = new Richiesta(nomeInsegnamento, LocalDate.parse(dataOriginale), LocalDate.parse(dataRichiesta), LocalTime.parse(oraInizioOriginale), LocalTime.parse(oraInizioRichiesta), LocalTime.parse(oraFineRichiesta), aula);
		richiesteLocale.add(richiesta);
		RichiestaDAO r = new RichiestaPostgresDAO();
		r.aggiungiRichiestaSpostamento(richiesta.getInsegnamento(), richiesta.getOraInizioOriginale(), richiesta.getGiornoOriginale(), richiesta.getGiornoRichiesto(), richiesta.getOraInizioRichiesta(), richiesta.getOraFineRichiesta(), aula);

	} // fine InviaRichiesta



	/**
	 * Registra un nuovo utente di tipo {@link Docente} nel database ed esegue automaticamente il login
	 * chiamando {@link #loginDocente(String, String)}.
	 * <p>
	 * Il metodo interagisce con il database tramite {@link DocentePostgresDAO#creaDocente(String, String, String, String)}
	 * per persistere il nuovo record del docente. In seguito, viene richiamato automaticamente
	 * {@link #loginDocente(String, String)} per inizializzare lo stato della sessione
	 * dell'utente ({@code docenteAttivo}) e restituire immediatamente i suoi dati
	 * anagrafici necessari per la visualizzazione nell'interfaccia utente.
	 *
	 * @param nome Il nome del docente.
	 * @param cognome Il cognome del docente.
	 * @param email L'indirizzo email utilizzato come identificativo univoco.
	 * @param password La password associata all'account.
	 * @return Un array di {@link String} contenente, nell'ordine: nome, cognome ed email del docente.
	 * @throws SQLException Se si verifica un errore durante la persistenza o l'autenticazione.
	 * @see Docente
	 * @see DocentePostgresDAO#creaDocente(String, String, String, String)
	 * @see #loginDocente(String, String)
	 */
	public String[] creaDocente(String nome, String cognome, String email, String password) throws SQLException{
		DocenteDAO d = new DocentePostgresDAO();
		d.creaDocente(nome, cognome, email, password);
		return loginDocente(email, password);
	} // fine CreaUtente


	/**
	 * Rimuove un docente dal database in modo permanente.
	 * <p>
	 * Il metodo interagisce con il database tramite {@link DocentePostgresDAO#eliminaDocente(String)}
	 * per eliminare il record associato all'indirizzo email specificato.
	 *
	 * @param email L'indirizzo email che identifica univocamente il docente da eliminare.
	 * @throws SQLException Se si verifica un errore durante l'interazione con il database.
	 * @see DocentePostgresDAO#eliminaDocente(String)
	 */
	public void eliminaDocente(String email) throws SQLException {
		DocenteDAO d = new DocentePostgresDAO();
		d.eliminaDocente(email);
	}

	/**
	 * Registra un nuovo utente di tipo {@link Studente} nel database ed esegue automaticamente il login
	 * chiamando {@link #loginStudente(String, String)}.
	 * <p>
	 * Il metodo interagisce con il database tramite {@link StudentePostgresDAO#creaStudente(String, String, String, String, String, int)}
	 * per persistere il nuovo record dello studente. In seguito, viene richiamato automaticamente
	 * {@link #loginStudente(String, String)} per inizializzare lo stato della sessione
	 * dell'utente ({@code studenteAttivo}) e restituire i dati anagrafici
	 * e accademici per la visualizzazione nell'interfaccia utente.
	 *
	 * @param nome Il nome dello studente.
	 * @param cognome Il cognome dello studente.
	 * @param email L'indirizzo email utilizzato come identificativo univoco.
	 * @param password La password associata all'account.
	 * @param matricola Il numero di matricola dello studente.
	 * @param anno L'anno accademico di iscrizione dello studente.
	 * @return Un array di {@link String} contenente, nell'ordine: nome, cognome, email, matricola e anno accademico dello studente.
	 * @throws SQLException Se si verifica un errore durante la persistenza o l'autenticazione.
	 * @see Studente
	 * @see StudentePostgresDAO#creaStudente(String, String, String, String, String, int)
	 * @see #loginStudente(String, String)
	 */
	public String[] creaStudente(String nome, String cognome, String email, String password, String matricola, int anno) throws SQLException {
		Studente studente = new Studente(nome, cognome, email, password, matricola, anno);
		StudenteDAO s = new StudentePostgresDAO();
		s.creaStudente(studente.getNome(), studente.getCognome(), studente.getEmail(), studente.getPassword(), studente.getMatricola(), studente.getAnnoAccademico());
		return loginStudente(email, password);
	} // fine CreaUtente


	/**
	 * Rimuove uno studente dal database in modo permanente.
	 * <p>
	 * Il metodo interagisce con il database tramite {@link StudentePostgresDAO#eliminaStudente(String)}
	 * per eliminare il record associato all'indirizzo email specificato.
	 *
	 * @param email L'indirizzo email che identifica univocamente lo studente da eliminare.
	 * @throws SQLException Se si verifica un errore durante l'interazione con il database.
	 * @see StudentePostgresDAO#eliminaStudente(String)
	 */
	public void eliminaStudente(String email) throws SQLException {
		StudenteDAO s = new StudentePostgresDAO();
		s.eliminaStudente(email);
	}

	/**
	 * Aggiunge una nuova {@link Lezione} settimanale nel database.
	 * <p>
	 * Il metodo istanzia un oggetto {@link Lezione} a partire dai parametri forniti,
	 * convertendo le stringhe di orario e giorno nei rispettivi formati {@link LocalTime}
	 * e {@link GiornoSettimana}, per garantire la conformità al modello logico dell'applicazione
	 * e una corretta validazione dei dati prima della persistenza.
	 * L'oggetto creato viene aggiunto a {@code lezioniLocale} e successivamente persistito
	 * nel database tramite {@link LezionePostgresDAO#creaLezione(String, LocalTime, LocalTime, String, String)}.
	 *
	 * @param nomeInsegnamento Il nome dell'insegnamento relativo alla lezione.
	 * @param giornoSettimana Il giorno settimanale in cui la lezione viene erogata.
	 * @param oraInizio L'orario di inizio della lezione (formato: HH:MM).
	 * @param oraFine L'orario di fine della lezione (formato: HH:MM).
	 * @param nomeAula Il nome dell'aula assegnata alla lezione.
	 * @throws SQLException Se si verifica un errore durante l'interazione con il database.
	 * @see Lezione
	 * @see LezionePostgresDAO#creaLezione(String, LocalTime, LocalTime, String, String)
	 */
	public void creaLezione(String nomeInsegnamento, String giornoSettimana, String oraInizio, String oraFine, String nomeAula) throws SQLException {
		Lezione lezione = new Lezione(GiornoSettimana.valueOf(giornoSettimana.toUpperCase()), LocalTime.parse(oraInizio), LocalTime.parse(oraFine), nomeAula, nomeInsegnamento);
		lezioniLocale.add(lezione);
		LezioneDAO l = new LezionePostgresDAO();
    	l.creaLezione(lezione.getGiornoSettimana().toString(), lezione.getOraInizio(), lezione.getOraFine(), lezione.getAula(), lezione.getInsegnamento());
	} // fine CreaLezione



	/**
	 * Aggiunge un nuovo {@link Insegnamento} nel database.
	 * <p>
	 * Il metodo istanzia un oggetto {@link Insegnamento} associandolo al docente attualmente
	 * autenticato ({@code docenteAttivo}). L'oggetto creato viene aggiunto a
	 * {@code insegnamentiLocale} e, successivamente, salvato nel database
	 * tramite {@link InsegnamentoPostgresDAO#creaInsegnamento(String, int, int, String)}
	 * per garantirne la persistenza.
	 *
	 * @param nome Il nome dell'insegnamento.
	 * @param numeroCFU Il numero di CFU accreditati dall'insegnamento.
	 * @param anno L'anno accademico in cui l'insegnamento viene erogato.
	 * @throws SQLException Se si verifica un errore durante l'interazione con il database.
	 * @see Insegnamento
	 * @see InsegnamentoPostgresDAO#creaInsegnamento(String, int, int, String)
	 */
	public void creaInsegnamento(String nome, int numeroCFU, int anno) throws SQLException {
		Insegnamento insegnamento = new Insegnamento(nome, numeroCFU, anno, docenteAttivo.getEmail());
		insegnamentiLocale.add(insegnamento);
		InsegnamentoDAO i = new InsegnamentoPostgresDAO();
		i.creaInsegnamento(insegnamento.getNome(), insegnamento.getNumeroCFU(), insegnamento.getAnnoAccademico(), insegnamento.getDocente());
	} //fine CreaInsegnamento


	/**
	 * Aggiorna i dati di un insegnamento esistente nel database e ne ripristina la coerenza delle lezioni associate.
	 * <p>
	 * Il metodo utilizza un for per individuare l'insegnamento all'interno della struttura dati locale
	 * ({@code insegnamentiLocale}) tramite il nome fornito.
	 * Una volta trovato, aggiorna i valori di CFU, anno accademico e
	 * docente responsabile direttamente nell'oggetto in memoria. In seguito,
	 * l'aggiornamento viene esteso al database tramite {@link InsegnamentoPostgresDAO#aggiornaInsegnamento(String, int, int, String)}
	 * per mantenere la persistenza sincronizzata. Infine, il metodo invoca
	 * {@link LezioneDAO#eliminaLezioniInsegnamento(String)} per rimuovere le lezioni
	 * precedentemente associate, garantendo così l'integrità dei dati.
	 *
	 * @param nomeInsegnamento Il nome dell'insegnamento da aggiornare, utilizzato come chiave di ricerca.
	 * @param numeroCFU Il nuovo numero di CFU da assegnare.
	 * @param anno Il nuovo anno accademico.
	 * @param emailDocente L'email del docente responsabile dell'insegnamento.
	 * @throws SQLException Se si verifica un errore durante l'aggiornamento nel database o
	 * durante la pulizia delle lezioni associate.
	 * @see Insegnamento
	 * @see InsegnamentoPostgresDAO#aggiornaInsegnamento(String, int, int, String)
	 * @see LezionePostgresDAO#eliminaLezioniInsegnamento(String)
	 */
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
	 * Registra una nuova {@link Variazione} di lezione nel database.
	 * <p>
	 * Il metodo istanzia un oggetto {@link Variazione} convertendo i parametri di ingresso: le stringhe
	 * contenenti date e orari vengono convertite nei tipi {@link LocalDate} e {@link LocalTime} per
	 * garantire la conformità al modello logico dell'applicazione e una corretta validazione
	 * dei dati prima della persistenza. L'oggetto creato viene aggiunto a {@code variazioniLocale}
	 * e successivamente salvato nel database tramite {@link VariazionePostgresDAO#creaVariazione(String, LocalDate, LocalDate, LocalTime, LocalTime, LocalTime, String)}.
	 *
	 * @param insegnamento Il nome dell'insegnamento a cui si riferisce la variazione.
	 * @param dataOriginale La data originale della lezione relativa nel formato "yyyy-MM-dd".
	 * @param nuovaData La nuova data programmata per la lezione modificata nel formato "yyyy-MM-dd".
	 * @param oraInizioOriginale L'orario di inizio originale della lezione nel formato "HH:mm".
	 * @param nuovaOraInizio Il nuovo orario di inizio della lezione modificata nel formato "HH:mm".
	 * @param nuovaOraFine Il nuovo orario di fine della lezione modificata nel formato "HH:mm".
	 * @param aula La nuova aula assegnata alla lezione modificata.
	 * @throws SQLException Se si verifica un errore durante l'interazione con il database.
	 * @see Variazione
	 * @see VariazionePostgresDAO#creaVariazione(String, LocalDate, LocalDate, LocalTime, LocalTime, LocalTime, String)
	 */
	public void creaVariazione(String insegnamento, String dataOriginale, String nuovaData, String oraInizioOriginale, String nuovaOraInizio, String nuovaOraFine, String aula) throws  SQLException {
		Variazione variazione = new Variazione(LocalDate.parse(dataOriginale), LocalDate.parse(nuovaData), LocalTime.parse(nuovaOraInizio), LocalTime.parse(nuovaOraFine), LocalTime.parse(oraInizioOriginale), insegnamento, aula);
		variazioniLocale.add(variazione);
		VariazioneDAO v = new VariazionePostgresDAO();
		v.creaVariazione(variazione.getInsegnamento(), variazione.getDataOriginale(), variazione.getNuovaData(), variazione.getOraInizioOriginale(), variazione.getNuovaOraInizio(), variazione.getNuovaOraFine(), variazione.getAula());
	} //fine CreaVariazione


	/**
	 * Elimina una {@link Variazione} di lezione dal database.
	 * <p>
	 * Il metodo interagisce con il {@link VariazionePostgresDAO#eliminaVariazione(String, LocalDate, LocalTime)}
	 * per eliminare in modo permanente il record corrispondente nel database,
	 * utilizzando le coordinate temporali e l'insegnamento forniti come chiavi.
	 * Contestualmente, esegue una sincronizzazione della struttura dati
	 * locale ({@code variazioniLocale}), utilizzando i parametri forniti per identificare ed
	 * eliminare l'oggetto corretto in memoria. Poiché le chiavi di ricerca sono fornite come
	 * {@link String}, queste vengono formattate nei tipi nativi {@link LocalDate}
	 * e {@link LocalTime} per garantire la conformità al modello logico.
	 *
	 * @param insegnamento Il nome dell'insegnamento relativo alla variazione da eliminare.
	 * @param dataOriginale La data originale della lezione (formato "yyyy-MM-dd").
	 * @param oraInizioOriginale L'orario di inizio originale della lezione (formato "HH:mm").
	 * @throws SQLException Se si verifica un errore durante l'eliminazione nel database.
	 * @see Variazione
	 * @see VariazionePostgresDAO#eliminaVariazione(String, LocalDate, LocalTime)
	 */
	public void eliminaVariazione(String insegnamento, String dataOriginale, String oraInizioOriginale ) throws SQLException {
		VariazioneDAO v = new VariazionePostgresDAO();
		v.eliminaVariazione(insegnamento, LocalDate.parse(dataOriginale), LocalTime.parse(oraInizioOriginale));
		variazioniLocale.removeIf(variaz -> variaz.getInsegnamento().equals(insegnamento)&&variaz.getDataOriginale().equals(LocalDate.parse(dataOriginale))&&variaz.getNuovaOraInizio().equals(LocalTime.parse(oraInizioOriginale)));
	}

	/**
	 * Aggiunge un nuovo vincolo di indisponibilità ({@link VincoloDocente}) per il docente attualmente autenticato
	 * <p>
	 * Il metodo istanzia un oggetto {@link VincoloDocente} combinando l'email del docente
	 * autenticato ({@code docenteAttivo}) con i parametri di vincolo forniti.
	 * Le stringhe di ingresso vengono convertite nei tipi
	 * {@link GiornoSettimana}, {@link LocalTime} e {@link LocalTime}. Questa trasformazione,
	 * assicura la validazione logica dei dati prima che vengano persistiti. Una volta creato,
	 * l'oggetto viene salvato in {@code vincoliLocale} e successivamente salvato nel database
	 * tramite {@link VincoloDocentePostgresDAO#creaVincolo(String, String, LocalTime, LocalTime)}.
	 *
	 * @param giornoSettimana Il giorno dell'indisponibilità (deve corrispondere ai valori dell'enumerazione {@link GiornoSettimana}).
	 * @param oraInizio L'ora di inizio dell'indisponibilità (formato "HH:mm").
	 * @param oraFine L'ora di fine dell'indisponibilità (formato "HH:mm").
	 * @throws SQLException Se si verifica un errore durante l'interazione con il database.
	 * @see VincoloDocente
	 * @see VincoloDocentePostgresDAO#creaVincolo(String, String, LocalTime, LocalTime)
	 */
	public void aggiungiVincoloDocente(String giornoSettimana, String oraInizio, String oraFine) throws SQLException {
		VincoloDocente vincolo =  new VincoloDocente(docenteAttivo.getEmail(), GiornoSettimana.valueOf(giornoSettimana), LocalTime.parse(oraInizio), LocalTime.parse(oraFine));
		vincoliLocale.add(vincolo);
		VincoloDocenteDAO v = new VincoloDocentePostgresDAO();
		v.creaVincolo(vincolo.getDocente(), vincolo.getGiorno().toString(), vincolo.getOraInizio(), vincolo.getOraFine());
	} //fine CreaVincolo


	/**
	 * Rimuove tutti i vincoli di indisponibilità associati al docente attualmente autenticato.
	 * <p>
	 * Il metodo recupera l'identificativo del docente da {@code docenteAttivo}
	 * e procede all'eliminazione di tutti i record corrispondenti nel database tramite
	 * {@link VincoloDocentePostgresDAO#eliminaVincoliDocente(String)}. Contestualmente,
	 * viene effettuata una pulizia della lista in memoria ({@code vincoliLocale}), rimuovendo tutte le istanze che presentano
	 * lo stesso identificativo docente per garantire la sincronizzazione tra lo stato
	 * persistente e quello locale.
	 *
	 * @throws SQLException Se si verifica un errore durante l'eliminazione dei dati nel database.
	 * @see VincoloDocentePostgresDAO#eliminaVincoliDocente(String)
	 */
	public void eliminaVincoliDocente () throws SQLException {
		String email = docenteAttivo.getEmail();
		VincoloDocenteDAO v = new VincoloDocentePostgresDAO();
		v.eliminaVincoliDocente(email);
		vincoliLocale.removeIf(vincolo -> vincolo.getDocente().equals(email));
	}



	/**
	 * Accetta l'email di un docente e restituisce (e formatta) l'elenco dei suoi insegnamenti.
	 * <p>
	 * Il metodo interroga il database tramite {@link InsegnamentoPostgresDAO#getInsegnamentiDocente(String)}, utilizzando l'email
	 * del {@code docenteAttivo} come filtro per ottenere un {@link ResultSet}. Ogni riga
	 * del risultato viene convertita in un oggetto {@link Insegnamento} e salvata localmente in ({@code insegnamentiLocale}).
	 * In seguito, il metodo trasforma la collezione in una lista di stringhe formattate secondo il pattern:
	 * "[nome], anno [X], [Y] CFU", rendendo i dati pronti per la visualizzazione nella UI.
	 *
	 * @return Un {@link ArrayList} di {@link String} contenente gli insegnamenti di un determinato docente. Le stringhe sono della forma: "[nome], anno [X], [Y] CFU"
	 * @throws SQLException Se si verifica un errore durante l'interazione con il database.
	 * @see Insegnamento
	 * @see InsegnamentoPostgresDAO#getInsegnamentiDocente(String)
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


	/**
	 * Recupera l'elenco completo degli insegnamenti dal database.
	 * <p>
	 * Il metodo interroga il database tramite {@link InsegnamentoPostgresDAO#getInsegnamenti()} e riceve i dati in
	 * un {@link ResultSet}; l'uso di {@code try-with-resources} garantisce che la
	 * connessione venga chiusa automaticamente appena terminata la lettura. Il metodo passa poi
	 * i dati al componente {@link #istanziaInsegnamenti(ResultSet)}, che si occupa di trasformare ogni riga
	 * in un oggetto {@link Insegnamento} e di memorizzarlo in locale in {@code insegnamentiLocale}.
	 * Infine, il metodo chiama {@link #formatInsegnamenti(ArrayList)} per prendere questi oggetti
	 * e trasformarli in semplici stringhe di testo, formattate per la visualizzazione
	 * nell'interfaccia utente.
	 *
	 * @return Una lista di {@link String} formattate con i dettagli degli insegnamenti
	 * @throws SQLException Se si verifica un errore durante l'interazione con il database.
	 * @see Insegnamento
	 * @see InsegnamentoPostgresDAO#getInsegnamenti()
	 * @see #istanziaInsegnamenti(ResultSet)
	 * @see #formatInsegnamenti(ArrayList)
	 */
	public List<String> getInsegnamenti() throws SQLException {

		InsegnamentoDAO i = new InsegnamentoPostgresDAO();
		try(ResultSet rs = i.getInsegnamenti()) {
			istanziaInsegnamenti(rs);
		}
		return formatInsegnamenti(insegnamentiLocale);
	}


	/**
	 * Converte i risultati estratti dal database in una lista di oggetti {@link Insegnamento}.
	 * <p>
	 * Il metodo scorre il contenuto del {@link ResultSet} ricevuto in ingresso, convertendo ogni record
	 * estratto in un'istanza di {@link Insegnamento}. Questi oggetti vengono raccolti in una lista
	 * temporanea e, al termine del ciclo di lettura, l'intera lista viene assegnata
	 * alla memoria locale ({@code insegnamentiLocale}), aggiornando così lo stato
	 * interno del sistema con i dati appena recuperati.
	 *
	 * @param rs Il {@link ResultSet} contenente i dati estratti dal database.
	 * @throws SQLException Se si verifica un errore durante la lettura dei dati dal database.
	 * @see Insegnamento
	 */
	void istanziaInsegnamenti(ResultSet rs) throws SQLException {
		ArrayList<Insegnamento> insegnamentiArr = new ArrayList<>();
		while(rs.next()) {
			Insegnamento insegnamento = new Insegnamento(rs.getString("nome"), rs.getInt("numerocfu"), rs.getInt("anno_accademico"), rs.getString("email_docente") );
			insegnamentiArr.add(insegnamento);
		} insegnamentiLocale = insegnamentiArr;
	}


	/**
	 * Trasforma una lista di oggetti {@link Insegnamento} in una lista di stringhe formattate per la visualizzazione.
	 * <p>
	 * Il metodo prende in ingresso la lista di oggetti {@link Insegnamento}, estraendo per
	 * ciascuno le proprietà fondamentali: nome, anno accademico e numero di CFU. Ogni
	 * insegnamento viene convertito in una stringa secondo il pattern:
	 * "[nome], anno [X], [Y] CFU", garantendo che la logica di presentazione sia coerente
	 * per l'interfaccia grafica.
	 *
	 * @param insegnamentiArr La lista di oggetti {@link Insegnamento} da elaborare.
	 * @return Una lista di stringhe formattate, ciascuna contenente i dettagli di un insegnamento.
	 * @see Insegnamento
	 */
	List<String> formatInsegnamenti(ArrayList<Insegnamento> insegnamentiArr) {

		ArrayList<String> insegnamentiStr = new ArrayList<>();
		for(Insegnamento ins : insegnamentiArr) {
			insegnamentiStr.add(ins.getNome() + ", anno " + ins.getAnnoAccademico() + ", " + ins.getNumeroCFU() + " CFU" );
		}
		return insegnamentiStr;
	}


	/**
	 * Rimuove un insegnamento dal database.
	 * <p>
	 * Il metodo si occupa di eliminare in modo permanente un insegnamento dal sistema
	 * inviando una richiesta al database tramite {@link InsegnamentoPostgresDAO#eliminaInsegnamento(String)}.
	 * Il metodo utilizza il nome fornito come riferimento univoco per individuare e
	 * cancellare correttamente il record corrispondente nella base dati.
	 *
	 * @param nomeInsegnamento Il nome dell'insegnamento che si desidera eliminare.
	 * @throws SQLException Se si verifica un errore durante la comunicazione con il database.
	 * @see InsegnamentoPostgresDAO#eliminaInsegnamento(String)
	 */
	public void eliminaInsegnamento(String nomeInsegnamento) throws SQLException {
		InsegnamentoDAO g = new InsegnamentoPostgresDAO();
		g.eliminaInsegnamento(nomeInsegnamento);
	}


	/**
	 * Recupera e formatta l'elenco di tutte le richieste di spostamento lezione in attesa.
	 * <p>
	 * Il metodo interroga il database tramite {@link RichiestaPostgresDAO#getRegistroRichiesteSpostamento()},
	 * ricevendo in risposta un {@link ResultSet}. L'uso del blocco {@code try-with-resources}
	 * garantisce che, non appena il programma finisce di leggere tutte le righe dal database,
	 * la connessione venga chiusa immediatamente.
	 * In seguito, trasforma ogni riga letta in un oggetto {@link Richiesta} e
	 * la salva nella collezione locale ({@code richiesteLocale}).
	 * Infine, crea una {@link Map} che associa l'identificativo unico di ogni richiesta (L'ID) a una
	 * descrizione testuale dettagliata, formattata per essere visualizzata dell'interfaccia grafica.
	 *
	 * @return Un {@link ArrayList} di {@link String} della forma: "Lezione di [insegnamento] giorno [gg/mm/yyyy] ore [hh:mm] -> giorno [gg/mm/yyyy] ore [hh:mm]"
	 * @throws SQLException Se si verifica un problema durante la comunicazione con il database.
	 * @see Richiesta
	 * @see RichiestaPostgresDAO#getRegistroRichiesteSpostamento()
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
	 * Approva una richiesta di spostamento di una lezione. La richiesta viene eliminata dal database, e
	 * inserisce una variazione corrispondente chiamando {@link #creaVariazione}.
	 * <p>
	 * IL metodo chiede al database di eliminare la richiesta identificata dal parametro
	 * {@code idRichiesta} tramite {@link RichiestaPostgresDAO#cancellaRichiesta(int)}; se la richiesta viene trovata e correttamente cancellata,
	 * il database restituisce i suoi dati tramite un {@link ResultSet}, altrimenti viene sollevata
	 * un'eccezione {@link SQLException} per segnalare l'errore.
	 * In secondo luogo, il metodo utilizza questi dati per richiamare {@link #creaVariazione(String, String, String, String, String, String, String)},
	 * rendendo la variazione effettiva. Infine, aggiorna la memoria locale eliminando
	 * la richiesta corrispondente dalla lista {@code richiesteLocale}, mantenendo così la coerenza
	 * tra i dati salvati e quelli mostrati all'utente.
	 *
	 * @param idRichiesta L'identificativo della richiesta da approvare.
	 * @throws SQLException Se la richiesta non esiste o se si verifica un errore durante 
	 * la comunicazione con il database.
	 * @see RichiestaPostgresDAO#cancellaRichiesta(int)
	 * @see #creaVariazione(String, String, String, String, String, String, String) 
	 */
	public void approvaRichiesta(int idRichiesta) throws SQLException {
		RichiestaDAO r = new RichiestaPostgresDAO();

		try(ResultSet rs = r.cancellaRichiesta(idRichiesta)){
			if(rs.next()) {
				creaVariazione(rs.getString ("insegnamento" ), rs.getString("data_originale"), rs.getString("data_richiesta"), rs.getString("ora_inizio_originale"), rs.getString("ora_inizio"), rs.getString("ora_fine"), rs.getString("aula"));
				richiesteLocale.removeIf(richiesta ->  richiesta.getIdRichiesta() == (idRichiesta));
			}
			else{throw new SQLException("La richiesta non esiste!");
			}
		}

	}

	/**
	 * Rifiuta una richiesta di spostamento di una lezione, eliminandola definitivamente dal database.
	 * <p>
	 * Per prima cosa, il metodo chiama {@link RichiestaPostgresDAO#cancellaRichiesta(int)} per rimuovere il record corrispondente
	 * all'id fornito; se l'operazione non trova alcuna richiesta da eliminare, il metodo
	 * solleva una eccezione {@link SQLException} per segnalare l'errore.
	 * In caso di successo, il metodo procede aggiornando la memoria locale del programma,
	 * rimuovendo la richiesta da {@code richiesteLocale}, per garantire che l'interfaccia utente
	 * rifletta correttamente l'avvenuto rifiuto.
	 *
	 * @param idRichiesta L'identificativo della richiesta da rifiutare.
	 * @throws SQLException Se la richiesta non esiste o se si verifica un errore durante
	 * la comunicazione con il database.
	 * @see RichiestaPostgresDAO#cancellaRichiesta(int)
	 */
	public void rifiutaRichiesta(int idRichiesta) throws SQLException {
		RichiestaDAO r = new RichiestaPostgresDAO();

		try (ResultSet rs = r.cancellaRichiesta(idRichiesta)){
			if(!rs.next()) {
				throw new SQLException("La richiesta non esiste!");
			}
			richiesteLocale.removeIf(richiesta ->  richiesta.getIdRichiesta() == (idRichiesta));
		}
	}


	/**
	 * Recupera l'elenco di tutti i vincoli di indisponibilità inseriti dai docenti.
	 * <p>
	 * Il metodo interroga il database tramite {@link VincoloDocentePostgresDAO#getVincoliR()} e legge i risultati
	 * in modo sicuro grazie al blocco {@code try-with-resources}. In seguito, trasforma
	 * ogni riga letta in un oggetto {@link VincoloDocente}, salvandolo nella memoria locale
	 * ({@code vincoliLocale}) per mantenere aggiornato lo stato del sistema. Infine, il metodo
	 * costruisce una {@link Map} che raggruppa le indisponibilità per docente:
	 * per ogni insegnante, crea una lista di descrizioni testuali leggibili
	 * (nel formato "Giorno: [gg/mm/yyyy], ore: [hh:mm] - [hh:mm]"),
	 * permettendo così una visualizzazione organizzata e immediata dei vincoli di
	 * tutto il corpo docente.
	 *
	 * @return Una {@link HashMap} che usa come chiave l'email di un docente e come valore un
	 * {@link ArrayList} di {@link String} contenente i suoi vincoli inseriti. Le stringhe sono
	 * della forma "Giorno: [gg/mm/yyyy], ore: [hh:mm] - [hh:mm]".
	 * @throws SQLException Se si verifica un problema durante la comunicazione con il database.
	 * @see VincoloDocente
	 * @see VincoloDocentePostgresDAO#getVincoliR()
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
	 * Recupera l'elenco dei vincoli di indisponibilità inseriti dal docente attualmente autenticato.
	 * <p>
	 * Il metodo interroga il database tramite {@link VincoloDocentePostgresDAO#getVincoli(String)}, filtrando i dati con l'email
	 * del docente attivo e gestendo il {@link ResultSet} in modo sicuro tramite {@code try-with-resources}.
	 * In seguito, converte ogni riga letta in un oggetto {@link VincoloDocente},
	 * aggiornando la memoria locale ({@code vincoliLocale}) con i nuovi dati. Infine,
	 * il metodo trasforma questi oggetti in una lista di stringhe, formattate
	 * come "Giorno: [gg/mm/yyyy], ore: [hh:mm] - [hh:mm]", pronte per essere visualizzate
	 * nell'interfaccia grafica.
	 *
	 * @return Un {@link ArrayList} di {@link String}, ciascuna contenente i dettagli di un vincolo di indisponibilità di un docente.
	 * Le stringhe sono della forma "Giorno: [gg/mm/yyyy], ore: [hh:mm] - [hh:mm]".
	 * @throws SQLException Se si verifica un problema durante la comunicazione con il database.
	 * @see VincoloDocente
	 * @see VincoloDocentePostgresDAO#getVincoli(String)
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
	 * Recupera l'elenco completo delle variazioni di lezione previste per l'anno
	 * accademico dello studente corrente.
	 * <p>
	 * Il metodo interroga il database tramite {@link VariazionePostgresDAO#getVariazioni(int)}, utilizzando l'anno accademico
	 * dello {@code studenteAttivo} come filtro per ottenere i risultati e restituendo un
	 * {@link ResultSet}. In seguito, il metodo passa questo {@link ResultSet} a {@link #istanziaVariazioni(ResultSet)},
	 * che trasforma i dati in oggetti {@link Variazione} e aggiorna la memoria locale
	 * ({@code variazioniLocale}).Infine, invoca {@link #formatVariazioni(List)}
	 * per convertire questi oggetti in stringhe testuali, organizzate nel formato
	 * "[insegnamento]: [gg/mm/yyyy] ore [hh:mm] spostata a [gg/mm/yyyy] ore [hh:mm]-[hh:mm] ",
	 * rendendole pronte per essere presentate allo studente nell'interfaccia grafica.
	 *
	 * @return Un {@link ArrayList} di {@link String} contenente tutte le variazioni. Le stringhe sono della forma
	 * "[insegnamento]: [gg/mm/yyyy] ore [hh:mm] spostata a [gg/mm/yyyy] ore [hh:mm]-[hh:mm] "
	 * @throws SQLException Se si verifica un problema durante la comunicazione con il database.
	 * @see Variazione
	 * @see VariazionePostgresDAO#getVariazioni(int)
	 * @see #istanziaVariazioni(ResultSet)
	 * @see #formatVariazioni(List)
	 */
	public List<String> getVariazioniStudente() throws SQLException{
		VariazioneDAO v = new VariazionePostgresDAO();
		ResultSet rs = v.getVariazioni(studenteAttivo.getAnnoAccademico());
		istanziaVariazioni(rs);
		return formatVariazioni(variazioniLocale);
	}


	/**
	 * Trasforma i dati provenienti dal database in una lista di oggetti {@link Variazione}.
	 * <p>
	 * Il metodo scorre riga per riga il {@link ResultSet} ricevuto in ingresso, che funge
	 * da contenitore per i dati estratti dal database. Per ogni riga trovata, il metodo
	 * estrae le informazioni necessarie (date, orari, aula e nome dell'insegnamento),
	 * utilizzandole per creare un nuovo oggetto {@link Variazione}. Questi oggetti
	 * vengono raccolti in una lista temporanea che, una volta completata la scansione
	 * di tutti i risultati, viene salvata nella memoria locale ({@code variazioniLocale}),
	 * aggiornando così lo stato interno del sistema con le variazioni correnti.
	 *
	 * @param rs Il {@link ResultSet} contenente i dati estratti dal database.
	 * @throws SQLException Se si verifica un errore durante la lettura dei dati dal database.
	 * @see Variazione
	 */
	private void istanziaVariazioni(ResultSet rs) throws SQLException{


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


	/**
	 * Trasforma una lista di oggetti {@link Variazione} in una lista di stringhe formattate per la visualizzazione.
	 * <p>
	 * Il metodo prende in ingresso una lista di oggetti {@link Variazione} e ne scorre
	 * ogni elemento singolarmente. Per ciascuna variazione, estrae tutti i dettagli
	 * necessari — come il nome dell'insegnamento, la data e l'orario originari, la
	 * nuova data, il nuovo orario e l'aula assegnata — e li combina in una stringa secondo il pattern:
	 * "[insegnamento]: [dataOriginale] ore [oraOriginale] spostata a [nuovaData] ore [nuovoOrarioInizio]-[nuovoOrarioFine] nell'aula [aula]".
	 * Ogni stringa così formattata viene aggiunta a una nuova
	 * lista, che al termine del ciclo viene restituita come risultato, pronta per
	 * essere mostrata direttamente nell'interfaccia grafica allo studente.
	 *
	 * @param variazioniArr La lista di oggetti {@link Variazione} da elaborare.
	 * @return Una {@link List} di stringhe, ciascuna rappresentante una variazione formattata.
	 * @see Variazione
	 */
	public List<String> formatVariazioni (List<Variazione> variazioniArr){

		ArrayList<String> stringArr = new ArrayList<>();

		for(Variazione variazione : variazioniArr){
			stringArr.add(variazione.getInsegnamento() + ": "+ variazione.getDataOriginale() +" ore "+variazione.getOraInizioOriginale()+" spostata a "+ variazione.getNuovaData() +" ore "+ variazione.getNuovaOraInizio() + "-"+ variazione.getNuovaOraFine() +" nell'aula " + variazione.getAula());
			}
		return stringArr;

	}




	/**
	 * Recupera le variazioni relative alle lezioni di un docente corrente.
	 * <p>
	 * Il metodo recupera le variazioni tramite {@link VariazionePostgresDAO#getVariazioni(String)},
	 * utilizzando l'email del docente autenticato ({@code docenteAttivo}).
	 * I dati vengono processati dal metodo {@link #istanziaVariazioni(ResultSet)}
	 * per popolare la collezione locale {@code variazioniLocale}. Infine il metodo invoca
	 * {@link #formatVariazioni(List)} per trasformare gli oggetti in una lista
	 * di stringhe formattate, pronte per la presentazione nell'interfaccia utente.
	 *
	 * @return Un {@link ArrayList} di {@link String} contenente tutte le variazioni. Le stringhe sono nel formato:
	 * "[insegnamento]: [gg/mm/yyyy] ore [hh:mm] spostata a [gg/mm/yyyy] ore [hh:mm]-[hh:mm] "
	 * @throws SQLException In caso di errori di comunicazione o esecuzione con il database.
	 * @see VariazionePostgresDAO#getVariazioni(String)
	 * @see #istanziaVariazioni(ResultSet)
	 * @see #formatVariazioni(List)
	 *
	 */
	public List<String> getVariazioniDocente() throws SQLException{
		String email = docenteAttivo.getEmail();
		VariazioneDAO v = new VariazionePostgresDAO();
		ResultSet rs = v.getVariazioni(email);
		istanziaVariazioni(rs);
		return formatVariazioni(variazioniLocale);
	}


	/**
	 * Crea una nuova {@link Aula} e la aggiunge al database.
	 * <p>
	 * Il metodo istanzia un nuovo oggetto {@link Aula} utilizzando i parametri forniti,
	 * lo aggiunge alla lista locale {@code auleLocale} per mantenere lo stato aggiornato in memoria
	 * e persiste l'informazione nel database tramite {@link AulaPostgresDAO#creaAula(String, int)}.
	 *
	 * @param nomeAula Il nome identificativo dell'aula da creare.
	 * @param capienzaMassima Il numero massimo di posti disponibili nell'aula.
	 * @throws SQLException In caso di errori durante l'interazione con il database.
	 * @see Aula
	 * @see AulaPostgresDAO#creaAula(String, int)
	 */
	public void creaAula (String nomeAula, int capienzaMassima) throws SQLException {
		Aula aula = new Aula(nomeAula, capienzaMassima);
		auleLocale.add(aula);
		AulaDAO a = new AulaPostgresDAO();
		a.creaAula(aula.getNomeAula(), aula.getCapienzaMassima());
	}


	/**
	 * Elimina un'aula dal database.
	 * <p>
	 * Il metodo utilizza {@link java.util.Collection#removeIf(java.util.function.Predicate)},
	 * per rimuovere da {@code auleLocale} tutte le istanze di {@link Aula}
	 * il cui nome corrisponde a quello specificato. In seguito, propaga
	 * l'eliminazione sul database tramite {@link AulaPostgresDAO#eliminaAula(String)}.
	 *
	 * @param nomeAula Il nome dell'aula da eliminare
	 * @throws SQLException In caso di errori durante l'interazione con il database.
	 * @see AulaPostgresDAO#eliminaAula(String)
	 * @see #creaAula(String, int)
	 */
	public void eliminaAula (String nomeAula) throws SQLException {
		auleLocale.removeIf(aula ->  aula.getNomeAula().equals(nomeAula));
		AulaDAO a = new AulaPostgresDAO();
		a.eliminaAula(nomeAula);
	}


	/**
	 * Recupera l'elenco di tutte le aule disponibili dal database.
	 * <p>
	 * Il metodo interroga il database tramite {@link AulaPostgresDAO#getAule()} per ottenere un
	 * {@link ResultSet} contenente i dati delle aule. Attraverso un ciclo di iterazione, ogni riga estratta
	 * viene convertita in un oggetto {@link Aula} che viene accumulato in una struttura dati temporanea.
	 * Una volta terminata la lettura, la collezione {@code auleLocale} è sincronizzata con i dati
	 * del database, garantendo la coerenza dello stato. Infine, il metodo estrae solo i nomi
	 * delle aule dalla collezione aggiornata, costruendo una lista di stringhe pronta per la visualizzazione
	 * nell'interfaccia utente.
	 *
	 * @return Un {@link ArrayList} di {@link String} contenente i nomi di tutte le aule.
	 * @throws SQLException In caso di errori durante la comunicazione con il database.
	 * @see Aula
	 * @see AulaPostgresDAO#getAule()
	 */
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






