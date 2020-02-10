package dao.utils;

import org.apache.log4j.Logger;
import org.postgresql.ds.PGPooledConnection;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtils {
    private static final Logger logger = Logger.getLogger(ConnectionUtils.class);
    private static String url;
    private static String name;
    private static String password;
    private static PGPooledConnection source;

    static {
        try {
            setProperties();
        } catch (URISyntaxException e) {
            logger.error("Problems with heroku database", e);
        }
        logger.info("Connection properties set successfully");
    }

    private ConnectionUtils() {
        throw new AssertionError("Cannot create an instance of a class");
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

    private static void setProperties() throws URISyntaxException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));
        name = dbUri.getUserInfo().split(":")[0];
        password = dbUri.getUserInfo().split(":")[1];
        url = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
    }
}
