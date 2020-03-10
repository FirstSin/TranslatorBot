package dao.services;

import dao.StatisticsDao;
import dao.StatisticsDaoImpl;
import dao.exceptions.DAOException;
import domain.model.Statistics;

public class StatisticsService {
    private StatisticsDao statisticsDao = new StatisticsDaoImpl();

    public void saveStatistics(Statistics statistics) throws DAOException {
        statisticsDao.save(statistics);
    }

    public void updateStatistics(Statistics statistics) throws DAOException{
        statisticsDao.update(statistics);
    }

    public void deleteStatistics(Statistics statistics) throws DAOException{
        statisticsDao.delete(statistics);
    }

    public Statistics getStatistics() throws DAOException{
        return statisticsDao.get();
    }
}
