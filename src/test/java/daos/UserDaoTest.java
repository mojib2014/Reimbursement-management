package daos;

import datastructure.UDArray;
import entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.*;

public class UserDaoTest {
    private static UserDao userDao;

    @BeforeEach
    public void setup() {
        userDao = DaoFactory.getUserDao();
        userDao.initTables();
        userDao.fillTables();
    }

    @Test
    public void shouldRegisterAUser() {
        User user = new User("newUser", "newUser@email.com", "user5123", "Employee");
        User dbUser = userDao.register(user);

        assertEquals("newUser", dbUser.getName());
        assertEquals(5, dbUser.getUser_id());
    }

    @Test
    public void shouldNotRegisterAUser() {
        User user1 = new User("newUser1", "newUser1@email.com", "user15123", "Employee");
        User dbUser1 = userDao.register(user1);

        User user2 = new User("newUser1", "newUser1@email.com", "user15123", "Employee");
        User dbUser2 = userDao.register(user2);

        assertNull(dbUser2);
    }

    @Test
    public void shouldLoginAUser() {
        User user = new User("login", "login@email.com", "login123", "Employee");
        User dbUser = userDao.register(user);

        User loggedInUser = userDao.login(dbUser.getEmail(), user.getPassword());

        assertEquals(5, loggedInUser.getUser_id());
    }

    @Test
    public void shouldNotLoginAUser() {
        User user = new User("login1", "login1@email.com", "login1123", "Employee");
        User dbUser = userDao.register(user);

        User loggedInUser = userDao.login("wrong email", user.getPassword());

        assertNull(loggedInUser);
    }

    @Test
    public void shouldUpdateAUser() {
        User user = new User(1,"updatedUser", "updatedUser@email.com", "user5123", "Employee");
        User resUser = userDao.update(user);

        assertEquals("updatedUser", resUser.getName());
        assertEquals(1, resUser.getUser_id());
    }

    @Test
    public void shouldNotUpdate() {
        User user = new User(100,"updatedUser", "updatedUser@email.com", "user5123", "Employee");
        User resUser = userDao.update(user);

        assertNull(resUser);
    }

    @Test
    public void shouldDeleteUser() {
        boolean res = userDao.delete(1);
        User dbUser = userDao.getById(1);

        assertEquals(null, dbUser);
        assertEquals(true, res);
    }

    @Test
    public void shouldGetUserById() {
        User user1 = userDao.getById(1);
        User user2 = userDao.getById(2);
        assertEquals("user1", user1.getName());
        assertEquals(1, user1.getUser_id());
        assertEquals("user2", user2.getName());
        assertEquals(2, user2.getUser_id());
    }

    @Test
    public void shouldGetAllUsers() {
        UDArray<User> users = userDao.getAll();
        assertEquals(4, users.getSize());
        assertEquals(2, users.get(1).getUser_id());
    }
}
