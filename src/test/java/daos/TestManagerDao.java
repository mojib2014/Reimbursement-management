package daos;

import datastructure.UDArray;
import entities.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestManagerDao {
    private ManagerDaoImpl managerDao = new ManagerDaoImpl();
    private UserDao userDao = DaoFactory.getUserDao();
    private TicketDao ticketDao = DaoFactory.getTicketDao();

    @BeforeEach
    public void setup() {
        userDao.initTables();
        userDao.fillTables();
        ticketDao.initTables();
        ticketDao.fillTables();
    }

    @Test
    public void shouldUpdateTicketStatus() {
        managerDao.updateTicketStatus(1, "Approved");

        Ticket ticket = ticketDao.getById(1);

        assertEquals("Approved", ticket.getStatus());
    }

    @Test
    public void shouldNotUpdateTicketStatus() {
        boolean res = managerDao.updateTicketStatus(10, "Approved");

        assertFalse(res);
    }

//    @Test
//    public void shouldFailUpdating() {
//        ManagerDaoImpl mock = mock(ManagerDaoImpl.class);
//        when(mock.updateTicketStatus(10, "Approved")).thenThrow(SQLException.class);
//
//        mock.updateTicketStatus(10, "Approved");
//    }

    @Test
    public void shouldGetAllTicketsDescending() {
        List<String> options = new ArrayList<>();
        options.add("DESC");
        UDArray<Ticket> tickets = managerDao.getAllTickets(options);

        assertEquals(4, tickets.get(0).getTicket_id());
    }

    @Test
    public void shouldGetAllTicketsPending() {
        List<String> options = new ArrayList<>();
        options.add("Pending");
        UDArray<Ticket> tickets = managerDao.getAllTickets(options);

        assertEquals("Pending", tickets.get(0).getStatus());
        assertEquals("Pending", tickets.get(1).getStatus());
        assertEquals("Pending", tickets.get(2).getStatus());
        assertEquals("Pending", tickets.get(3).getStatus());
    }

    @Test
    public void shouldGetATicketById() {
        Ticket ticket = managerDao.getATicket(1);


        assertEquals("Pending", ticket.getStatus());
        assertEquals(1, ticket.getTicket_id());
    }
}
