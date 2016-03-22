package fr.iutcommunity;

import java.util.Date;

/**
 * Created by peillexv on 22/03/2016.
 */
public class Message {
    private String titre;
    private String message;
    private Date date;
    private Utilisateur utilisateur;

    public Message(String titre, String message, Date date, Utilisateur utilisateur) {
        this.titre = titre;
        this.message = message;
        this.date = date;
        this.utilisateur = utilisateur;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
