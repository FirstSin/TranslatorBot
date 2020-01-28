package dao.services;

import dao.UserDao;
import dao.UserDaoImpl;
import dao.exceptions.DAOException;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

public class UserService {
    private UserDao userDao = new UserDaoImpl();

    public User findUser(int id) throws DAOException {
        return userDao.findById(id);
    }

    public void saveUser(User user) throws DAOException{
        userDao.save(user);
    }

    public void updateUser(User user) throws DAOException{
        userDao.update(user);
    }

    public void deleteUser(User user) throws DAOException{
        userDao.delete(user);
    }

    public List<User> findAllUsers() throws DAOException{
        return userDao.findAll();
    }
}
