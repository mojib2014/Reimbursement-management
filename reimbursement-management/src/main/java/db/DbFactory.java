package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DbFactory {
    private static Connection connection;

    public static Connection getConnection() {
        if(connection == null) {
            try {
                Class.forName("org.postgresql.Driver");
            }catch (ClassNotFoundException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
            try {
                ResourceBundle bundle = ResourceBundle.getBundle("development");
                String url = bundle.getString("url");
                String username = bundle.getString("username");
                String password = bundle.getString("password");
                connection = DriverManager.getConnection(url, username, password);
            }catch (SQLException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
        return connection;
    }
}
