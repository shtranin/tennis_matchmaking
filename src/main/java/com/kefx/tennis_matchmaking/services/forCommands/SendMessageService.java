package com.kefx.tennis_matchmaking.services.forCommands;


import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.services.withDB.UpdateLastReceivedMessageService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Service
public class SendMessageService{

    private final Bot bot;
    private final UpdateLastReceivedMessageService updateLastReceivedMessageService;

    public SendMessageService(@Lazy Bot bot, UpdateLastReceivedMessageService updateLastReceivedMessageService) {
        this.bot = bot;
        this.updateLastReceivedMessageService = updateLastReceivedMessageService;
    }



    public void sendMessage(Long userId,String message) {
        SendMessage sm = new SendMessage();
        sm.setChatId(userId.toString());
        sm.setText(message);
        try {
            Message becameMessage =  bot.execute(sm);
            updateLastReceivedMessageService.update(userId,userId.toString(),becameMessage.getMessageId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
