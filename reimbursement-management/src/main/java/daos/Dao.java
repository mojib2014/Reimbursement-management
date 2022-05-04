package daos;

public interface Dao<T> {
    boolean insert(T data);
    boolean update(T data);
    boolean delete(int id);
    T get(int id);
    T getAll();
}
