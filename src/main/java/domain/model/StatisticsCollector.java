package domain.model;

import domain.commands.Command;
import domain.commands.CommandType;
import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

public class StatisticsCollector {
    private static final Logger logger = Logger.getLogger(StatisticsCollector.class);
    private static Map<String, AtomicLong> commandsCounter;
    private static AtomicLong usersCounter;
    private static AtomicLong wordsCounter;

    static {
        commandsCounter = new HashMap<>();
        usersCounter = new AtomicLong();
        wordsCounter = new AtomicLong();
        initializeMap();
        Thread run = new Thread(() -> {
            while (true) {
                try {
                    writeToProps();
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    logger.error("Thread can't sleep", e);
                }
            }
        });
        run.start();
    }

    private StatisticsCollector() {
        throw new AssertionError("Cannot create an instance of a class");
    }

    public static void commandIncrement(Command command) {
        commandsCounter.get(command.toString()).incrementAndGet();
    }

    public static void userIncrement() {
        usersCounter.incrementAndGet();
    }

    public static void translatedWordIncrement() {
        wordsCounter.incrementAndGet();
    }

    private static void writeToProps() {
        try (OutputStream out = new FileOutputStream("src/main/resources/statistic.properties")) {
            Properties prop = new Properties();
            commandsCounter.forEach((k, v) -> prop.put(k, String.valueOf(v)));
            prop.put("users", String.valueOf(usersCounter.get()));
            prop.put("translatedWords", String.valueOf(wordsCounter.get()));
            prop.store(out, null);
        } catch (IOException e) {
            logger.error("An error occurred while writing data to properties file", e);
        }
    }

    private static void initializeMap() {
        for (CommandType type : CommandType.values()) {
            commandsCounter.put(type.getCommandName(), new AtomicLong(0));
        }
    }
}
