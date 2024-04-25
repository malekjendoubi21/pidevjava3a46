package models;

import java.sql.Timestamp;

public class reclamation {
    private static final reclamation instance = new reclamation();
    private int id;
    private String sujet;
    private String contenu;
    private Timestamp date;
    private int user_id;

    public reclamation() {
    }

    public reclamation(int id, String sujet, String contenu, Timestamp date, int user_id) {
        this.id = id;
        this.sujet = sujet;
        this.contenu = contenu;
        this.date = date;
        this.user_id = user_id;
    }

    public reclamation(int id, String sujet, String contenu, int user_id) {
        this.id = id;
        this.sujet = sujet;
        this.contenu = contenu;
        this.user_id = user_id;
    }

    public reclamation(String sujet, String contenu, int user_id) {
        this.sujet = sujet;
        this.contenu = contenu;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    @Override
    public String toString() {
        return "reclamation{" +
                "id=" + id +
                ", sujet='" + sujet + '\'' +
                ", contenu='" + contenu + '\'' +
                ", user_id=" + user_id +
                '}';
    }

    public static reclamation getInstance(){
        return instance;
    }

}
