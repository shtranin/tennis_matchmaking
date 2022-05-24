package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.commands.base.Redirector;
import com.kefx.tennis_matchmaking.entity.UserEntity;
import com.kefx.tennis_matchmaking.services.forCommands.CreateGamesService;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
import com.kefx.tennis_matchmaking.services.other.UserBusynessManager;
import com.kefx.tennis_matchmaking.services.withDB.UserDBService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Transactional
public class AcceptGameResultButton implements Command {
    private final UserBusynessManager userBusynessManager;
    private final CreateGamesService createGameService;
    private final UserDBService userDBService;
    private final DeleteMessageService deleteMessageService;
    private final SendMessageService sendMessageService;
    private final Redirector redirector;


    public AcceptGameResultButton(UserBusynessManager userBusynessManager, CreateGamesService createGameService, UserDBService userDBService, DeleteMessageService deleteMessageService, SendMessageService sendMessageService, Redirector redirector) {
        this.userBusynessManager = userBusynessManager;
        this.createGameService = createGameService;
        this.userDBService = userDBService;

        this.deleteMessageService = deleteMessageService;

        this.sendMessageService = sendMessageService;
        this.redirector = redirector;
    }

    @Override
    public void execute(Update update) {

        String[] callBackData = update.getCallbackQuery().getData().split(" ");
        Long winnerId = Long.parseLong(callBackData[1]);
        Long loserId = Long.parseLong(callBackData[2]);


        UserEntity winner = userDBService.getById(winnerId);
        UserEntity loser = userDBService.getById(loserId);
        int[] accruedRatings = createGameService.createGame(winner, loser);

        userBusynessManager.makeUsersFree(winnerId,loserId);

        deleteMessageService.deleteMessage(winnerId);
        deleteMessageService.deleteMessage(loserId);

        sendMessageService.sendMessage(winnerId,"Вы получили +" + accruedRatings[0] + " рейтинга после победы над " + loser.getName() + "!");
        sendMessageService.sendMessage(loserId,"Вы потеряли -" + accruedRatings[1] + " рейтинга после поражения " + winner.getName() + " ");

        deleteMessageService.banClearing(winnerId);
        deleteMessageService.banClearing(loserId);

        redirector.redirectAtCommand("/menu",winnerId);
        redirector.redirectAtCommand("/menu",loserId);


    }
}
