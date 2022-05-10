package daos;

import datastructure.UDArray;
import db.DbFactory;
import entities.Ticket;
import util.DateTimeZone;

import java.sql.*;
import java.util.List;

public class TicketDaoImpl implements TicketDao {
    private Connection connection;

    public TicketDaoImpl() { connection = DbFactory.getConnection();}

    @Override
    public String insert(Ticket ticket) {
        String query = "INSERT INTO tickets (ticket_id, amount, description, created_at, category, user_id) VALUES(default, ?, ?, ?, ?, ?);";
        String resultString = null;
        try {
            PreparedStatement st = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            st.setDouble(1, ticket.getAmount());
            st.setString(2, ticket.getDescription());
            st.setTimestamp(3, ticket.getCreated_at());
            st.setString(4, ticket.getCategory());
            st.setInt(5, ticket.getUser_id());
            int count = st.executeUpdate();
            if(count == 1) {
                ResultSet res = st.getGeneratedKeys();
                res.next();
                int ticket_id = res.getInt(1);
                ticket.setTicket_id(ticket_id);
                System.out.println("Ticket successfully created: " + ticket_id);
                resultString = "Success";
            }
        }catch (SQLException ex) {
            System.out.println("Inserting error: " + ex.getLocalizedMessage());
        }
        return resultString;
    }

    @Override
    public boolean update(Ticket ticket) {
        String query = "UPDATE tickets SET amount = ?, description = ?, updated_at = ?, category = ? WHERE ticket_id = ?;";
        boolean result = false;
        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setDouble(1, ticket.getAmount());
            st.setString(2, ticket.getDescription());
            st.setTimestamp(3, DateTimeZone.getDateTimeZone());
            st.setString(4, ticket.getCategory());
            st.setInt(5, ticket.getTicket_id());
            int count = st.executeUpdate();
            if(count == 1) {
                result = true;
            }
        }catch (SQLException ex) {
            System.out.println("update method: " + ex.getLocalizedMessage());
        }
        return result;
    }

    @Override
    public boolean delete(int ticket_id) {
        String query = "DELETE FROM tickets WHERE ticket_id = ?;";
        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setInt(1, ticket_id);
            int count = st.executeUpdate();
            if(count == 1) {
                return true;
            }
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return false;
    }

    @Override
    public Ticket getById(int ticket_id) {
        String query = "SELECT * FROM tickets WHERE ticket_id = ?;";
        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setInt(1, ticket_id);
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()) {
                Ticket ticket = getTicketFromResultSet(resultSet);
                return ticket;
            }
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return null;
    }

//    public Ticket getByDate(Date date) {
//        String query = "SELECT * FROM tickets WHERE created_at::date = ?;";
//        try {
//            PreparedStatement st = connection.prepareStatement(query);
//            st.setDate(1, date);
//            ResultSet resultSet = st.executeQuery();
//            while (resultSet.next()) {
//                Ticket ticket = getTicketFromResultSet(resultSet);
//                return ticket;
//            }
//        }catch (SQLException ex) {
//            System.out.println(ex.getLocalizedMessage());
//        }
//        return null;
//    }

    @Override
    public UDArray getAll(List<String> options) {
        UDArray<Ticket> tickets = new UDArray<>();
        String query = "SELECT * FROM tickets";
        for(String option : options) {
            switch (option) {
                case "Pending":
                    query += " WHERE status LIKE '%Pending'";
                    break;
                case "Approved":
                    query += " WHERE status LIKE '%Approved'";
                    break;
                case "DESC":
                    query += " ORDER BY created_at DESC";
                    break;
                default:
                    query += " ORDER BY created_at";
            }
        }
        query += ";";
        try {
            PreparedStatement st = connection.prepareStatement(query);
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()) {
                Ticket ticket = getTicketFromResultSet(resultSet);
                tickets.add(ticket);
            }
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return tickets;
    }

    /**
     * Setup for H2 database tests
     */
    @Override
    public void initTables() {
        String query = "DROP TABLE IF EXISTS tickets CASCADE; CREATE TABLE tickets(ticket_id serial primary key, amount float8 not null check(amount >0), " +
                "description varchar(100) not null, " +
                "created_at timestamp default(Current_timestamp), " +
                "updated_at timestamp, " +
                "status varchar(50) default('Pending'), category varchar(100), user_id int references users(user_id) ON DELETE CASCADE);";

        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.executeUpdate();
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Override
    public void fillTables() {
        String query = "INSERT INTO tickets (ticket_id, amount, description, created_at, updated_at, status, category, user_id) " +
                "VALUES(1, 25.5, 'Uber from home to work', default, default, 'Pending', 'Transportation', 1);";
        query += "INSERT INTO tickets (ticket_id, amount, description, created_at, updated_at, status, category, user_id) " +
                "VALUES(2, 50.3, 'Hotel in CA for js conference', default, default, 'Pending', 'Accommodation', 2);";
        query += "INSERT INTO tickets (ticket_id, amount, description, created_at, updated_at, status, category, user_id) " +
                "VALUES(3, 125.09, 'Work party dinner', default, default, 'Pending', 'Business party', 3);";
        query += "INSERT INTO tickets (ticket_id, amount, description, created_at, updated_at, status, category, user_id) " +
                "VALUES(4, 245.12, 'Uber work to home', default, default, 'Pending', 'Transportation', 4);";
        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.executeUpdate();
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public void truncateTable() {
        String query = "TRUNCATE TABLE tickets;";
        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.executeUpdate();
        }catch(SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
    // End of test setup

    private Ticket getTicketFromResultSet(ResultSet resultSet) {
        try {
            int ticket_id = resultSet.getInt("ticket_id");
            System.out.println("Ticket_id: " + ticket_id);
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
