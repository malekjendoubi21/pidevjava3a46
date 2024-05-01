package models;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

public class rendezvouz {
    private int id;
    private Timestamp daterdv;
    private String email;
    private int local_id;

    private static final rendezvouz instance = new rendezvouz();

    public rendezvouz(int id, Timestamp daterdv, String email) {
        this.id = id;
        this.daterdv = daterdv;
        this.email = email;
    }

    public rendezvouz(Timestamp daterdv, String email) {
        this.daterdv = daterdv;
        this.email = email;
    }

    public rendezvouz() {
    }

    public rendezvouz(ZonedDateTime time, String hans) {
    }


    public static rendezvouz getInstance(){

        return instance;
    }

    public static rendezvouz get(int k) {
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDaterdv() {
        return daterdv;
    }

    public void setDaterdv(Timestamp daterdv) {
        this.daterdv = daterdv;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLocal_id() {
        return local_id;
    }

    public void setLocal_id(int local_id) {
        this.local_id = local_id;
    }

    @Override
    public String toString() {
        return "rendezvouz{" +
                "id=" + id +
                ", daterdv=" + daterdv +
                ", email='" + email + '\'' +
                '}';
    }
}
