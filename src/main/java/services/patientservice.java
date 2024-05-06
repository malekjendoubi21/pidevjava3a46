package services;

import models.patient;
import models.user;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class patientservice implements IService<patient>{
    private Connection connection;

    public patientservice(){
        connection = MyDatabase.getInstance().getConnection();
    }


    @Override
    public void create(patient patient) throws SQLException {
        String sql = "INSERT INTO user (email, roles, password, nom, prenom, numtel, birth, profileImage, gender) VALUES ('"
                + patient.getEmail() + "', '"
                + patient.getRoles() + "', '"
                + patient.getPassword() + "', '"
                + patient.getNom() + "', '"
                + patient.getPrenom() + "', "
                + patient.getNumtel() + ", '"
                + patient.getBirth() + "', '"
                + patient.getProfileImage() + "', '"
                + patient.getGender() + "')";

        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    @Override
    public void update(patient user) throws SQLException {
        String sql = "UPDATE user SET email = ?, roles = ?, password = ?, nom = ?, prenom = ?, numtel = ?, birth = ?, profileImage = ?, gender = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, user.getEmail());
        ps.setString(2, user.getRoles());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getNom());
        ps.setString(5, user.getPrenom());
        ps.setInt(6, user.getNumtel());
        ps.setObject(7, user.getBirth()); // Assuming birth is a LocalDateTime
        ps.setString(8, user.getProfileImage());
        ps.setString(9, user.getGender());
        ps.setInt(10, user.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public List<patient> read() throws SQLException {
        String sql = "SELECT * FROM user";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<patient> patients = new ArrayList<>();
        while (rs.next()) {
            patient pat = new patient();
            pat.setId(rs.getInt("id"));
            pat.setEmail(rs.getString("email"));
            pat.setRoles(rs.getString("roles"));
            pat.setPassword(rs.getString("password"));
            pat.setNom(rs.getString("nom"));
            pat.setPrenom(rs.getString("prenom"));
            pat.setNumtel(rs.getInt("numtel"));
            pat.setBirth(rs.getObject("birth", LocalDate.class));
            pat.setProfileImage(rs.getString("profileImage"));
            pat.setGender(rs.getString("gender"));

            patients.add(pat);
        }
        return patients;
    }

    @Override
    public user findByID(int id) {


        return null;
    }
}
