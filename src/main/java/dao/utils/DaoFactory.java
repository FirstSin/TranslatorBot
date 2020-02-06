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
            logger.info("Creating DaoFactory instance");
            factory = new DaoFactory();
        }
        return factory;
    }

    public BotUserDao getUserDao(){
        return new BotUserDaoImpl();
    }
}
