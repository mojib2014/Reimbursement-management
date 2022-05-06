package daos;

import java.util.List;

public interface Dao<T> {
    String insert(T data);
    boolean update(T data);
    boolean delete(int id);
    T getById(int id);
    List<T> getAll();
    /**
     * For H2 database tests
     */
    void initTables();
    void fillTables();
}
