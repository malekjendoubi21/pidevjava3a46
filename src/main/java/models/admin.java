package models;

import java.time.LocalDateTime;

public class admin extends user{
    public admin(String email, String roles, String password, String nom, String prenom, int numtel, LocalDateTime birth, String profileImage, String gender) {
        super(email, roles, password, nom, prenom, numtel, birth, profileImage, gender);
    }

    public admin() {
    }
}
