package services;

import models.Organisation;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrganisationService implements IService<Organisation> {

    private Connection connection;

    public OrganisationService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    private PreparedStatement pst;


    @Override
    public void create(Organisation o) throws SQLException {
        String sql = "INSERT INTO organisation (nom, email, adresse, num_tel,image) VALUES (?,?, ?, ?, ?)";

        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, o.getNom());
            pst.setString(2, o.getEmail());
            pst.setString(3, o.getAdresse());
            pst.setString(4, o.getNum_tel());
            pst.setString(5, o.getImage());

            pst.executeUpdate();
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }


    @Override
    public void update(Organisation o) throws SQLException {
        String sql = "update organisation set nom = ?, adresse = ?, email = ? , num_tel = ?,image =? where id = ? ";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, o.getNom());
        ps.setString(2, o.getAdresse());
        ps.setString(3, o.getEmail());
        ps.setString(4, o.getNum_tel());
        ps.setInt(6, o.getId());
        ps.setString(5, o.getImage());
        ps.executeUpdate();
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM organisation WHERE id = ?";

        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Organisation avec l'ID " + id + " supprimée avec succès.");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'organisation avec l'ID " + id, e);
        }
    }

    @Override
    public List<Organisation> read() throws SQLException {
        List<Organisation> list = new ArrayList<>();
        String sql = "SELECT * FROM organisation";

        try (ResultSet rs = connection.createStatement().executeQuery(sql)) {
            while (rs.next()) {
                Organisation o = new Organisation(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("adresse"),
                        rs.getString("num_tel"),
                        rs.getString("image")

                );
                list.add(o);
            }
        }

        return list;
    }


    public int getnomm(String s) throws SQLException {
        String sql = "SELECT * FROM Organisation WHERE nom=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, s);
        ResultSet rs = statement.executeQuery();
        Organisation o = new Organisation();

        while (rs.next()) {
            o.setId(rs.getInt("id"));
            o.setNom(rs.getString("nom"));
            o.setAdresse(rs.getString("adresse"));
            o.setEmail(rs.getString("email"));
            o.setNum_tel(rs.getString("num_tel"));
        }

        return o.getId();
    }

    public Organisation affichageOrg(int id) throws SQLException {
        String sql = "SELECT * FROM Organisation WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();

        Organisation o = new Organisation();

        while (rs.next()) {
            o.setId(rs.getInt("id"));
            o.setNom(rs.getString("nom"));
            o.setAdresse(rs.getString("adresse"));
            o.setEmail(rs.getString("email"));
            o.setNum_tel(rs.getString("num_tel"));
        }

        return o;
    }

}


