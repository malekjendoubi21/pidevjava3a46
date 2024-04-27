package services;

import models.reclamation;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ReclamationService  implements IService<reclamation>{
    private Connection connection;

    public ReclamationService(){
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void create(reclamation reclamation) throws SQLException {
        String sql = "insert into reclamation (sujet,contenu,user_id)"+
                "values('"+reclamation.getSujet()+"','"+reclamation.getContenu()+"'" +
                ","+reclamation.getUser_id()+")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    @Override
    public void update(reclamation reclamation) throws SQLException {
        String sql = "update reclamation set sujet = ?, contenu = ?, user_id = ? where id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, reclamation.getSujet());
        ps.setString(2, reclamation.getContenu());
        ps.setInt(3,reclamation.getUser_id());
        ps.setInt(4,reclamation.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "delete  from reclamation where id= ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<reclamation> read() throws SQLException {
        String sql = "select * from reclamation";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<reclamation> people = new ArrayList<>();
        while (rs.next()){
            reclamation p = new reclamation();
            p.setId(rs.getInt("id"));
            p.setSujet(rs.getString("sujet"));
            p.setContenu(rs.getString("contenu"));
            p.setUser_id(rs.getInt("user_id"));
            p.setDate(rs.getTimestamp("date"));
            people.add(p);
        }
        return people;
    }

    public List<reclamation> rech(String ff) throws SQLException {
        String sql = "select * from `reclamation` where `sujet`= ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, ff);
        ResultSet rs = statement.executeQuery();
        List<reclamation> people = new ArrayList<>();
        while (rs.next()){
            reclamation p = new reclamation();
            p.setId(rs.getInt("id"));
            p.setSujet(rs.getString("sujet"));
            p.setContenu(rs.getString("contenu"));
            p.setUser_id(rs.getInt("user_id"));

            people.add(p);
        }
        return people;
    }

    public List<reclamation> ffxd(String ff) throws SQLException {
        String sql = "SELECT * FROM reclamation WHERE sujet LIKE CONCAT('%', ?, '%') OR contenu LIKE CONCAT('%', ?, '%')";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, ff);
        statement.setString(2, ff);
        ResultSet rs = statement.executeQuery();
        List<reclamation> people = new ArrayList<>();
        while (rs.next()) {
            reclamation p = new reclamation();
            p.setId(rs.getInt("id"));
            p.setSujet(rs.getString("sujet"));
            p.setContenu(rs.getString("contenu"));
            p.setUser_id(rs.getInt("user_id"));
            p.setDate(rs.getTimestamp("date"));
            people.add(p);
        }
        return people;
    }
}
