package dao.utils;

import org.apache.log4j.Logger;
import org.postgresql.ds.PGPooledConnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtils {
    private static final Logger logger = Logger.getLogger(ConnectionUtils.class);
    private static String url;
    private static String name;
    private static String password;
    private static PGPooledConnection source;

    static {
        setProperties();
        logger.info("Connection properties set successfully");
    }

    private ConnectionUtils() {
    }

    public static Connection getConnection() throws SQLException {
        logger.trace("Taking a connection from the pool");
        if (source == null)
            source = new PGPooledConnection(DriverManager.getConnection(url, name, password), true);
        return source.getConnection();
    }

    public static void putConnection(Connection connection) throws SQLException {
        logger.trace("Returning the connection to the pool");
        connection.close();
    }

    private static void setProperties() {
        try (InputStream in = new FileInputStream("src/main/resources/connection.properties")) {
            Properties prop = new Properties();
            prop.load(in);
            name = prop.getProperty("username");
            url = prop.getProperty("url");
            password = prop.getProperty("password");
        } catch (IOException e) {
            logger.error("An error occurred while loading connection properties", e);
        }
    }
}
