package services;
import models.reclamation;
import models.reponse;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class ReponseService implements IService<reponse>{
    private Connection connection;

    public ReponseService(){
        connection = MyDatabase.getInstance().getConnection();
    }
    @Override
    public void create(reponse reponse) throws SQLException {
        String sql = "insert into reponse (response,reclamation_id)"+
                "values('"+reponse.getResponse()+"','"+reponse.getReclamation_id()+"'" +
                ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    @Override
    public void update(reponse reponse) throws SQLException {
        String sql = "update reponse set response = ? where id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, reponse.getResponse());
        ps.setInt(2, reponse.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "delete  from reponse where id= ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();

    }

    @Override
    public List<reponse> read() throws SQLException {
        return null;
    }

    public reponse affichagerep(int id) throws SQLException {
        String sql = "select * from reponse where reclamation_id= ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        reponse rep=new reponse();
        rep.setResponse("pas de reponse :/");
        rep.setDateresponse(Timestamp.valueOf(LocalDateTime.now()));
        while (rs.next()){

            rep.setId(rs.getInt("id"));
            rep.setResponse(rs.getString("response"));
            rep.setDateresponse(rs.getTimestamp("date"));
        }
        return rep;
    }
}
