package daos;

import datastructure.UDArray;
import entities.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TicketDaoTest {
    private static TicketDao ticketDao;
    private static UserDao userDao;

    @BeforeEach
    public void setup() {
        userDao = DaoFactory.getUserDao();
        ticketDao = DaoFactory.getTicketDao();

        userDao.initTables();
        ticketDao.initTables();
        userDao.fillTables();
        ticketDao.fillTables();
    }

    @Test
    public void shouldInsertATicket() {
        Ticket ticket = new Ticket(55.09522, "description ticket 5", "category5", 1);
        String res = ticketDao.insert(ticket);

        assertEquals("Success", res);
        assertEquals(5, ticket.getTicket_id());
    }

    @Test
    public void shouldNotInsertATicket() {
        Ticket ticket = new Ticket(55.09522, "description ticket 5", "category5", -1);
        String res = ticketDao.insert(ticket);

        assertEquals(null, res);
    }

    @Test
    public void shouldUpdateATicket() {
        Ticket ticket = ticketDao.getById(1);
        Timestamp updatedAt = Timestamp.valueOf("2022-05-05 16:06:50.952296");
        Ticket updatedTicket = new Ticket(ticket.getTicket_id(), 20.5, "updated description ticket 5", ticket.getCreated_at(), updatedAt, "Approved" ,"updated category5", 1);
        boolean res = ticketDao.update(updatedTicket);

        assertTrue(res);
        assertEquals(20.5, updatedTicket.getAmount(), 2);
        assertEquals(1, updatedTicket.getTicket_id());
    }

    @Test
    public void shouldNotUpdateATicket() {
        Ticket ticket = ticketDao.getById(1);
        Timestamp updatedAt = Timestamp.valueOf("2022-05-05 16:06:50.952296");
        Ticket updatedTicket = new Ticket(-1, 20.5, "updated description ticket 5", ticket.getCreated_at(), updatedAt, "Approved" ,"updated category5", 1);
        boolean res = ticketDao.update(updatedTicket);

        assertFalse(res);
    }

    @Test
    public void shouldDeleteTicketById() {
        boolean res = ticketDao.delete(1);

        Ticket ticket = ticketDao.getById(1);

        assertTrue(res);
        assertEquals(null, ticket);
    }

    @Test
    public void shouldNotDeleteTicketById() {
        boolean res = ticketDao.delete(100);

        assertFalse(res);
    }

    @Test
    public void shouldGetATicketById() {
        Ticket dbTicket = ticketDao.getById(1);

        assertEquals(25.5, dbTicket.getAmount(), 100);
        assertEquals(1, dbTicket.getTicket_id());
    }

//    @Test
//    public void shouldGetATicketByDate() {
//        userDao.fillTables();
//        Ticket dbTicket = ticketDao.getByDate(1);
//
//        assertEquals(20.5, dbTicket.getAmount(), 2);
//        assertEquals(1, dbTicket.getTicket_id());
//    }

    @Test
    public void shouldGetAllTickets() {
        List<String> options = new ArrayList<>();
        options.add("ASC");
        UDArray<Ticket> tickets = ticketDao.getAll(options);

        assertEquals(4, tickets.getSize());
        assertEquals(1, tickets.get(0).getTicket_id());
    }
}
