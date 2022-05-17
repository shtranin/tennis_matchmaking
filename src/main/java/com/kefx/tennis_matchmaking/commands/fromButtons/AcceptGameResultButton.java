package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.Bot;
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

        String[] callBackData = update.getCallbackQuery().getData().split(" ");
        Long winnerId = Long.parseLong(callBackData[1]);
        String winnerName =callBackData[2];
        Long loserId = Long.parseLong(callBackData[3]);
        String loserName = callBackData[4];

        int[] accruedRatings = updatePlayerRatingsService.updateRatings(winnerId,loserId);
        createGameService.createGame(winnerId,winnerName,loserId,loserName,accruedRatings);

        deleteMessageService.deleteMessage(winnerId);
        deleteMessageService.deleteMessage(loserId);

        sendMessageService.sendMessage(winnerId,"Вы получили +" + accruedRatings[0] + " рейтинга после победы над " + loserName + "!");
        sendMessageService.sendMessage(loserId,"Вы потеряли -" + accruedRatings[1] + " рейтинга после поражения " + winnerName + " ");

        deleteMessageService.banClearing(winnerId);
        deleteMessageService.banClearing(loserId);

        redirector.redirectAtCommand("/menu",winnerId);
        redirector.redirectAtCommand("/menu",loserId);


    }
}
