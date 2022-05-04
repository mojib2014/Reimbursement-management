package daos;

import db.DbFactory;
import entities.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public T get(int id) {
        return null;
    }

    @Override
    public T getAll() {
        return null;
    }
}
