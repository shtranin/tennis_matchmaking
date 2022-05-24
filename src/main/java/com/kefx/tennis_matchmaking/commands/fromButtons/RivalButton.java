package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.entity.UserEntity;
import com.kefx.tennis_matchmaking.repo.UserStatementRepo;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
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
public class RivalButton implements Command {
    private final Bot bot;
    private final DeleteMessageService deleteMessageService;
    private final SendMessageService sendMessageService;
    private final UserDBService userDBService;
    private final UpdateLastReceivedMessageService updateLastReceivedMessageService;
    private final UserStatementRepo userStatementRepo;
    @Autowired
    public RivalButton(@Lazy Bot bot, DeleteMessageService deleteMessageService, SendMessageService sendMessageService, UserDBService userDBService, UpdateLastReceivedMessageService updateLastReceivedMessageService, UserStatementRepo userStatementRepo) {
        this.bot = bot;
        this.deleteMessageService = deleteMessageService;
        this.sendMessageService = sendMessageService;
        this.userDBService = userDBService;
        this.updateLastReceivedMessageService = updateLastReceivedMessageService;
        this.userStatementRepo = userStatementRepo;
    }

    @Override
    public void execute(Update update) {

        Long userId = Bot.getPlayerIdFromUpdate(update);
        String chatId = Bot.getChatIdFromUpdate(update);

        Long rivalId = Long.parseLong(update.getCallbackQuery().getData().split(" ")[1]);
        UserEntity rival = userDBService.getById(rivalId);
        String rivalName = rival.getName();
        if(userStatementRepo.findByOwnerId(rivalId) != null){
            sendMessageService.sendMessage(userId,rivalName + " не может начать новую рейтинговую игру, пока не завершил прошлую");
            return;
        }

        deleteMessageService.deleteMessage(userId);
        List<List<InlineKeyboardButton>> overList = new ArrayList<>();
        List<InlineKeyboardButton> listWithPlayers = new ArrayList<>();
        List<InlineKeyboardButton> listWithCancelButton = new ArrayList<>();

        UserEntity player = userDBService.getById(userId);
        String playerName = player.getName();
        String playerId = player.getId().toString();



        InlineKeyboardButton firstPlayedButton = new InlineKeyboardButton();
        firstPlayedButton.setText(playerName);
        firstPlayedButton.setCallbackData("winner " + playerId + " " + playerName + " " + rivalId + " " + rivalName);

        InlineKeyboardButton secondPlayerButton = new InlineKeyboardButton();
        secondPlayerButton.setText(rivalName);
        secondPlayerButton.setCallbackData("winner " + rivalId + " " + rivalName + " " + playerId + " " + playerName);

        InlineKeyboardButton cancelButton = new InlineKeyboardButton();
        cancelButton.setText("Отменить матч");
        cancelButton.setCallbackData("/menu");

        listWithPlayers.add(firstPlayedButton);
        listWithPlayers.add(secondPlayerButton);
        listWithCancelButton.add(cancelButton);
        overList.add(listWithPlayers);
        overList.add(listWithCancelButton);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(overList);

        SendMessage sm = new SendMessage();
        sm.setText("Укажите победителя матча");
        sm.setChatId(chatId);
        sm.setReplyMarkup(markup);

        try {
            Message becameMessage =bot.execute(sm);
            updateLastReceivedMessageService.update(userId,chatId, becameMessage.getMessageId());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }
}
