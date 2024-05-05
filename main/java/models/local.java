package models;

import java.util.Date;

public class local {
    private static final local instance = new local();
    private int id;
    private String adresse;
    private String nom;

    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public local(int id, String adresse, String nom, String image) {
        this.id = id;
        this.adresse = adresse;
        this.nom = nom;
        this.image = image;
    }

    public local(String adresse, String nom) {
        this.adresse = adresse;
        this.nom = nom;
    }

    public local() {

    }

    public static local getInstance(){
        return instance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "local{" +
                "id=" + id +
                ", adresse='" + adresse + '\'' +
                ", nom='" + nom + '\'' +
                '}';
    }

}
