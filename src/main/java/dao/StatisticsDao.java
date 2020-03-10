package dao;

import dao.exceptions.DAOException;
import domain.model.Statistics;

public interface StatisticsDao {

    void save(Statistics statistics) throws DAOException;

    void update(Statistics statistics) throws DAOException;

    void delete(Statistics statistics) throws DAOException;

    Statistics get() throws DAOException;
}
