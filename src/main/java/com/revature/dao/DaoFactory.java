package com.revature.dao;

import com.revature.entity.Ticket;

// this class doesn't seem super useful because we only have one Dao, but with multiple daos, its use becomes apparent
// multiple methods to return different daos:
// we only need 1 dao factory for the entire application, different method to return different daos
public class DaoFactory {
    private static EmployeeDao eDao;
    private static TicketDao tDao;
    private static ManagerDao mDao;

    // private constructor, intentionally disallow instantiation of this class:
    private DaoFactory() {

    }

    public static ManagerDao getManagerDao() {
        if (mDao == null) {
            mDao = new ManagerDao();
        }
        return mDao;
    }

    // check if null, return the dao
    public static EmployeeDao getEmployeDao() {
        if (eDao == null) {
            // we only ever call this once, we're reusing the same instance:
            eDao = new EmployeeDao();
        }
        return eDao;
    }

    public static TicketDao getTicketDao() {
        if (tDao == null) {
            // we only ever call this once, we're reusing the same instance:
            tDao = new TicketDao();
        }
        return tDao;
    }



}
