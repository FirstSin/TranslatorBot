package dao.services;

import dao.UserDao;
import dao.UserDaoImpl;
import dao.exceptions.DAOException;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

public class UserService {
    private UserDao userDao = new UserDaoImpl();

    User findUser(int id) throws DAOException {
        return userDao.findById(id);
    }

    void saveUser(User user) throws DAOException{
        userDao.save(user);
    }

    void updateUser(User user) throws DAOException{
        userDao.update(user);
    }

    void deleteUser(User user) throws DAOException{
        userDao.delete(user);
    }

    List<User> findAllUsers() throws DAOException{
        return userDao.findAll();
    }
}
