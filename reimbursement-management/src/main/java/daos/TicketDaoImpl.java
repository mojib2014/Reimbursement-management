package daos;

import db.DbFactory;
import entities.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDaoImpl<T> implements Dao<T>{
    private Connection connection;

    public TicketDaoImpl() { connection = DbFactory.getConnection();}

    @Override
    public boolean insert(T data) {
        Ticket ticket = (Ticket) data;
        String query = "INSERT INTO tickets(amount, description, created_at, status, category) VALUES(?, ?, ?, ?, ?);";
        try {
            PreparedStatement st = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            st.setDouble(1, ticket.getAmount());
            st.setString(2, ticket.getDescription());
            st.setTimestamp(3, ticket.getCreated_at());
            st.setString(4, ticket.getStatus());
            st.setString(5, ticket.getCategory());
            int count = st.executeUpdate();
            if(count == 1) {
                ResultSet res = st.getGeneratedKeys();
                res.next();
                int ticket_id = res.getInt("ticket_id");
                System.out.println("Ticket successfully created: " + ticket_id);
                return true;
            }
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return false;
    }

    @Override
    public boolean update(T data) {
        Ticket ticket = (Ticket) data;
        String query = "UPDATE tickets SET amount = ?, description = ?, updated_at = ? status = ? category = ? WHERE ticket_id = ?;";
        try {
            PreparedStatement st = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            st.setDouble(1, ticket.getAmount());
            st.setString(2, ticket.getDescription());
            st.setTimestamp(3, ticket.getUpdated_at());
            st.setString(4, ticket.getStatus());
            st.setString(5, ticket.getCategory());
            st.setInt(6, ticket.getTicket_id());
            int count = st.executeUpdate();
            if(count == 1) {
                ResultSet res = st.getGeneratedKeys();
                res.next();
                int ticket_id = res.getInt("ticket_id");
                System.out.println("Ticket successfully created: " + ticket_id);
                return true;
            }
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int ticket_id) {
        String query = "DELETE FROM tickets WHERE ticket_id = ?;";
        try {
            PreparedStatement st = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            st.setInt(6, ticket_id);
            int count = st.executeUpdate();
            if(count == 1) {
                ResultSet res = st.getGeneratedKeys();
                res.next();
                int id = res.getInt("ticket_id");
                System.out.println("Ticket successfully created: " + id);
                return true;
            }
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return false;
    }

    @Override
    public T get(int ticket_id) {
        String query = "SELECT * FROM tickets WHERE ticket_id = ?;";
        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setInt(1, ticket_id);
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()) {
                Ticket ticket = getTicketFromResultSet(resultSet);
                return (T) ticket;
            }
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public List<T> getAll() {
        List<T> tickets = new ArrayList<>();
        String query = "SELECT * FROM tickets;";
        try {
            PreparedStatement st = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()) {
                resultSet.next();
                Ticket ticket = getTicketFromResultSet(resultSet);
                tickets.add((T) ticket);
            }
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return tickets;
    }

    private Ticket getTicketFromResultSet(ResultSet resultSet) {
        try {
            int ticket_id = resultSet.getInt("ticket_id");
            double amount = resultSet.getDouble("amount");
            String description = resultSet.getString("description");
            Timestamp created_at = resultSet.getTimestamp("created_at");
            Timestamp updated_at = resultSet.getTimestamp("updated_at");
            String status = resultSet.getString("status");
            String category = resultSet.getString("category");
            return new Ticket(ticket_id, amount, description, created_at, updated_at, status, category);
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return null;
    }
}
