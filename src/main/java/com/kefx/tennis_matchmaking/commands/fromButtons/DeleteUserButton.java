package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.commands.base.Redirector;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
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
public class DeleteUserButton implements Command {
    private final UserDBService userDBService;
    private final DeleteMessageService deleteMessageService;
    private final SendMessageService sendMessageService;
    private final Bot bot;
    private final UpdateLastReceivedMessageService updateLastReceivedMessageService;
    private final Redirector redirector;

    @Autowired
    public DeleteUserButton(UserDBService userDBService, @Lazy Bot bot, DeleteMessageService deleteMessageService, SendMessageService sendMessageService, UpdateLastReceivedMessageService updateLastReceivedMessageService, Redirector redirector) {
        this.userDBService = userDBService;
        this.deleteMessageService = deleteMessageService;
        this.bot = bot;
        this.sendMessageService = sendMessageService;
        this.updateLastReceivedMessageService = updateLastReceivedMessageService;
        this.redirector = redirector;
    }

    @Override
    public void execute(Update update) {
        Long userId = Bot.getPlayerIdFromUpdate(update);
        if (!userDBService.isIdPresentAndNotDeleted(userId)){
            sendMessageService.sendMessage(userId, "Вы еще не регистрировались");
        }else{
            deleteMessageService.deleteMessage(Bot.getPlayerIdFromUpdate(update));
            List<List<InlineKeyboardButton>> overList = new ArrayList<>();

            InlineKeyboardButton yesButton = new InlineKeyboardButton();
            yesButton.setText("УДАЛИТЬ");
            yesButton.setCallbackData("/finallyDelete");
            InlineKeyboardButton noButton = new InlineKeyboardButton();
            noButton.setText("Передумал");
            noButton.setCallbackData("/menu");

            List<InlineKeyboardButton> list = new ArrayList<>();
            list.add(yesButton);
            list.add(noButton);
            overList.add(list);

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup(overList);

            String chatId = Bot.getChatIdFromUpdate(update);
            SendMessage sm = new SendMessage();
            sm.setChatId(chatId);
            sm.setText("Вы уверены, что хотите удалить свою запись из списка игроков? Весь рейтинг будет утерян, при повторной регистрации вы начнете c 0");
            sm.setReplyMarkup(markup);
            try {
                Message message = bot.execute(sm);
                updateLastReceivedMessageService.update(userId, chatId, message.getMessageId());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
