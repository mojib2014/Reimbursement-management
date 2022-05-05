package daos;

public class DaoFactory {
    private static Dao ticketDao;
    private static Dao userDao;

    public static Dao getTicketDao() {
        if(ticketDao == null) {
            ticketDao = new TicketDaoImpl();
        }
        return ticketDao;
    }
    public static Dao getUserDao() {
        if(userDao == null) {
            userDao = new UserdaoImpl();
        }
        return userDao;
    }
}
