package daos;

import datastructure.UDArray;
import entities.User;

public interface UserDao {
    User register(User user);
    User login(String email, String password);
    User update(User data);
    boolean delete(int id);
    User getById(int id);
    UDArray<User> getAll();
    /**
     * For H2 database tests
     */
    void initTables();
    void fillTables();
}
