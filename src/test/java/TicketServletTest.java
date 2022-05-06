import daos.Dao;
import daos.DaoFactory;
import entities.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

public class TicketServletTest {
    private Dao<Ticket> ticketDao;

    @BeforeEach
    public void setup() {
        ticketDao = DaoFactory.getTicketDao();
        ticketDao.initTables();
        ticketDao.fillTables();
    }

    @Test
    public void shouldGetTicketById() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);


    }
}
