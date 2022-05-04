import daos.Dao;
import daos.TicketDaoImpl;

public class DaoFactory {
    private static Dao ticketDao;

    public static Dao getTicketDao() {
        if(ticketDao == null) {
            ticketDao = new TicketDaoImpl();
        }
        return ticketDao;
    }
}
