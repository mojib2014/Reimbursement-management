package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DbFactory {
    private static Connection connection = null;

    /**
     * Intentionally disallow instantiation of this class
     * @return
     */
    private DbFactory() {}

    public static Connection getConnection() {
        if(connection == null) {
            boolean test = false;
            String driver = test ? "org.h2.Driver" : "org.postgresql.Driver";
            String configFile = test ? "test" : "development";
            try {
                Class.forName(driver);
            }catch (ClassNotFoundException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
            try {
                ResourceBundle bundle = ResourceBundle.getBundle(configFile);
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
