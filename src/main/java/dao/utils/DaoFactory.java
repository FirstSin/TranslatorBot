package dao.utils;

import dao.BotUserDao;
import dao.BotUserDaoImpl;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DaoFactory {
    private static final Logger logger = Logger.getLogger(DaoFactory.class);
    private static DaoFactory factory;

    private DaoFactory(){}

    public static DaoFactory getInstance(){
        if(factory == null){
            factory = new DaoFactory();
        }
        return factory;
    }

    public BotUserDao getUserDao(){
        return new BotUserDaoImpl();
    }

    public Connection getConnection() throws SQLException {
        Connection connection = null;
        try (InputStream in = new FileInputStream("src/main/resources/connection.properties")) {
            Properties prop = new Properties();
            prop.load(in);
            String username = prop.getProperty("username");
            String url = prop.getProperty("url");
            String password = prop.getProperty("password");
            connection = DriverManager.getConnection(url, username, password);
        } catch (IOException e) {
            logger.error("An error occurred while loading connection properties. ", e);
        }
        return connection;
    }
}
