package daos;

import entities.Ticket;
import entities.User;

public class DaoFactory {
    private static Dao<Ticket> ticketDao;
    private static Dao<User> userDao;

    /**
     * Intentionally disallow instantiating this class
     */
    private DaoFactory() {}

    public static Dao<Ticket> getTicketDao() {
        if(ticketDao == null) {
            ticketDao = new TicketDaoImpl();
        }
        return ticketDao;
    }
    public static Dao<User> getUserDao() {
        if(userDao == null) {
            userDao = new UserdaoImpl();
        }
        return userDao;
    }
}
