package dao;

import dao.exceptions.DAOException;
import dao.utils.ConnectionUtils;
import domain.model.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticsDaoImpl implements StatisticsDao {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsDaoImpl.class);
    private static final String SAVE_STATS_QUERY = "INSERT INTO statistics (start, help, langinfo, setmylang, tolang, users, translated_words) VALUES (?,?,?,?,?,?,?)";
    private static final String UPDATE_STATS_QUERY = "UPDATE statistics SET start = ?, help = ?, langinfo = ?, setmylang = ?, tolang = ?, users = ?, translated_words = ? WHERE id = ?";
    private static final String DELETE_STATS_QUERY = "DELETE FROM statistics WHERE id = ?";
    private static final String GET_STATS_QUERY = "SELECT * FROM statistics WHERE id = ?";
    @Override
    public void save(Statistics statistics) throws DAOException {
        logger.trace("Saving statistics");
        try {
            Connection connection = ConnectionUtils.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(SAVE_STATS_QUERY)) {
                setStatisticsProps(statement, statistics);
                statement.executeUpdate();
                logger.trace("Statistics saved");
            }
            ConnectionUtils.putConnection(connection);
        } catch (SQLException e) {
            logger.error("Can't save statistics", e);
            throw new DAOException("Can't save statistics", e);
        }
    }

    @Override
    public void update(Statistics statistics) throws DAOException {
        logger.trace("Updating statistics");
        try {
            Connection connection = ConnectionUtils.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_STATS_QUERY)) {
                setStatisticsProps(statement, statistics);
                statement.setInt(8, 1);
                statement.executeUpdate();
                logger.trace("Statistics updated");
            }
            ConnectionUtils.putConnection(connection);
        } catch (SQLException e) {
            logger.error("Can't update statistics", e);
            throw new DAOException("Can't update statistics", e);
        }
    }

    @Override
    public void delete(Statistics statistics) throws DAOException {
        logger.trace("Deleting statistics");
        try {
            Connection connection = ConnectionUtils.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(DELETE_STATS_QUERY)) {
                statement.setInt(1, 1);
                statement.executeUpdate();
                logger.trace("Statistics deleted");
            }
            ConnectionUtils.putConnection(connection);
        } catch (SQLException e) {
            logger.error("Can't delete statistics", e);
            throw new DAOException("Can't delete statistics", e);
        }
    }

    @Override
    public Statistics get() throws DAOException {
        Statistics statistics = null;
        try {
            Connection connection = ConnectionUtils.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(GET_STATS_QUERY)) {
                statement.setInt(1, 1);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        long startCount = rs.getLong("start");
                        long helpCount = rs.getLong("help");
                        long langInfoCount = rs.getLong("langinfo");
                        long setMyLangCount = rs.getLong("setmylang");
                        long toLangCount = rs.getLong("tolang");
                        long usersCount = rs.getLong("users");
                        long translatedWordsCount = rs.getLong("translated_words");
                        statistics = new Statistics(startCount, helpCount, langInfoCount, setMyLangCount, toLangCount, usersCount, translatedWordsCount);
                    }
                    logger.trace("Statistics were found");
                }
            }
            ConnectionUtils.putConnection(connection);
        } catch (SQLException e) {
            logger.error("Can't find statistics", e);
            throw new DAOException("Can't find statistics", e);
        }
        return statistics;
    }

    private void setStatisticsProps(PreparedStatement statement, Statistics statistics) throws SQLException {
        statement.setLong(1, statistics.getStartCount());
        statement.setLong(2, statistics.getHelpCount());
        statement.setLong(3, statistics.getLangInfoCount());
        statement.setLong(4, statistics.getSetMyLangCount());
        statement.setLong(5, statistics.getToLangCount());
        statement.setLong(6, statistics.getUsersCount());
        statement.setLong(7, statistics.getTranslatedWordsCount());
    }
}
