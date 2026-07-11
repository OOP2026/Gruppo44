package model;


public class Utente {
    protected String nome;
    protected String cognome;
    public String email;
    //protected String login;
    protected String password;

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
