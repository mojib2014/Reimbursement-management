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
    public String insert(T data) {
        Ticket ticket = (Ticket) data;
        String query = "INSERT INTO tickets(amount, description, created_at, category, user_id) VALUES(?, ?, ?, ?, ?);";
        try {
            PreparedStatement st = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            st.setDouble(1, ticket.getAmount());
            st.setString(2, ticket.getDescription());
            st.setTimestamp(3, ticket.getCreated_at());
            st.setString(5, ticket.getCategory());
            st.setInt(6, ticket.getUser_id());
            int count = st.executeUpdate();
            if(count == 1) {
                ResultSet res = st.getGeneratedKeys();
                res.next();
                int ticket_id = res.getInt("ticket_id");
                System.out.println("Ticket successfully created: " + ticket_id);
                return "Success";
            }
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return "";
    }

    @Override
    public boolean update(T data) {
        Ticket ticket = (Ticket) data;
        String query = "UPDATE tickets SET amount = ?, description = ?, updated_at = ?, status = ?, category = ? WHERE ticket_id = ?;";
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
        String query = "DELETE FROM tickets WHERE ticket_id = ? CASCADE;";
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
    public T getById(int ticket_id) {
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

    public T getByDate(Date date) {
        String query = "SELECT * FROM tickets WHERE created_at::date = ?;";
        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setDate(1, date);
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
            int user_id = resultSet.getInt("user_id");
            return new Ticket(ticket_id, amount, description, created_at, updated_at, status, category, user_id);
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return null;
    }
}
