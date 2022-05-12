package com.kefx.tennis_matchmaking.commands.fromButtons;

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

    @Autowired
    public InfoCommand(DeleteMessageService deleteMessageService, SendMessageService sendMessageService, Redirector redirector) {
        this.deleteMessageService = deleteMessageService;
        this.sendMessageService = sendMessageService;
        this.redirector = redirector;
    }

    @Override
    public void execute(Update update) {
        deleteMessageService.deleteMessage(update);
        sendMessageService.sendMessage(update,"tut infa how it workds");
        redirector.redirectAtCommand("/menu",update);
    }
}
