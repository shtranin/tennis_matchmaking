package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.commands.base.Redirector;
import com.kefx.tennis_matchmaking.services.forCommands.DeleteUserService;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component
public class FinallyDeleteUser implements Command {
   private final DeleteMessageService deleteMessageService;
   private final DeleteUserService deleteUserService;
   private final SendMessageService sendMessageService;
   private final Redirector redirector;
    @Autowired
    public FinallyDeleteUser(DeleteMessageService deleteMessageService, DeleteUserService deleteUserService, SendMessageService sendMessageService, Redirector redirector) {
        this.deleteMessageService = deleteMessageService;
        this.deleteUserService = deleteUserService;
        this.sendMessageService = sendMessageService;
        this.redirector = redirector;
    }

    @Override
    public void execute(Update update) {
        deleteMessageService.deleteMessage(Bot.getPlayerIdFromUpdate(update));
        deleteUserService.deleteUser(update);

        redirector.redirectAtCommand("/menu",update);

    }
}
