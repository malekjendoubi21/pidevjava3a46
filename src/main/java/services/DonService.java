package services;

import models.Don;
import models.Organisation;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonService implements IService<Don> {

    private Connection connection;

    public DonService(){
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void create(Don don) throws SQLException {
        String sql = "INSERT INTO don (nom, prenom, email, description, type, image, montant,organisation_id) VALUES (? ,?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, don.getNom());
        statement.setString(2, don.getPrenom());
        statement.setString(3, don.getEmail());
        statement.setString(4, don.getDescription());
        statement.setString(5, don.getType());
        statement.setString(6, don.getImage());
        statement.setInt(7, don.getMontant());
        statement.setInt(8, don.getOrganisation_id());
        statement.executeUpdate();
    }

    @Override
    public void update(Don don) throws SQLException {
        String sql = "UPDATE don SET nom=?, prenom=?, email=?, description=?, type=?, image=?, montant=? WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, don.getNom());
        ps.setString(2, don.getPrenom());
        ps.setString(3, don.getEmail());
        ps.setString(4, don.getDescription());
        ps.setString(5, don.getType());
        ps.setString(6, don.getImage());
        ps.setInt(7, don.getMontant());

        ps.setInt(8, don.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM don WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<Don> read() throws SQLException {
        String sql = "SELECT * FROM don";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Don> dons = new ArrayList<>();
        while (rs.next()){
            Don don = new Don();
            don.setId(rs.getInt("id"));
            don.setNom(rs.getString("nom"));
            don.setPrenom(rs.getString("prenom"));
            don.setEmail(rs.getString("email"));
            don.setType(rs.getString("type"));
            don.setDescription(rs.getString("description"));
            don.setImage(rs.getString("image"));
            don.setMontant(rs.getInt("montant"));
            don.setOrganisation_id(rs.getInt("organisation_id"));
            dons.add(don);
        }
        return dons;
    }


    public String affichageDon(int id) throws SQLException {
        String sql = "SELECT nom FROM organisation WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();

        String a = null;
        while (rs.next()) {
            a = rs.getString("nom");
        }
        rs.close();
        statement.close();

        return a;
    }


    public List<Don> reada() throws SQLException {
        String sql = "SELECT * FROM don";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Don> dons = new ArrayList<>();
        while (rs.next()){
            Don don = new Don();
            don.setId(rs.getInt("id"));
            don.setNom(rs.getString("nom"));
            don.setPrenom(rs.getString("prenom"));
            don.setEmail(rs.getString("email"));
            don.setType(rs.getString("type"));
            don.setDescription(rs.getString("description"));
            don.setImage(rs.getString("image"));
            don.setMontant(rs.getInt("montant"));
            don.setOrganisation_id(rs.getInt("organisation_id"));
            don.setNomm(affichageDon(don.getOrganisation_id()));
            dons.add(don);
        }
        return dons;
    }


    public List<Don> rech(String dd) throws SQLException {
        String sql = "SELECT * FROM Don WHERE nom LIKE CONCAT('%', ?, '%') OR prenom LIKE CONCAT('%', ?, '%') OR email LIKE CONCAT('%', ?, '%') OR  montant LIKE CONCAT('%', ?, '%') OR description LIKE CONCAT('%', ?, '%')";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, dd);
        statement.setString(2, dd);
        statement.setString(3, dd);
        statement.setString(4, dd);
        statement.setString(5, dd);

        ResultSet rs = statement.executeQuery();
        List<Don> don = new ArrayList<>();
        while (rs.next()) {
            Don d = new Don();
            d.setId(rs.getInt("id"));
            d.setNom(rs.getString("nom"));
            d.setPrenom(rs.getString("prenom"));
            d.setEmail(rs.getString("email"));
            d.setDescription(rs.getString("description"));
            d.setMontant(rs.getInt("montant"));
            don.add(d);
        }
        return don;
    }


}
