package models;

import java.sql.Timestamp;

public class reponse {
    int id;
    String response;
    Timestamp dateresponse;
    int reclamation_id;

    public reponse() {
    }

    public reponse(String response, int reclamation_id) {
        this.response = response;
        this.reclamation_id = reclamation_id;
    }

    public reponse(String response, Timestamp dateresponse, int reclamation_id) {
        this.response = response;
        this.dateresponse = dateresponse;
        this.reclamation_id = reclamation_id;
    }

    public reponse(int id, String response, int reclamation_id) {
        this.id = id;
        this.response = response;
        this.reclamation_id = reclamation_id;
    }

    public reponse(int id, String response, Timestamp dateresponse, int reclamation_id) {
        this.id = id;
        this.response = response;
        this.dateresponse = dateresponse;
        this.reclamation_id = reclamation_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Timestamp getDateresponse() {
        return dateresponse;
    }

    public void setDateresponse(Timestamp dateresponse) {
        this.dateresponse = dateresponse;
    }

    public int getReclamation_id() {
        return reclamation_id;
    }

    public void setReclamation_id(int reclamation_id) {
        this.reclamation_id = reclamation_id;
    }
}
