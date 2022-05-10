package daos;

import datastructure.UDArray;
import db.DbFactory;
import entities.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserdaoImpl implements UserDao {

    Connection connection;

    public UserdaoImpl() {
        connection = DbFactory.getConnection();
    }

    /**
     * Inserts a user record in users table
     * @param user
     * @return String success if inserted else empty string and also the string error from db
     */
    @Override
    public User register(User user) {
        String query = "INSERT INTO users(name, email, password, user_type) VALUES(?, ?, ?, ?);";
        try {
            String hashedPassword = hashPassword(user.getPassword());
            PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashedPassword);
            statement.setString(4, user.getUser_type());
            int count = statement.executeUpdate();
            if (count == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                resultSet.next();
                int user_id = resultSet.getInt(1);
                User dbUser = getById(user_id);
                return dbUser;
            }
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     *  Employee/Manager login route with email and password
     * @param email
     * @param password
     * @return User
     */
    public User login(String email, String password) {
        User user = this.getProfileByEmail(email);
        try {
            if(user == null) System.out.println("This email is not registered");
            else if(BCrypt.checkpw(password, user.getPassword())) {
                String query = "SELECT * FROM users WHERE user_id = ?;";
                PreparedStatement st = connection.prepareStatement(query);
                st.setInt(1, user.getUser_id());
                ResultSet resultSet = st.executeQuery();
                while (resultSet.next()) {
                    User dbUser = getUserFromRS(resultSet);
                    return dbUser;
                }
            }else System.out.println("Wrong username or password");
        }catch (SQLException ex) {
            System.out.println("Logging you in failed: " + ex.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Updates a user by id in db
     * @param user
     * @return true if found and updated else false
     */
    @Override
    public User update(User user) {
        String query = "UPDATE users Set name = ?, email = ?, password = ?, " +
                        "user_type = ? WHERE  user_id = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getUser_type());
            statement.setInt(5, user.getUser_id());
            int count = statement.executeUpdate();
            if (count == 1) {
                return getById(user.getUser_id());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Removes an existing user from db and cascades (user_id is foreign key in tickets table)
     * @param id
     * @return true if found and removed else false
     */
    @Override
    public boolean delete(int id) {
        boolean isSuccess = false;
        String query = "DELETE FROM users where user_id = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            int count = statement.executeUpdate();
            if (count == 1) {
                isSuccess = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return isSuccess;

    }

    /**
     * Retrieves a specific user from db
     * @param user_id
     * @return user
     */
    @Override
    public User getById(int user_id) {
        String query = "SELECT * FROM users where user_id = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user_id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return getUserFromRS(rs);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Queries all the users from postgres db
     * @return A custom-built array list of users
     */
    @Override
    public UDArray getAll() {
        UDArray<User> users = new UDArray<>();
        String query = String.format("SELECT * FROM users;");
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                User user = getUserFromRS(rs);
                users.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }

    /**
     * Helper methods
     */

    /**
     * Extracts user from resultSet returned by PreparedStatement
     * @param resultSet  resultSet
     * @return user
     */
    private User getUserFromRS(ResultSet resultSet) {
        try {
            int user_id = resultSet.getInt("user_id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            String user_type = resultSet.getString("user_type");
            return new User(user_id, name, email, password, user_type);
        } catch (SQLException e) {
            System.out.println("Cannot get User from resultSet");
            e.printStackTrace();
        }
        return null;
    }

    protected String hashPassword(String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        return hashedPassword;
    }

    public User getProfileByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?;";
        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, email);
            ResultSet res = st.executeQuery();
            if(res.next()) {
                return getUserFromRS(res);
            }else System.out.println("Could not find a user by that email");
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return null;
    }
    // End of helper methods

    /**
     * Setup for H2 database tests
     */
    @Override
    public void initTables() {
        String query = "DROP TABLE users IF EXISTS CASCADE; CREATE TABLE users(user_id serial primary key, name varchar(50) not null, email " +
                "varchar(100) not null unique, password varchar(200) not null, user_type varchar(50));";

        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.executeUpdate();
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Override
    public void fillTables() {
        String query = "INSERT INTO users (user_id, name, email, password, user_type) " +
                "VALUES (1, 'user1', 'user1@email.com', 'user1123', 'Employee');";
        query += "INSERT INTO users (user_id, name, email, password, user_type) " +
                "VALUES (2, 'user2', 'user2@email.com', 'user2123', 'Employee');";
        query += "INSERT INTO users (user_id, name, email, password, user_type) " +
                "VALUES (3, 'user3', 'user3@email.com', 'user3123', 'Employee');";
        query += "INSERT INTO users (user_id, name, email, password, user_type) " +
                "VALUES (4, 'user4', 'user4@email.com', 'user4123', 'Manager');";

        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.executeUpdate();
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
    // End of test setup
}

