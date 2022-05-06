import daos.Dao;
import daos.DaoFactory;
import datastructure.UDArray;
import entities.Ticket;
import entities.User;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.*;

public class TicketDaoTest {
    private static Dao<Ticket> ticketDao;
    private static Dao<User> userDao;

    @Before
    public void setup() {
        userDao = DaoFactory.getUserDao();
        ticketDao = DaoFactory.getTicketDao();

        userDao.initTables();
        ticketDao.initTables();
    }

    @Test
    public void shouldInsertATicket() {
        userDao.fillTables();
        Ticket ticket = new Ticket(55.09522, "description ticket 5", "category5", 1);
        String res = ticketDao.insert(ticket);

        assertEquals("Success", res);
        assertEquals(6, ticket.getTicket_id());
    }

    @Test
    public void shouldNotInsertATicket() {
        Ticket ticket = new Ticket(55.09522, "description ticket 5", "category5", -1);
        String res = ticketDao.insert(ticket);

        assertEquals(null, res);
    }

    @Test
    public void shouldUpdateATicket() {
        userDao.fillTables();
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
        userDao.fillTables();
        Ticket ticket = ticketDao.getById(1);
        Timestamp updatedAt = Timestamp.valueOf("2022-05-05 16:06:50.952296");
        Ticket updatedTicket = new Ticket(-1, 20.5, "updated description ticket 5", ticket.getCreated_at(), updatedAt, "Approved" ,"updated category5", 1);
        boolean res = ticketDao.update(updatedTicket);

        assertNull(res);
    }

    @Test
    public void shouldDeleteTicketById() {
        userDao.fillTables();
        ticketDao.fillTables();

        boolean res = ticketDao.delete(1);

        Ticket ticket = ticketDao.getById(1);

        assertTrue(res);
        assertEquals(null, ticket);
    }

    @Test
    public void shouldGetATicketById() {
        userDao.fillTables();
        Ticket dbTicket = ticketDao.getById(1);

        assertEquals(20.5, dbTicket.getAmount(), 2);
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
        userDao.fillTables();
        ticketDao.fillTables();
        UDArray<Ticket> tickets = ticketDao.getAll();

        assertEquals(4, tickets.getSize());
        assertEquals(1, tickets.get(0).getTicket_id());
    }
}
