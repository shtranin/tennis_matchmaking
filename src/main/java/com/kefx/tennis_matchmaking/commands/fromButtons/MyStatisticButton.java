package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.entity.GameEntity;
import com.kefx.tennis_matchmaking.entity.UserEntity;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
import com.kefx.tennis_matchmaking.services.withDB.GameDBService;
import com.kefx.tennis_matchmaking.services.withDB.UpdateLastReceivedMessageService;
import com.kefx.tennis_matchmaking.services.withDB.UserDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyStatisticButton implements Command {
    private final Bot bot;
    private final UserDBService userDBService;
    private final GameDBService gameDBService;
    private final SendMessageService sendMessageService;
    private final DeleteMessageService deleteMessageService;
    private final UpdateLastReceivedMessageService updateLastReceivedMessageService;
    @Autowired
    public MyStatisticButton(@Lazy Bot bot, UserDBService userDBService, GameDBService gameDBService, SendMessageService sendMessageService, DeleteMessageService deleteMessageService, UpdateLastReceivedMessageService updateLastReceivedMessageService) {
        this.bot = bot;
        this.userDBService = userDBService;
        this.gameDBService = gameDBService;
        this.sendMessageService = sendMessageService;
        this.deleteMessageService = deleteMessageService;
        this.updateLastReceivedMessageService = updateLastReceivedMessageService;
    }

    @Override
    public void execute(Update update) {
        Long userId = Bot.getPlayerIdFromUpdate(update);
        UserEntity currentUser = getUserEntityFromUpdate(update);
        if(currentUser == null){
            sendMessageService.sendMessage(userId,"Вы еще не имели рейтинговых игр");
            return;
        }

        List<GameEntity> list = gameDBService.getAllGamesById(currentUser);
        if(list.isEmpty()){
            sendMessageService.sendMessage(userId,"Вы еще не имели рейтинговых игр");
        }else {

            deleteMessageService.deleteMessage(userId);
            List<List<InlineKeyboardButton>> overList = new ArrayList<>();

            for (GameEntity game : list) {
                InlineKeyboardButton gameButton = new InlineKeyboardButton();
                gameButton.setText(game.textResultForUser(userId));
                gameButton.setCallbackData("/gameDetails " + game.getId());
                List<InlineKeyboardButton> innerList = new ArrayList<>();
                innerList.add(gameButton);
                overList.add(innerList);
            }

            InlineKeyboardButton backButton = new InlineKeyboardButton();
            backButton.setText("Назад к меню");
            backButton.setCallbackData("/menu");
            List<InlineKeyboardButton> innerList = new ArrayList<>();
            innerList.add(backButton);
            overList.add(innerList);

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup(overList);
            SendMessage sm = new SendMessage();
           // sm.setText("Ваша статистика игр");
            sm.setText("Статистика Ваших игр\n[Противник]    РЕЗУЛЬТАТ    рейтинг");
            sm.setChatId(Bot.getChatIdFromUpdate(update));
            sm.setReplyMarkup(markup);

            try {
                Message becameMessage = bot.execute(sm);
                updateLastReceivedMessageService.update(userId,Bot.getChatIdFromUpdate(update), becameMessage.getMessageId());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }
    }

    private UserEntity getUserEntityFromUpdate(Update update){
        return userDBService.getById(Bot.getPlayerIdFromUpdate(update));
    }
}