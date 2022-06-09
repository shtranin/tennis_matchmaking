package com.kefx.tennis_matchmaking.logger;

import com.kefx.tennis_matchmaking.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SendLogsService {
    private final Bot bot;
    @Autowired
    public SendLogsService(@Lazy Bot bot) {
        this.bot = bot;
    }

    protected void sendLogs(Long chatId,String message){
        SendMessage sm = new SendMessage();
        sm.setChatId(chatId.toString());
        sm.setText(message);
        try {
            bot.execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
