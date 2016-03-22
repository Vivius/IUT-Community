package fr.iutcommunity;

/**
 * Created by peillexv on 22/03/2016.
 */
public class Message {
    private String titre;
    private String message;
    private Utilisateur utilisateur;

    public Message(String titre, String message, Utilisateur utilisateur) {
        this.titre = titre;
        this.message = message;
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
}
