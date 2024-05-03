package services;

import models.notif;
import models.reclamation;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class notifservice implements IService<notif>{
    private Connection connection;
    public notifservice(){
        connection = MyDatabase.getInstance().getConnection();
    }
    @Override
    public void create(notif notif) throws SQLException {
        String sql = "INSERT INTO notif (recid) VALUES (?)";
       PreparedStatement statement = connection.prepareStatement(sql) ;
            statement.setInt(1, notif.getRecid());
            statement.executeUpdate();

        }

    @Override
    public void update(notif notif) throws SQLException {

    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "delete  from notif where id= ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<notif> read() throws SQLException {
        String sql = "select * from notif";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<notif> people = new ArrayList<>();
        while (rs.next()){
            notif p = new notif(rs.getInt("id"),rs.getInt("recid"));
            people.add(p);
        }
        return people;
    }
}
