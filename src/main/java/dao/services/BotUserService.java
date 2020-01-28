package dao.services;

import dao.BotUserDao;
import dao.BotUserDaoImpl;
import dao.exceptions.DAOException;
import domain.model.BotUser;

import java.util.List;

public class BotUserService {
    private BotUserDao userDao = new BotUserDaoImpl();

    public BotUser findUser(int id) throws DAOException {
        return userDao.findById(id);
    }

    public void saveUser(BotUser user) throws DAOException{
        userDao.save(user);
    }

    public void updateUser(BotUser user) throws DAOException{
        userDao.update(user);
    }

    public void deleteUser(BotUser user) throws DAOException{
        userDao.delete(user);
    }

    public List<BotUser> findAllUsers() throws DAOException{
        return userDao.findAll();
    }
}
