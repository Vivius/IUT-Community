package fr.iutcommunity;

/**
 * Created by peillexv on 22/03/2016.
 */
public class Utilisateur {
    private String login;
    private String prenom;
    private String nom;
    private String password;
    private String mail;

    public Utilisateur(String login, String prenom, String nom) {
        this.login = login;
        this.prenom = prenom;
        this.nom = nom;
    }

    public Utilisateur(String login, String prenom, String nom, String password, String mail) {
        this.login = login;
        this.prenom = prenom;
        this.nom = nom;
        this.password = password;
        this.mail = mail;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
