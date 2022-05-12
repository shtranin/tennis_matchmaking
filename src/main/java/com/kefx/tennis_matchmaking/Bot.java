package com.kefx.tennis_matchmaking;

import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.commands.base.CommandContainer;
import com.kefx.tennis_matchmaking.documents.UserStatementDocument;
import com.kefx.tennis_matchmaking.logger.Logger;
import com.kefx.tennis_matchmaking.repo.UserStatementRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class Bot extends TelegramLongPollingBot {
    private final Logger logger;
    private final CommandContainer container;
    private final UserStatementRepo userStatementRepo;
    @Autowired
    public Bot(CommandContainer container, Logger logger, UserStatementRepo userStatementRepo) {
        this.container = container;
        this.logger = logger;
        this.userStatementRepo = userStatementRepo;
    }


    @Value("${bot.username}")
    private String userName;

    @Value("${bot.token}")
    private String token;


    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return token;
    }
    public static Long getPlayerIdFromUpdate(Update update){
        long playerId;
        if(update.hasMessage()) {
            playerId = Long.parseLong(update.getMessage().getFrom().getId().toString());
        }else{
            playerId = Long.parseLong(update.getCallbackQuery().getFrom().getId().toString());
        }
        return playerId;
    }
    public static String getChatIdFromUpdate(Update update){
        String chatId;
        if(update.hasMessage()) {
            chatId = update.getMessage().getFrom().getId().toString();
        }else{
            chatId = update.getCallbackQuery().getFrom().getId().toString();
        }
        return chatId;
    }

    @Override
    public void onUpdateReceived(Update update) {
        logger.log(update);
        Command currentCommand;
        Long playerId = getPlayerIdFromUpdate(update);

        UserStatementDocument userStatementDocument = userStatementRepo.findByOwnerId(playerId);
        if(userStatementDocument != null){
            currentCommand = container.receiveCommand(userStatementDocument);
        }else {

            if (update.hasMessage()) {
                currentCommand = container.receiveCommand(update.getMessage().getText());
            } else {
                currentCommand = container.receiveCommand(update.getCallbackQuery().getData().split(" ")[0]);
            }

        }

        currentCommand.execute(update);
    }

}
