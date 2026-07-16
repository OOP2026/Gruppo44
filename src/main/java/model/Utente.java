package model;


public class Utente {
    private String nome;
    private String cognome;
    private String email;
    private String password;

    public Utente(String nome, String cognome, String email, String password) {
        //Costruttore che assegna le varie stringhe all'attributo corrispondente
        //this.login = login;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    //testing
    @Override
    public String toString(){
        return "nome:" + nome + ", cognome:" + cognome + ", email:" + email + ", password:" + password;
    }

    public boolean login(String email, String password) {
        return ( email.equals(this.email) && password.equals(this.password));
    }


    /**
     * Recupera il nome dell'utente.
     * @return Il nome.
     */
    public String getNome() {
        return nome;
    }


    /**
     * Imposta il nome dell'utente.
     * @param nome Il nome da assegnare.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }


    /**
     * Recupera il cognome dell'utente.
     * @return Il cognome.
     */
    public String getCognome() {
        return cognome;
    }


    /**
     * Imposta il cognome dell'utente.
     * @param cognome Il cognome da assegnare.
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }


    /**
     * Recupera l'indirizzo email dell'utente.
     * @return L'email.
     */
    public String getEmail() {
        return email;
    }


    /**
     * Imposta l'indirizzo email dell'utente.
     * @param email L'email da assegnare.
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Recupera la password dell'utente.
     * @return La password.
     */
    public String getPassword() {
        return password;
    }


    /**
     * Imposta la password dell'utente.
     * @param password La password da assegnare.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
