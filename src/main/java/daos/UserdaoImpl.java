package daos;

import datastructure.UDArray;
import db.DbFactory;
import entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserdaoImpl<T> implements Dao <T>{

    Connection connection;

    public UserdaoImpl() {
        connection = DbFactory.getConnection();
    }

    /**
     * Inserts a user record in users table
     * @param data
     * @return String success if inserted else empty string and also the string error from db
     */
    @Override
    public String insert(T data) {
        User user = (User) data;
        String query = "INSERT INTO users (user_id, name, email, password, user_type) VALUES (default, ?, ?, ?, ?);";
        try {
            PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getUser_type());
            int count = statement.executeUpdate();
            if (count == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                resultSet.next();
                int user_id = resultSet.getInt(1);
                user.setUser_id(user_id);
                return "Success";
            }
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return "";
    }

    /**
     * Updates a user by id in db
     * @param data
     * @return true if found and updated else false
     */
    @Override
    public boolean update(T data) {
        boolean isSuccess = false;
        User user = (User) data;
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
                isSuccess = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSuccess;
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
    public T getById(int user_id) {
        User user = null;
        String query = "SELECT * FROM users where user_id = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user_id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            user = getUserFromRS(rs);
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return (T) user;
    }

    /**
     * Queries all the users from postgres db
     * @return A custom-built array list of users
     */
    @Override
    public List getAll() {
        List<User> users = new ArrayList<>();
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
     * @param rs  result
     * @return user
     */
    private User getUserFromRS(ResultSet rs) {
        User user = null;
        // int user_id, String name, String email, String password, String user_type
        try {
            user = new User(rs.getInt("user_id"), rs.getString("name"), rs.getString("email"),
                    rs.getString("password"), rs.getString("user_type"));
        } catch (SQLException e) {
            System.out.println("Cannot get Users from RS");
            e.printStackTrace();
        }
        return user;
    }
    // End of helper methods

    /**
     * Setup for H2 database tests
     */
    @Override
    public void initTables() {
        String query = "CREATE TABLE IF NOT EXISTS users(user_id serial primary key, name varchar(50) not null, email " +
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
                "VALUES(1, 'user1', 'user1@email.com', 'user1123', 'Employee');";
        query += "INSERT INTO users (user_id, name, email, password, user_type) " +
                "VALUES(2, 'user2', 'user2@email.com', 'user2123', 'Employee');";
        query += "INSERT INTO users (user_id, name, email, password, user_type) " +
                "VALUES(3, 'user3', 'user3@email.com', 'user3123', 'Employee');";
        query += "INSERT INTO users (user_id, name, email, password, user_type) " +
                "VALUES(4, 'user4', 'user4@email.com', 'user4123', 'Manager');";

        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.executeUpdate();
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
    // End of test setup
}

