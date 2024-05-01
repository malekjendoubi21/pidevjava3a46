package models;

import java.util.List;

public class publication {
    int id;
    String titre;
    String contenu;
   /* private List<commentaire> comments;


    public boolean hasComments() {
        return !(comments.isEmpty());
    }*/
    String imageurl;
    private static final publication instance = new publication();
    public publication() {
    }

    public publication(String titre, String contenu, String imageurl) {
        this.titre = titre;
        this.contenu = contenu;
        this.imageurl = imageurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getImage() {
        return imageurl;
    }
    public static publication getInstance(){

        return instance;
    }

    public void setImage1(String imageurl) {
        this.imageurl = imageurl;
    }
}
