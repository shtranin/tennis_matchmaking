package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.commands.base.Redirector;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
import com.kefx.tennis_matchmaking.services.withDB.UserDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class RenameButton implements Command {
    private final Redirector redirector;
    private final UserDBService userDBService;
    private final DeleteMessageService deleteMessageService;
    private final SendMessageService sendMessageService;
    @Autowired
    public RenameButton(Redirector redirector, UserDBService userDBService, DeleteMessageService deleteMessageService, SendMessageService sendMessageService) {
        this.redirector = redirector;
        this.userDBService = userDBService;
        this.deleteMessageService = deleteMessageService;
        this.sendMessageService = sendMessageService;
    }

    @Override
    public void execute(Update update) {
        Long userId = Bot.getPlayerIdFromUpdate(update);

        if (!userDBService.isIdPresentAndNotDeleted(userId) ) {
            sendMessageService.sendMessage(userId, "Вы еще не были зарегистрированы");
        } else {
            deleteMessageService.deleteMessage(userId);
            redirector.redirectAtCommand("/registration",update);
        }
    }
}
