package com.kefx.tennis_matchmaking.commands.phaseCommand;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.commands.base.Redirector;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UserIsBusy implements Command {
    private final SendMessageService sendMessageService;
    private final Redirector redirector;
    @Autowired
    public UserIsBusy(SendMessageService sendMessageService, Redirector redirector) {
        this.sendMessageService = sendMessageService;
        this.redirector = redirector;
    }

    @Override
    public void execute(Update update) {
        Long userId = Bot.getPlayerIdFromUpdate(update);
        String data;
        if (update.hasMessage()) {
            data = update.getMessage().getText().split(" ")[0];
        } else {
            data = update.getCallbackQuery().getData().split(" ")[0];
        }

        if(data.equals("accept")){
            redirector.redirectAtCommand("accept", update);
        }else if(data.equals("cancel")){
            redirector.redirectAtCommand("cancel",update);
        }else{
            sendMessageService.sendMessage(userId,"Вам необходимо прежде завершить предыдущий матч");
        }



    }
}
