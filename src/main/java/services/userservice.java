package services;

import models.user;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class userservice implements IService<user>{
    private Connection connection;

    public userservice(){
        connection = MyDatabase.getInstance().getConnection();
    }


    @Override
    public void create(user user) throws SQLException {
        String sql = "INSERT INTO user (email, roles, password, nom, prenom, numtel, birth, profileImage, gender, userType, specialite) VALUES ('"
                + user.getEmail() + "', '"
                + user.getRoles() + "', '"
                + user.getPassword() + "', '"
                + user.getNom() + "', '"
                + user.getPrenom() + "', "
                + user.getNumtel() + ", '"
                + user.getBirth() + "', '"
                + user.getProfileImage() + "', '"
                + user.getGender() + "', '"
                + user.getUserType() + "', '"
                + user.getSpecialite() + "')";

        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    @Override
    public void update(user user) throws SQLException {
        String sql = "UPDATE user SET email = ?, roles = ?, password = ?, nom = ?, prenom = ?, numtel = ?, birth = ?, profileImage = ?, gender = ?, userType = ?, specialite = ? WHERE id = ?";
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
        ps.setString(10, user.getUserType());
        ps.setString(11, user.getSpecialite());
        ps.setInt(12, user.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public List<user> read() throws SQLException {
        String sql = "SELECT * FROM user";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<user> users = new ArrayList<>();
        while (rs.next()) {
            user user = new user();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setRoles(rs.getString("roles"));
            user.setPassword(rs.getString("password"));
            user.setNom(rs.getString("nom"));
            user.setPrenom(rs.getString("prenom"));
            user.setNumtel(rs.getInt("numtel"));
            // Assuming birth is stored as a LocalDateTime in the database
            user.setBirth(rs.getObject("birth", LocalDateTime.class));
            user.setProfileImage(rs.getString("profileImage"));
            user.setGender(rs.getString("gender"));
            user.setUserType(rs.getString("userType"));
            user.setSpecialite(rs.getString("specialite"));

            users.add(user);
        }
        return users;
    }
}
