package daos;

import datastructure.UDArray;
import db.DbFactory;
import entities.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserdaoImpl<T> implements Dao <T>{

    Connection connection;

    public UserdaoImpl() {
        connection = DbFactory.getConnection();
    }

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

    @Override
    public boolean insert(T data) {
        boolean isSuccess = false;
        User user = (User) data;
        String query = String.format("INSERT INTO users (name, email, password, user_type)" +
                        " VALUES ('%s', '%s', '%s', %s)", user.getName(), user.getEmail(), user.getPassword(),
                user.getUser_type());
        try {
            Statement statement = connection.createStatement();
            int count = statement.executeUpdate(query);
            if (count > 1) {
                isSuccess = true;
            }
        } catch (SQLException e) {
            System.out.println("cannot insert user");
            e.printStackTrace();
        }
        return isSuccess;
    }

    @Override
    public boolean update(T data) {
        boolean isSuccess = false;
        User user = (User) data;
        String query = String.format("UPDATE from users Set id = %s, name = '%s', email = '%s', password = '%s', " +
                        "user_type = '%s'" + " where" + " id = %s;",
                user.getUser_id(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getPassword());
        try {
            Statement statement = connection.createStatement();
            int count = statement.executeUpdate(query);
            if (count > 1) {
                isSuccess = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    @Override
    public boolean delete(int id) {
        boolean isSuccess = false;
        String query = String.format("DELETE FROM users where id = %s", id);
        try {
            Statement statement = connection.createStatement();
            int count = statement.executeUpdate(query);
            if (count > 1) {
                isSuccess = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return isSuccess;

    }

    @Override
    public T get(int id) {
        User user = null;
        String query = String.format("SELECT * FROM users where id = %s", id);
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            user = getUserFromRS(rs);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return (T) user;
    }

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
}

