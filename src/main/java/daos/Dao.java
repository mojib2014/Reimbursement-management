package daos;

import java.util.List;

public interface Dao<T> {
    boolean insert(T data);
    boolean update(T data);
    boolean delete(int id);
    T get(int id);
    List<T> getAll();
}
