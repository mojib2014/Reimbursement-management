package daos;

import datastructure.UDArray;
import entities.Ticket;

import java.util.List;

public interface TicketDao {
    String insert(Ticket data);
    boolean update(Ticket data);
    boolean delete(int id);
    Ticket getById(int id);
    UDArray<Ticket> getAll(List<String> options);
    /**
     * For H2 database tests
     */
    void initTables();
    void fillTables();
}
