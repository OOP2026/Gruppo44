package controller;

import dao.UtenteDAO;
import jdk.vm.ci.meta.Local;
import model.*;
import java.time.LocalTime;
import java.time.LocalDate;
import database_connection.ConnessioneDatabase;

public class Controller {

	public Controller() {
	}

	private static Controller instance;

	public void Login(String email, String password) {

	} // fine Login

	public void Logout() {

	} // fine Logout

	public void Signin(String username, String password) {

	} //fine Signin

	public void GetLezioni(Insegnamento insegnamento) {

	} //fine GetLezioni


	public void InviaRichiesta(String nomeInsegnamento, String oraOriginale, LocalDate giornoOriginale, LocalDate giornoRichiesto, LocalTime oraInizioRichiesta, LocalTime oraFineRichiesta) {

	} // fine InviaRichiesta

	public void CreaUtente(String nome, String cognome, String email, String password, String tipo) {


	} // fine CreaUtente

	public void CreaLezione(String nomeInsegnamento, GiornoSettimana giornoSettimana, LocalTime oraInizio, LocalTime oraFine, Aula aula) {

	} // fine CreaLezione

	public void CreaInsegnamento(String nome, Integer numeroCFU, Integer anno, Docente docente ) {

	} //fine CreaInsegnamento

	public void CreaVariazione(String idLezione, LocalDate giornoOriginale, LocalDate nuovaData, LocalTime oraInizio, LocalTime oraFine){
								      //idLezione è temporaneo, non so come identificare una singola lezione altrimenti
	} //fine CreaVariazione


}
