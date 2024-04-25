package services;

import models.publication;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class publicationservice implements IService<publication>{
    private Connection connection;

    public publicationservice(){
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void create(publication publication) throws SQLException {
        String sql = "INSERT INTO publication (titre, contenu, image) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, publication.getTitre());
        statement.setString(2, publication.getContenu());
        statement.setString(3, publication.getImage());
        statement.executeUpdate();
    }


    @Override
    public void update(publication publication) throws SQLException {
        String sql = "update publication set titre = ?, contenu = ? where id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, publication.getTitre());
        ps.setString(2, publication.getContenu());
     //   ps.setString(3,publication.getImage());
        ps.setInt(3,publication.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "delete  from publication where id= ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<publication> read() throws SQLException {
        String sql = "select * from publication";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<publication> people = new ArrayList<>();
        while (rs.next()){
            publication p = new publication();
            p.setId(rs.getInt("id"));
            p.setTitre(rs.getString("titre"));
            p.setContenu(rs.getString("contenu"));
            p.setImage1(rs.getString("image"));

            people.add(p);
        }
        return people;
    }

    public publication searchh(String a) throws SQLException {
        String sql = "select * from `publication` where `titre`=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, a);
        ResultSet rs = statement.executeQuery();
        List<publication> people = new ArrayList<>();
        publication p = new publication();
        while (rs.next()){

            p.setId(rs.getInt("id"));
            p.setTitre(rs.getString("titre"));
            p.setContenu(rs.getString("contenu"));
            p.setImage1(rs.getString("image"));


        }
        return p;
    }

}

