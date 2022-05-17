package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.commands.base.Redirector;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CancelGameResultButton implements Command {
    private final DeleteMessageService deleteMessageService;
    private final SendMessageService sendMessageService;
    private final Redirector redirector;

    public CancelGameResultButton(DeleteMessageService deleteMessageService, SendMessageService sendMessageService, Redirector redirector) {
        this.deleteMessageService = deleteMessageService;
        this.sendMessageService = sendMessageService;
        this.redirector = redirector;
    }

    @Override
    public void execute(Update update) {
        String[] callBackData = update.getCallbackQuery().getData().split(" ");
        Long winnerId = Long.parseLong(callBackData[1]);
        String winnerName = callBackData[2];
        Long loserId = Long.parseLong(callBackData[3]);
        String loserName = callBackData[4];
        deleteMessageService.deleteMessage(winnerId);
        deleteMessageService.deleteMessage(loserId);

        sendMessageService.sendMessage(loserId,"Вы не подтвердили поражение " + winnerName);
        sendMessageService.sendMessage(winnerId,loserName + " не подтвердил поражение Вам");

        deleteMessageService.banClearing(winnerId);
        deleteMessageService.banClearing(loserId);

        redirector.redirectAtCommand("/menu",winnerId);
        redirector.redirectAtCommand("/menu",loserId);
    }
}
