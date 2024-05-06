package services;

import models.docteur;
import models.user;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class docteurservice implements IService<docteur> {
    private Connection connection;

    public docteurservice(){
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override

    public void create(docteur docteur) throws SQLException {
        String sql = "INSERT INTO user (email, roles, password, nom, prenom, numtel, birth, profileImage, gender, specialite) VALUES ('"
                + docteur.getEmail() + "', '"
                + docteur.getRoles() + "', '"
                + docteur.getPassword() + "', '"
                + docteur.getNom() + "', '"
                + docteur.getPrenom() + "', "
                + docteur.getNumtel() + ", '"
                + docteur.getBirth() + "', '"
                + docteur.getProfileImage() + "', '"
                + docteur.getGender() + "', '"
                + docteur.getSpecialite() + "')";

        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    @Override

    public void update(docteur docteur) throws SQLException {
        String sql = "UPDATE user SET email = ?, roles = ?, password = ?, nom = ?, prenom = ?, numtel = ?, birth = ?, profileImage = ?, gender = ?, specialite = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, docteur.getEmail());
        ps.setString(2, docteur.getRoles());
        ps.setString(3, docteur.getPassword());
        ps.setString(4, docteur.getNom());
        ps.setString(5, docteur.getPrenom());
        ps.setInt(6, docteur.getNumtel());
        ps.setObject(7, docteur.getBirth()); // Assuming birth is a LocalDateTime
        ps.setString(8, docteur.getProfileImage());
        ps.setString(9, docteur.getGender());
        ps.setString(10, docteur.getSpecialite());
        ps.setInt(11, docteur.getId());
        ps.executeUpdate();
    }

    @Override

    public void delete(int id) throws SQLException {
        String sql = "delete  from user where id= ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override

    public List<docteur> read() throws SQLException {
        String sql = "SELECT * FROM user";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<docteur> docteurs = new ArrayList<>();
        while (rs.next()) {
            docteur docteur = new docteur();
            docteur.setId(rs.getInt("id"));
            docteur.setEmail(rs.getString("email"));
            docteur.setRoles(rs.getString("roles"));
            docteur.setPassword(rs.getString("password"));
            docteur.setNom(rs.getString("nom"));
            docteur.setPrenom(rs.getString("prenom"));
            docteur.setNumtel(rs.getInt("numtel"));
            // Assuming birth is stored as a LocalDateTime in the database
            docteur.setBirth(rs.getObject("birth", LocalDate.class));
            docteur.setProfileImage(rs.getString("profileImage"));
            docteur.setGender(rs.getString("gender"));
            docteur.setSpecialite(rs.getString("specialite"));

            docteurs.add(docteur);
        }
        return docteurs;
    }
    @Override
    public user findByID(int id) {


        return null;
    }
}
