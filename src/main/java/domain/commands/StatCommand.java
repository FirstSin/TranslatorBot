package domain.commands;

import dao.exceptions.DAOException;
import dao.services.StatisticsService;
import domain.model.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

public class StatCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(StatCommand.class);
    private static CommandType type = CommandType.STAT;
    private StatisticsService statService = new StatisticsService();

    @Override
    public void execute(User user, String argument, SendMessage response) throws DAOException {
        Statistics statistics = statService.getStatistics();
        StringBuilder sb = new StringBuilder();
        sb.append("<b>Статистика вызова команд:</b>\n");
        sb.append("start: ").append(statistics.getStartCount());
        sb.append("\nhelp: ").append(statistics.getHelpCount());
        sb.append("\nlanginfo: ").append(statistics.getLangInfoCount());
        sb.append("\nsetmylang: ").append(statistics.getSetMyLangCount());
        sb.append("\ntolang: ").append(statistics.getToLangCount());
        sb.append("\n\n<b>Общая статистика:</b>\n");
        sb.append("Пользователей бота: ").append(statistics.getUsersCount());
        sb.append("\nПереведено слов: ").append(statistics.getTranslatedWordsCount());
        response.setText(sb.toString());
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
