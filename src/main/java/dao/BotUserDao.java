package dao;

import dao.exceptions.DAOException;
import domain.model.BotUser;

import java.util.List;

public interface BotUserDao {

    BotUser findById(int id) throws DAOException;

    void save(BotUser user) throws DAOException;

    void update(BotUser user) throws DAOException;

    void delete(BotUser user) throws DAOException;

    List<BotUser> findAll() throws DAOException;
}
