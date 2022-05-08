package daos;

public class DaoFactory {
    private static TicketDao ticketDao;
    private static UserDao userDao;

    /**
     * Intentionally disallow instantiating this class
     */
    private DaoFactory() {}

    public static TicketDao getTicketDao() {
        if(ticketDao == null) {
            ticketDao = new TicketDaoImpl();
        }
        return ticketDao;
    }
    public static UserDao getUserDao() {
        if(userDao == null) {
            userDao = new UserdaoImpl();
        }
        return userDao;
    }
}
