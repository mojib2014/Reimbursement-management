package daos;

import datastructure.UDArray;

public interface Dao<T> {
    String insert(T data);
    boolean update(T data);
    boolean delete(int id);
    T getById(int id);
//    T getByDate(Date date);
    UDArray<T> getAll();
    /**
     * For H2 database tests
     */
    void initTables();
    void fillTables();
}
