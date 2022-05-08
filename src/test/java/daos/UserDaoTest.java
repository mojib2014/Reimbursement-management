package daos;

import datastructure.UDArray;
import entities.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserDaoTest {
    private static UserDao userDao;

    @Before
    public void setup() {
        userDao = DaoFactory.getUserDao();
        userDao.initTables();
    }

    @Test
    public void shouldRegisterAUser() {
        User user = new User("newUser", "newUser@email.com", "user5123", "Employee");
        User dbUser = userDao.register(user);

        assertEquals("newUser", dbUser.getName());
        assertEquals(6, dbUser.getUser_id());
    }

    @Test
    public void shouldLoginAUser() {
        User user = new User("login", "login@email.com", "login123", "Employee");
        User dbUser = userDao.register(user);

        User loggedInUser = userDao.login(dbUser.getEmail(), user.getPassword());

        assertEquals(5, loggedInUser.getUser_id());
    }

    @Test
    public void shouldUpdateAUser() {
        userDao.fillTables();
        User user = new User(1,"updatedUser", "updatedUser@email.com", "user5123", "Employee");
        User resUser = userDao.update(user);

        assertEquals("updatedUser", resUser.getName());
        assertEquals(1, resUser.getUser_id());
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
        UDArray<User> users = userDao.getAll();
        assertEquals(4, users.getSize());
        assertEquals(2, users.get(1).getUser_id());
    }
}
