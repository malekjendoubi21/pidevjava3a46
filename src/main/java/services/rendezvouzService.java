package services;

import models.rendezvouz;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class rendezvouzService implements IService<rendezvouz> {
    private Connection connection;

    public rendezvouzService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void create(rendezvouz rendezvouz) throws SQLException {
        String sql = "INSERT INTO rendezvouz (daterdv, email, local_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, rendezvouz.getDaterdv());
            ps.setString(2, rendezvouz.getEmail());
            ps.setInt(3, rendezvouz.getLocal_id());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(rendezvouz rendezvouz) throws SQLException {
        String sql = "UPDATE rendezvouz SET daterdv = ?, email = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, rendezvouz.getDaterdv());
            ps.setString(2, rendezvouz.getEmail());
            ps.setInt(3, rendezvouz.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM rendezvouz WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<rendezvouz> read() throws SQLException {
        String sql = "SELECT * FROM rendezvouz ";
        List<rendezvouz> rendezvousList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                rendezvouz rvl = new rendezvouz();
                rvl.setId(rs.getInt("id"));
                rvl.setDaterdv(rs.getTimestamp("daterdv"));
                rvl.setEmail(rs.getString("email"));
                rvl.setLocal_id(rs.getInt("local_id"));
                rendezvousList.add(rvl);
            }
        }
        return rendezvousList;
    }

    public rendezvouz affichagerdv(int id) throws SQLException {
        String sql = "SELECT * FROM rendezvouz WHERE local_id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();

        rendezvouz rdv = new rendezvouz();
        List<rendezvouz> listn = new ArrayList<>();
        while (rs.next()) {
            rdv.setId(rs.getInt("id"));
            listn.add(rdv);
        }

        return rdv;
    }

    public List<rendezvouz> getRendezvousByMonth(int month, int year) throws SQLException {
        List<rendezvouz> rendezvousInMonth = new ArrayList<>();
        List<rendezvouz> rendezvousList = read();
        for (rendezvouz r : rendezvousList) {
            Timestamp rendezvousDate = r.getDaterdv();
            LocalDate rendezvousLocalDate = rendezvousDate.toLocalDateTime().toLocalDate();
            if (rendezvousLocalDate.getMonthValue() == month && rendezvousLocalDate.getYear() == year) {
                rendezvousInMonth.add(r);
            }
        }
        return rendezvousInMonth;
    }

    public List<rendezvouz> getRendezvousByDate(Timestamp date) throws SQLException {
        List<rendezvouz> rendezvousList = new ArrayList<>();
        String sql = "SELECT * FROM rendezvouz WHERE DATE(daterdv) = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // Convertir la Date en Timestamp avec l'heure à minuit
            Timestamp timestamp = new Timestamp(date.getTime());
            ps.setTimestamp(1, timestamp);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rendezvouz rdv = new rendezvouz();
                    rdv.setId(rs.getInt("id"));
                    rdv.setDaterdv(rs.getTimestamp("daterdv"));
                    rdv.setEmail(rs.getString("email"));
                    rdv.setLocal_id(rs.getInt("local_id"));
                    rendezvousList.add(rdv);
                }
            }
        }
        return rendezvousList;
    }

}
