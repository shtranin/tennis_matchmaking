package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.commands.base.Redirector;
import com.kefx.tennis_matchmaking.services.forCommands.CreateGamesService;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
import com.kefx.tennis_matchmaking.services.other.UpdatePlayerRatingsService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class AcceptGameResultButton implements Command {
    private final CreateGamesService createGameService;
    private final UpdatePlayerRatingsService updatePlayerRatingsService;
    private final DeleteMessageService deleteMessageService;
    private final SendMessageService sendMessageService;
    private final Redirector redirector;


    public AcceptGameResultButton(CreateGamesService createGameService, UpdatePlayerRatingsService updatePlayerRatingsService, DeleteMessageService deleteMessageService, SendMessageService sendMessageService, Redirector redirector) {
        this.createGameService = createGameService;
        this.updatePlayerRatingsService = updatePlayerRatingsService;
        this.deleteMessageService = deleteMessageService;

        this.sendMessageService = sendMessageService;
        this.redirector = redirector;
    }

    @Override
    public void execute(Update update) {
        deleteMessageService.deleteMessage(update);
        String[] callBackData = update.getCallbackQuery().getData().split(" ");
        Long winnerId = Long.parseLong(callBackData[1]);
        String winnerName =callBackData[2];
        Long loserId = Long.parseLong(callBackData[3]);
        String loserName = callBackData[4];

        int[] accruedRatings = updatePlayerRatingsService.updateRatings(winnerId,loserId);
        createGameService.createGame(winnerId,winnerName,loserId,loserName,accruedRatings);

        sendMessageService.sendMessage(winnerId.toString(),"Вы получили +" + accruedRatings[0] + " рейтинга!");
        sendMessageService.sendMessage(loserId.toString(),"Вы потеряли -" + accruedRatings[1] + " рейтинга");


    }
}
