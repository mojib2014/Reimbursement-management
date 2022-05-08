package daos;

import datastructure.UDArray;
import entities.Ticket;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import util.DateTimeZone;

public class ManagerDaoImpl extends UserdaoImpl {
    private TicketDao ticketDao = DaoFactory.getTicketDao();

    public boolean updateTicketStatus(int ticket_id, String status) {
        String query = "UPDATE tickets SET updated_at = ?, status = ? WHERE ticket_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setTimestamp(1, DateTimeZone.getDateTimeZone());
            st.setString(2, status);
            st.setInt(3, ticket_id);
            int count = st.executeUpdate();
            if(count == 1) return true;
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return false;
    }

    public UDArray<Ticket> getAllTickets(List<String> options) {
        return ticketDao.getAll(options);
    }

    public Ticket getATicket(int ticket_id) {
        return ticketDao.getById(ticket_id);
    }
}
