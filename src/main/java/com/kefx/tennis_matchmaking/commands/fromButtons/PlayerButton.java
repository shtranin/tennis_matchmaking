package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.commands.base.Redirector;
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
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class PlayerButton implements Command {
    private final Bot bot;
    private final GameDBService gameDBService;
    private final UserDBService userDBService;
    private final SendMessageService sendMessageService;
    private final DeleteMessageService deleteMessageService;
    private final UpdateLastReceivedMessageService updateLastReceivedMessageService;
    private final Redirector redirector;
    @Autowired
    public PlayerButton(@Lazy Bot bot, GameDBService gameDBService, UserDBService userDBService, SendMessageService sendMessageService, DeleteMessageService deleteMessageService, UpdateLastReceivedMessageService updateLastReceivedMessageService, Redirector redirector) {
        this.bot = bot;
        this.gameDBService = gameDBService;
        this.userDBService = userDBService;
        this.sendMessageService = sendMessageService;
        this.deleteMessageService = deleteMessageService;
        this.updateLastReceivedMessageService = updateLastReceivedMessageService;
        this.redirector = redirector;
    }

    @Override
    public void execute(Update update) {
        Long userId = Bot.getPlayerIdFromUpdate(update);
        Long playerId = Long.parseLong(update.getCallbackQuery().getData().split(" ")[1]);
        UserEntity playerEntity = userDBService.getById(playerId);
        String playerName = playerEntity.getName();

        List<GameEntity> list = gameDBService.getAllGamesById(playerEntity);
        if(list.isEmpty()){
            sendMessageService.sendMessage(userId,"Игрок еще не имел рейтинговых игр");
        }else {

            deleteMessageService.deleteMessage(Bot.getPlayerIdFromUpdate(update));
            List<List<InlineKeyboardButton>> overList = new ArrayList<>();

            for (GameEntity game : list) {
                InlineKeyboardButton playerButton = new InlineKeyboardButton();
                playerButton.setText(game.textResultForUser(playerId));
                playerButton.setCallbackData("/gameDetails " + game.getId());
                List<InlineKeyboardButton> innerList = new ArrayList<>();
                innerList.add(playerButton);
                overList.add(innerList);
            }

            InlineKeyboardButton backButton = new InlineKeyboardButton();
            backButton.setText("Назад к таблице");
            backButton.setCallbackData("/showTable");
            List<InlineKeyboardButton> innerList = new ArrayList<>();
            innerList.add(backButton);
            overList.add(innerList);

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup(overList);
            SendMessage sm = new SendMessage();
            //sm.setText("Статистика игр " + playerName);
            sm.setText("Статистика игр " + playerName + "\n[Противник]    РЕЗУЛЬТАТ    рейтинг");
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
}
