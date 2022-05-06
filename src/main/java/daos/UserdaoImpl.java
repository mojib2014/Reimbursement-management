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
    public String insert(T data) {
        User user = (User) data;
        String query = "INSERT INTO users (name, email, password, user_type) VALUES (?, ?, ?, ?);";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getUser_type());
            int count = statement.executeUpdate();
            if (count > 0) {
                return "Success";
            }
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return "";
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
    public T getById(int user_id) {
        User user = null;
        String query = String.format("SELECT * FROM users where user_id = %s", user_id);
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            user = getUserFromRS(rs);
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return (T) user;
    }

    @Override
    public UDArray getAll() {
        UDArray<User> users = new UDArray<>();
        String query = String.format("SELECT * FROM users ORDER BY id DESC;");
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

