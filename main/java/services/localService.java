package services;

import models.local;
import models.rendezvouz;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class localService implements IService<local> {
    private Connection connection;

    public localService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void create(local loc) throws SQLException {
        String sql = "INSERT INTO local (adresse, nom , image) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, loc.getAdresse());
            ps.setString(2, loc.getNom());
            ps.setString(3, loc.getImage());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(local loc) throws SQLException {
        String sql = "UPDATE local SET adresse = ?, nom = ? , image= ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, loc.getAdresse());
            ps.setString(2, loc.getNom());
            ps.setString(3, loc.getImage());
            ps.setInt(4, loc.getId());

            ps.executeUpdate();
        }
    }

    /*@Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM local WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }*/

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM local WHERE id = ?";

        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Local with ID " + id + " deleted successfully.");
            } else {
                System.out.println("No local found with ID " + id + ".");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting local with ID " + id, e);
        }
    }


    @Override
    public List<local> read() throws SQLException {
        String sql = "SELECT * FROM local";
        List<local> locList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                local loc = new local();
                loc.setId(rs.getInt("id"));
                loc.setAdresse(rs.getString("adresse"));
                loc.setImage(rs.getString("image"));
                loc.setNom(rs.getString("nom"));
                locList.add(loc);
            }
        }
        return locList;
    }
    public local affichagerdv(int id) throws SQLException {
        String sql = "SELECT * FROM local WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();

        local rdv = new local ();

        while (rs.next()) {
            rdv.setId(rs.getInt("id"));
            rdv.setNom(rs.getString("nom"));
            rdv.setAdresse(rs.getString("adresse"));
            rdv.setImage(rs.getString("image"));
        }

        return rdv;
    }
    public int getnomm(String s) throws SQLException {
        String sql = "SELECT * FROM local WHERE nom=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, s);
        ResultSet rs = statement.executeQuery();
        local rdv = new local ();

        while (rs.next()) {
            rdv.setId(rs.getInt("id"));
            rdv.setNom(rs.getString("nom"));
            rdv.setAdresse(rs.getString("adresse"));
        }

        return rdv.getId();
    }

    public local affichageOrg(int id) throws SQLException {
        String sql = "SELECT * FROM local WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();

        local o = new local();

        while (rs.next()) {
            o.setId(rs.getInt("id"));
            o.setNom(rs.getString("nom"));
            o.setAdresse(rs.getString("adresse"));
            o.setImage(rs.getString("image"));

        }

        return o;
    }
}
