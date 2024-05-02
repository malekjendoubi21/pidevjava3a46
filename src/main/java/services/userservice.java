package services;

import models.user;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class userservice implements IService<user>{
    private Connection connection;
    List<user> people = new ArrayList<>();

    public userservice(){
        connection = MyDatabase.getInstance().getConnection();
    }

    public static user loggedIn ;
    @Override
    public void create(user user) throws SQLException {
        String sql = "INSERT INTO user (email, roles, password, nom, prenom, numtel, birth, profileImage, gender) VALUES ('"
                + user.getEmail() + "', '"
                + user.getRoles() + "', '"
                + user.getPassword() + "', '"
                + user.getNom() + "', '"
                + user.getPrenom() + "', "
                + user.getNumtel() + ", '"
                + user.getBirth() + "', '"
                + user.getProfileImage() + "', '"
                + user.getGender() + "')";

        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    @Override
    public void update(user user) throws SQLException {
        String sql = "UPDATE user SET email = ?, roles = ?, password = ?, nom = ?, prenom = ?, numtel = ?, birth = ?, profileImage = ?, gender = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, user.getEmail());
        ps.setString(2, user.getRoles());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getNom());
        ps.setString(5, user.getPrenom());
        ps.setInt(6, user.getNumtel());
        ps.setObject(7, user.getBirth());
        ps.setString(8, user.getProfileImage());
        ps.setString(9, user.getGender());
        ps.setInt(10, user.getId());
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
    public List<user> read() throws SQLException {

        String sql = "SELECT * FROM user";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
    //    List<user> people = new ArrayList<>();
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
            user.setBirth(rs.getObject("birth", LocalDate.class));
            user.setProfileImage(rs.getString("profileImage"));
            user.setGender(rs.getString("gender"));
           // user.docteur.setSpecialite(rs.getString("specialite"));

            people.add(user);
        }
        return people;
    }
    public List<user> rech(String nom) throws SQLException {
        String sql = "select * from `user` where `nom`= ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, nom);
        ResultSet rs = statement.executeQuery();
        List<user> users = new ArrayList<>();
        while (rs.next()){
            user user = new user();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setRoles(rs.getString("roles"));
            user.setPassword(rs.getString("password"));
            user.setNom(rs.getString("nom"));
            user.setPrenom(rs.getString("prenom"));
            user.setNumtel(rs.getInt("numtel"));
            user.setBirth(rs.getObject("birth", LocalDate.class));
            user.setProfileImage(rs.getString("profileImage"));
            user.setGender(rs.getString("gender"));
            // user.setSpecialite(rs.getString("specialite"));
            users.add(user);
        }
        return users;
    }


    public user userByMail(String mail){
        user user1;
        System.out.println("Loading users from database...+ " +mail);

        if (people.isEmpty()){
            try {
                System.out.println("people.isEmpty()");
                read();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        for (user user: people) {
            if (user.getEmail().equals(mail)) {
                System.out.println("found user by mail");
                user1=user;
                return user1; // Found the user, return immediately
            }
        }

        // User with the given email not found
        System.out.println("no found user by mail");

        return null;

    }







    /**
     *
     * @param email (String)
     * @param newPassword (String) new Password after resetting
     * @throws SQLException pb w sql
     */
    public void updatePassword(String email, String newPassword) throws SQLException {
        String sql = "UPDATE user SET password=? WHERE email=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, email);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                // No user found with the provided email
                throw new SQLException("User with email " + email + " not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
            throw e;
        }
    }




}
