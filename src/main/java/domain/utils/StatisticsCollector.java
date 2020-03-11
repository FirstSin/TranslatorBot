package domain.utils;

import dao.exceptions.DAOException;
import dao.services.StatisticsService;
import domain.commands.Command;
import domain.commands.CommandType;
import domain.model.Statistics;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class StatisticsCollector {
    private static final Logger logger = Logger.getLogger(StatisticsCollector.class);
    private static Map<String, AtomicLong> commandsCounter;
    private static AtomicLong usersCounter;
    private static AtomicLong wordsCounter;
    private static Runnable updatingThread = new Runnable() {
        private final StatisticsService statService = new StatisticsService();

        @Override
        public void run() {
            while (true) {
                try {
                    Statistics lastStatistics = statService.getStatistics();
                    lastStatistics.setStartCount(lastStatistics.getStartCount() + commandsCounter.get("START").get());
                    lastStatistics.setHelpCount(lastStatistics.getHelpCount() + commandsCounter.get("HELP").get());
                    lastStatistics.setLangInfoCount(lastStatistics.getLangInfoCount() + commandsCounter.get("LANGINFO").get());
                    lastStatistics.setSetMyLangCount(lastStatistics.getSetMyLangCount() + commandsCounter.get("SETMYLANG").get());
                    lastStatistics.setToLangCount(lastStatistics.getToLangCount() + commandsCounter.get("TOLANG").get());
                    lastStatistics.setUsersCount(lastStatistics.getUsersCount() + usersCounter.get());
                    lastStatistics.setTranslatedWordsCount(lastStatistics.getTranslatedWordsCount() + wordsCounter.get());
                    statService.updateStatistics(lastStatistics);
                    resetLocalStatistics();
                    Thread.sleep(30000);
                } catch (DAOException e) {
                    logger.error("Can't update statistics", e);
                } catch (InterruptedException e) {
                    logger.error("Thread was interrupted", e);
                }
            }
        }
    };

    static {
        commandsCounter = new HashMap<>();
        usersCounter = new AtomicLong();
        wordsCounter = new AtomicLong();
        initMap();
        new Thread(updatingThread).start();
    }

    private StatisticsCollector() {
        throw new AssertionError("Cannot create an instance of a class");
    }

    public static void incrementCommandCount(Command command) {
        commandsCounter.get(command.toString()).incrementAndGet();
    }

    public static void incrementUserCount() {
        usersCounter.incrementAndGet();
    }

    public static void addTranslationWordsCount(long delta) {
        wordsCounter.addAndGet(delta);
    }

    private static void initMap() {
        for (CommandType type : CommandType.values()) {
            commandsCounter.put(type.toString(), new AtomicLong(0));
        }
    }

    private static void resetLocalStatistics(){
        usersCounter.getAndSet(0);
        wordsCounter.getAndSet(0);
        commandsCounter.forEach((k, v) -> {
            v.getAndSet(0);
        });
    }
}
