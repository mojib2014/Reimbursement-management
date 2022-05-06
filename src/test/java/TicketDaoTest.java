import daos.Dao;
import daos.DaoFactory;
import entities.Ticket;
import entities.User;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TicketDaoTest {
    private static Dao<Ticket> ticketDao;
    private static Dao<User> userDao;

    @Before
    public void setup() {
        userDao = DaoFactory.getUserDao();
        ticketDao = DaoFactory.getTicketDao();

        userDao.initTables();
        userDao.fillTables();
        ticketDao.initTables();
    }

    @Test
    public void shouldInsertATicket() {
        Ticket ticket = new Ticket(55.09522, "description ticket 5", "category5", 1);
        String res = ticketDao.insert(ticket);

        assertEquals("Success", res);
        assertEquals(1, ticket.getTicket_id());
    }

    @Test
    public void shouldUpdateATicket() {
        Ticket ticket = new Ticket(100.15, "description ticket 5", "category5", 1);
        String res = ticketDao.insert(ticket);

        assertEquals("Success", res);
        assertEquals(1, ticket.getTicket_id());
    }
}
