package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
import com.kefx.tennis_matchmaking.services.withDB.UpdateLastReceivedMessageService;
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
public class SettingsButtonCommand implements Command {
    private final Bot bot;
    private final DeleteMessageService deleteMessageService;
    private final UpdateLastReceivedMessageService updateLastReceivedMessageService;
    @Autowired
    public SettingsButtonCommand(@Lazy Bot bot, DeleteMessageService deleteMessageService, UpdateLastReceivedMessageService updateLastReceivedMessageService) {
        this.bot = bot;
        this.deleteMessageService = deleteMessageService;
        this.updateLastReceivedMessageService = updateLastReceivedMessageService;
    }

    @Override
    public void execute(Update update) {
        deleteMessageService.deleteMessage(update);

        List<List<InlineKeyboardButton>> overList = new ArrayList<>();

        InlineKeyboardButton renameButton = new InlineKeyboardButton();
        renameButton.setText("Изменить имя");
        renameButton.setCallbackData("/rename");

        InlineKeyboardButton deleteMyRecordButton = new InlineKeyboardButton();
        deleteMyRecordButton.setText("Удалить игрока");
        deleteMyRecordButton.setCallbackData("/deleteUser");

        InlineKeyboardButton backToMenuButton = new InlineKeyboardButton();
        backToMenuButton.setText("Назад к меню");
        backToMenuButton.setCallbackData("/menu");

        List<InlineKeyboardButton> list1 = new ArrayList<>();
        List<InlineKeyboardButton> list2 = new ArrayList<>();
        list1.add(renameButton);
        list1.add(deleteMyRecordButton);
        list2.add(backToMenuButton);
        overList.add(list1);
        overList.add(list2);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(overList);

        Long userId = Bot.getPlayerIdFromUpdate(update);
        String chatId = Bot.getChatIdFromUpdate(update);
        SendMessage sm = new SendMessage();
        sm.setText("Настройки");
        sm.setChatId(chatId);
        sm.setReplyMarkup(markup);
        try {
            Message message = bot.execute(sm);
            updateLastReceivedMessageService.update(userId,chatId, message.getMessageId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }



    }
}
