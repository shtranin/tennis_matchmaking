package com.kefx.tennis_matchmaking.services.forCommands;


import com.kefx.tennis_matchmaking.Bot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Service
public class SendMessageService{


    private final Bot bot;

    public SendMessageService(@Lazy Bot bot) {
        this.bot = bot;
    }


    public void sendMessage(Update update,String message) {
        String chatId = Bot.getChatIdFromUpdate(update);
        SendMessage sm = new SendMessage();
        sm.setChatId(chatId);
        sm.setText(message);

        try {
           bot.execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(String chatId,String message) {
        SendMessage sm = new SendMessage();
        sm.setChatId(chatId);
        sm.setText(message);
        try {
            bot.execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
