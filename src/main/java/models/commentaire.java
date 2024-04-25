package models;

public class commentaire {

    int id;
    String contenu;

    public int getSignalements() {
        return signalements;
    }

    public void setSignalements(int signalements) {
        this.signalements = signalements;
    }

    int signalements;
    private String titree;

    public String getTitree() {
        return titree;
    }

    public void setTitree(String titree) {
        this.titree = titree;
    }

    int publication_id;

    public int getPublication_id() {
        return publication_id;
    }

    public void setPublication_id(int publication_id) {
        this.publication_id = publication_id;
    }

    public commentaire() {
    }

    public commentaire(String contenu) {

        this.contenu = contenu;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
}