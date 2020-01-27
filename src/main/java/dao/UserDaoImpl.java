package dao;

import dao.exceptions.DAOException;
import dao.utils.DaoFactory;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoImpl.class);
    private DaoFactory daoFactory = DaoFactory.getInstance();
    private static final String SAVE_USER_QUERY = "INSERT INTO users (user_id, first_name, last_name, user_name, language_code, is_bot) VALUES (?,?,?,?,?,?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET user_id = ?, first_name = ?, last_name = ?, user_name = ?, language_code = ?, is_bot = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE user_id = ?";
    private static final String SELECT_USER_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM users";

    @Override
    public User findById(int id) throws DAOException {
        logger.trace("Finding user with id " + id);
        User user = null;
        try (Connection connection = daoFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SELECT_USER_QUERY)) {
                statement.setInt(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        user = parseUser(rs);
                    }
                    logger.trace("User with id " + id + " were found");
                }
            }
        } catch (SQLException e) {
            logger.error("Can't find user", e);
            throw new DAOException("Can't find user", e);
        }
        return user;
    }

    @Override
    public void save(User user) throws DAOException {
        logger.trace("Saving user with id " + user.getId());
        try (Connection connection = daoFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SAVE_USER_QUERY)) {
                setStatementParams(statement, user);
                statement.executeUpdate();
                logger.trace("User with id " + user.getId() + " is saved.");
            }
        } catch (SQLException e) {
            logger.error("Can't save user", e);
            throw new DAOException("Can't save user", e);
        }
    }

    @Override
    public void update(User user) throws DAOException {
        logger.trace("Updating user with id " + user.getId());
        try (Connection connection = daoFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_USER_QUERY)) {
                setStatementParams(statement, user);
                statement.executeUpdate();
                logger.trace("User with id " + user.getId() + " is updated.");
            }
        } catch (SQLException e) {
            logger.error("Can't update user", e);
            throw new DAOException("Can't update user", e);
        }
    }

    @Override
    public void delete(User user) throws DAOException {
        logger.trace("Deleting user with id " + user.getId());
        try (Connection connection = daoFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(DELETE_USER_QUERY)) {
                statement.setInt(1, user.getId());
                statement.executeUpdate();
                logger.trace("User with id " + user.getId() + " is deleted.");
            }
        } catch (SQLException e) {
            logger.error("Can't delete user", e);
            throw new DAOException("Can't delete user", e);
        }
    }

    @Override
    public List<User> findAll() throws DAOException {
        logger.trace("Finding list of users");
        List<User> users = new ArrayList<>();
        try (Connection connection = daoFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_USERS_QUERY)) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        User user = parseUser(rs);
                        users.add(user);
                    }
                    logger.trace("All users were found");
                }
            }
        } catch (SQLException e) {
            logger.error("Can't find users list", e);
            throw new DAOException("Can't find users list", e);
        }
        return users;
    }

    private User parseUser(ResultSet rs) throws DAOException {
        User user = null;
        try {
            int userId = Integer.parseInt(rs.getString("user_id"));
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String username = rs.getString("user_name");
            String languageCode = rs.getString("language_code");
            boolean isBot = rs.getBoolean("is_bot");
            user = new User(userId, firstName, isBot, lastName, username, languageCode);
        } catch (SQLException e) {
            logger.error("Can't find user", e);
            throw new DAOException("Can't find user", e);
        }
        return user;
    }

    private void setStatementParams(PreparedStatement statement, User user) throws DAOException {
        try {
            statement.setInt(1, user.getId());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getUserName());
            statement.setString(5, user.getLanguageCode());
            statement.setBoolean(6, user.getBot());
        } catch (SQLException e) {
            logger.error("Can't set statement params", e);
            throw new DAOException("Can't set statement params", e);
        }
    }
}
