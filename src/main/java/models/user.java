package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class user {
    private int id;
    private String email;
    private String roles;
    private String password;
    private String nom;
    private String prenom;
    private int numtel;
    private LocalDateTime birth;
    private String profileImage;
    private String gender;
    private int code;

    public user(String email, String roles, String password, String nom, String prenom, int numtel,
                LocalDateTime birth, String profileImage, String gender) {
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.numtel = numtel;
        this.birth = birth;
        this.profileImage = profileImage;
        this.gender = gender;
    }

    public user() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getNumtel() {
        return numtel;
    }

    public void setNumtel(int numtel) {
        this.numtel = numtel;
    }

    public LocalDate getBirth() {
        if (birth != null) {
            return birth.toLocalDate();
        }
        return null;
    }

    public void setBirth(LocalDate birth) {
        if (birth != null) {
            this.birth = birth.atStartOfDay();
        } else {
            this.birth = null;
        }
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof user user)) return false;
        return getId() == user.getId() && getNumtel() == user.getNumtel() && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getRoles(), user.getRoles()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getNom(), user.getNom()) && Objects.equals(getPrenom(), user.getPrenom()) && Objects.equals(getBirth(), user.getBirth()) && Objects.equals(getProfileImage(), user.getProfileImage()) && Objects.equals(getGender(), user.getGender());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getRoles(), getPassword(), getNom(), getPrenom(), getNumtel(), getBirth(), getProfileImage(), getGender());
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}