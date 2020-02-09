package domain.model;

import domain.commands.Command;
import domain.commands.CommandType;
import domain.utils.CommandFactory;
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
    private static AtomicLong wordsTranslated;

    static {
        commandsCounter = new HashMap<>();
        usersCounter = new AtomicLong();
        wordsTranslated = new AtomicLong();
        initializeMap();
        Thread run = new Thread(() -> {
            while(true){
                try {
                    writeToProps();
                    Thread.sleep(15000);
                } catch (InterruptedException ex) {
                    logger.error("Thread can't sleep");
                }
            }
        });
        run.start();
    }

    public static void commandIncrement(Command command) {
        commandsCounter.get(command.toString()).incrementAndGet();
    }

    public static void userIncrement() {
        usersCounter.incrementAndGet();
    }

    public static void translatedWordIncrement() {
        wordsTranslated.incrementAndGet();
    }

    private static void writeToProps() {
        try (OutputStream out = new FileOutputStream("src/main/resources/statistic.properties")) {
            Properties prop = new Properties();
            commandsCounter.forEach((k, v) -> prop.put(k, String.valueOf(v)));
            prop.put("users", String.valueOf(usersCounter.get()));
            prop.put("translatedWords", String.valueOf(wordsTranslated.get()));
            prop.store(out, null);
        } catch (IOException e) {
            logger.error("An error occurred while writing data to properties file", e);
        }
    }

    private static void initializeMap(){
        CommandFactory factory = new CommandFactory();
        for (Command command: factory.getAllCommands()) {
            commandsCounter.put(command.toString(), new AtomicLong(0));
        }
    }
}
