package fr.iutcommunity;

/**
 * Created by Peill on 28/03/2016.
 */
public class Groupe {
    private int idGroupe;
    private String libelle;

    public Groupe(String libelle) {
        this.libelle = libelle;
    }

    public Groupe(int idGroupe, String libelle) {
        this.idGroupe = idGroupe;
        this.libelle = libelle;
    }

    public int getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(int idGroupe) {
        this.idGroupe = idGroupe;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
