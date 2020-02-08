package dao.utils;

import dao.BotUserDao;
import dao.BotUserDaoImpl;
import org.apache.log4j.Logger;

public class DaoFactory {
    private static final Logger logger = Logger.getLogger(DaoFactory.class);
    private static DaoFactory factory;

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
        if (factory == null) {
            factory = new DaoFactory();
            logger.info("DaoFactory instance created");
        }
        return factory;
    }

    public BotUserDao getUserDao() {
        return new BotUserDaoImpl();
    }
}
