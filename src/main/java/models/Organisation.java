package models;

public class Organisation {

    private int id;
    private String nom;
    private String email;
    private String adresse;
    private String num_tel;
    private String image;
    private static final Organisation instance = new Organisation();

    public static Organisation getInstance(){
        return instance;
    }


    public Organisation(int id, String nom, String email, String adresse, String num_tel, String image) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.adresse = adresse;
        this.num_tel = num_tel;
        this.image = image;
    }

    public Organisation( String nom, String email, String adresse, String num_tel, String image) {
        this.nom = nom;
        this.email = email;
        this.adresse = adresse;
        this.num_tel = num_tel;
        this.image = image;
    }

    public Organisation() {

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNum_tel() {
        return num_tel;
    }

    public void setNum_tel(String num_tel) {
        this.num_tel = num_tel;
    }

    @Override
    public String toString() {
        return "Organisation{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", adresse='" + adresse + '\'' +
                ", num_tel='" + num_tel + '\'' +
                '}';
    }

}

