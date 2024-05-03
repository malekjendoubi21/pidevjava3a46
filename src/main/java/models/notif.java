package models;

public class notif {
    int id;
    int recid;

    public notif(int id, int recid) {
        this.id = id;
        this.recid = recid;
    }

    public notif(int recid) {
        this.recid = recid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecid() {
        return recid;
    }

    public void setRecid(int recid) {
        this.recid = recid;
    }
}
