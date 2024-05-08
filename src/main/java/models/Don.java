package models;

public class Don {
    private static final Don instance = new Don();

    public static Don getInstance(){
        return instance;
    }

    private int id;
    private String type;
    private String description;
    private String email;
    private String nom;
    private String prenom;
    private String image;
    private int montant;
    private String nomm;
    private int organisation_id;


    public Don(int id, String nom, String prenom, String email, String type, String description, String image, int montant,int organisation_id) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.image = image;
        this.montant = montant;
        this.organisation_id = organisation_id;
    }

    public Don(String nom, String prenom, String email, String type, String description, String image, int montant,int organisation_id) {
        this.type = type;
        this.description = description;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.image = image;
        this.montant = montant;
        this.organisation_id = organisation_id;
    }

    public Don() {

    }

    public String getNomm() {
        return nomm;
    }

    public void setNomm(String nomm) {
        this.nomm = nomm;
    }

    public int getOrganisation_id() {
        return organisation_id;
    }

    public void setOrganisation_id(int organisation_id) {
        this.organisation_id = organisation_id;
    }


    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getImage() {
        return image;
    }

    public int getMontant() {
        return montant;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public String toString() {
        return "Don{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", image='" + image + '\'' +
                ", montant=" + montant +
                '}';
    }

}
