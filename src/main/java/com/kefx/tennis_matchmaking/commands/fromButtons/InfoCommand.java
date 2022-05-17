package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.commands.base.Redirector;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class InfoCommand implements Command {
    private final DeleteMessageService deleteMessageService;
    private final SendMessageService sendMessageService;
    private final Redirector redirector;
    private final String info = "tut infa how it works";

    @Autowired
    public InfoCommand(DeleteMessageService deleteMessageService, SendMessageService sendMessageService, Redirector redirector) {
        this.deleteMessageService = deleteMessageService;
        this.sendMessageService = sendMessageService;
        this.redirector = redirector;
    }

    @Override
    public void execute(Update update) {
        Long userId = Bot.getPlayerIdFromUpdate(update);
        deleteMessageService.deleteMessage(userId);
        sendMessageService.sendMessage(userId,info);
        deleteMessageService.banClearing(userId);


        redirector.redirectAtCommand("/menu",update);
    }
}
