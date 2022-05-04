package daos;

public class DaoFactory {
    private static Dao ticketDao;

    public static Dao getTicketDao() {
        if(ticketDao == null) {
            ticketDao = new TicketDaoImpl();
        }
        return ticketDao;
    }
}
