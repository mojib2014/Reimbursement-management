package com.revature.dao;

import com.revature.entity.Employee;
import com.revature.entity.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDao {

    Connection conn = ConnectionFactory.getConnection();
    EmployeeDao eDoa = DaoFactory.getEmployeDao();

    private Ticket getTicketFromRS(ResultSet rs) {
        Ticket ticket = null;
        try {
            int ticket_id = rs.getInt("ticket_id");
            double ticket_amount = rs.getDouble("ticket_amount");
            String ticket_description = rs.getString("ticket_description");
            Timestamp ticket_timestamp = rs.getTimestamp("ticket_timestamp");
            String ticket_status = rs.getString("ticket_status");
            String ticket_category = rs.getString("ticket_category");
            int employee_id = rs.getInt("employee_id");
            ticket = new Ticket(ticket_id, ticket_amount, ticket_description, ticket_timestamp, ticket_status,
                    ticket_category, employee_id);
        } catch (SQLException e) {e.printStackTrace();}
        return ticket;
    }

//    private Employee getEmployee(String email, String password) {
//        return eDoa.getEmployee(email, password);
//    }

    public boolean insert(Ticket ticket, int employee_id) {
        boolean isSuccess = false;
        String query = "insert into tickets (ticket_amount, ticket_description, ticket_category, employee_id) VALUES" +
                "(?, ?, ?, ?);";
        try {
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, ticket.getAmount());
            ps.setString(2, ticket.getDescription());
            ps.setString(3, ticket.getExpenseCategory());
            ps.setInt(4, employee_id);
            int count = ps.executeUpdate();
            if (count > 0) isSuccess = true;
        } catch (Exception ex) {}
        return isSuccess;
    }

    public UDArray<Ticket> getTickets(int employee_id, String order) {
        UDArray<Ticket> tickets = new UDArray<>();
        String DESCOrASC = order.equals("DESC") ? "DESC" : "ASC";
        String query = String.format("SELECT * from tickets where employee_id = %s ORDER BY ticket_timestamp %s;",
                employee_id, DESCOrASC);
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Ticket ticket = getTicketFromRS(rs);
                tickets.add(ticket);
            }
        } catch (Exception ex) {ex.printStackTrace();}
        return tickets;
    }

    public UDArray<Ticket> getTickets() {
        UDArray<Ticket> tickets = new UDArray<>();
       String query = ("SELECT * from tickets;");
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Ticket ticket = getTicketFromRS(rs);
                tickets.add(ticket);
            }
        } catch (Exception ex) {ex.printStackTrace();}
        return tickets;
    }

    public boolean updateTicketStatus(int ticket_id, String new_status) {
        boolean isSuccessful = false;
        String query = "Update tickets SET ticket_status = ? where ticket_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, new_status);
            ps.setInt(2, ticket_id);
            isSuccessful = ps.executeUpdate() > 0;
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return isSuccessful;
    }
}
