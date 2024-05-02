package models;

import java.time.LocalDateTime;

public class docteur extends user {

    private String specialite;

    public docteur(String email, String roles, String password, String nom, String prenom, int numtel,
                   LocalDateTime birth, String profileImage, String gender, String specialite) {
        super(email, roles, password, nom, prenom, numtel, birth, profileImage, gender);
        this.specialite = specialite;
    }

    public docteur() {

    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
}
