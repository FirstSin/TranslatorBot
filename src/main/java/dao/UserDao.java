package dao;

import dao.exceptions.DAOException;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

public interface UserDao {

    User findById(int id) throws DAOException;

    void save(User user) throws DAOException;

    void update(User user) throws DAOException;

    void delete(User user) throws DAOException;

    List<User> findAll() throws DAOException;
}
