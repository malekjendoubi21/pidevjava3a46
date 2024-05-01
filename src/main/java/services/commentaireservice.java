package services;

import models.commentaire;
import models.publication;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class commentaireservice implements IService<commentaire>{
    private Connection connection;

    public commentaireservice(){
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void create(commentaire commentaire) throws SQLException {
        String sql = "INSERT INTO commentaire (contenu,publication_id,signalements) VALUES (?, ?,0)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, commentaire.getContenu());
        statement.setInt(2, commentaire.getPublication_id());
        statement.executeUpdate();
    }


    @Override
    public void update(commentaire commentaire) throws SQLException {
        String sql = "update commentaire set  contenu = ?  ,signalements = ? where id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, commentaire.getContenu());
        ps.setInt(2, commentaire.getSignalements());
        ps.setInt(3, commentaire.getId());
        ps.executeUpdate();
    }



    @Override
    public void delete(int id) throws SQLException {
        String sql = "delete  from commentaire where id= ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<commentaire> read() throws SQLException {
        String sql = "select * from commentaire";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<commentaire> people = new ArrayList<>();
        while (rs.next()){
            commentaire p = new commentaire();
            p.setId(rs.getInt("id"));
            p.setContenu(rs.getString("contenu"));
            p.setPublication_id(rs.getInt("publication_id"));
            people.add(p);
        }
        return people;
    }


    public String affichageCommentaire(int id) throws SQLException {
        String sql = "SELECT titre FROM publication WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();

        String a = null;
        while (rs.next()) {
            a = rs.getString("titre");
        }
        rs.close();
        statement.close();

        return a;
    }
    public List<commentaire> readByPublicationId(int publicationId) throws SQLException {
        List<commentaire> commentaires = new ArrayList<>();
        String query = "SELECT * FROM commentaire WHERE publication_id = ?";
        try (PreparedStatement preparedStatement = MyDatabase.getInstance().getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, publicationId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    commentaire commentaire = new commentaire();
                    commentaire.setId(resultSet.getInt("id"));
                    commentaire.setContenu(resultSet.getString("contenu"));
                    commentaire.setSignalements(resultSet.getInt("signalements"));
                    // Ajouter d'autres attributs si nécessaire

                    commentaires.add(commentaire);
                }
            }
        }
        return commentaires;
    }

}