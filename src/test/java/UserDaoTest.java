import daos.Dao;
import daos.DaoFactory;
import entities.User;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserDaoTest {
    private static Dao<User> userDao;

    @Before
    public void setup() {
        userDao = DaoFactory.getUserDao();
        userDao.initTables();
    }

    @Test
    public void shouldInsertAUser() {
        User user = new User(5,"user5", "user5@email.com", "user5123", "Employee");
        String res = userDao.insert(user);

        User dbUser = userDao.getById(user.getUser_id());

        assertEquals("user5", dbUser.getName());
        assertEquals(5, dbUser.getUser_id());
        assertEquals("Success", res);
    }

    @Test
    public void shouldUpdateAUser() {
        userDao.fillTables();
        User user = new User(1,"updatedUser", "updatedUser@email.com", "user5123", "Employee");
        boolean res = userDao.update(user);
        User dbUser = userDao.getById(user.getUser_id());

        assertEquals("updatedUser", dbUser.getName());
        assertEquals(1, dbUser.getUser_id());
        assertTrue(res);
    }

    @Test
    public void shouldDeleteUser() {
        userDao.fillTables();
        boolean res = userDao.delete(1);
        User dbUser = userDao.getById(1);

        assertEquals(null, dbUser);
        assertEquals(true, res);
    }

    @Test
    public void shouldGetUserById() {
        userDao.fillTables();
        User user1 = userDao.getById(1);
        User user2 = userDao.getById(2);
        assertEquals("user1", user1.getName());
        assertEquals(1, user1.getUser_id());
        assertEquals("user2", user2.getName());
        assertEquals(2, user2.getUser_id());
    }

    @Test
    public void shouldGetAllUsers() {
        userDao.fillTables();
        List<User> users = userDao.getAll();
        assertEquals(4, users.size());
        assertEquals(2, users.get(1).getUser_id());
    }
}
