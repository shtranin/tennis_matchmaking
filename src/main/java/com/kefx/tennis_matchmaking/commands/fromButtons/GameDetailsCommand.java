package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.entity.GameEntity;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import com.kefx.tennis_matchmaking.services.withDB.GameDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.SimpleDateFormat;

@Component
@Transactional
public class GameDetailsCommand implements Command {
    private final SendMessageService sendMessageService;
    private final GameDBService gameDBService;
    @Autowired
    public GameDetailsCommand(SendMessageService sendMessageService, GameDBService gameDBService) {
        this.sendMessageService = sendMessageService;
        this.gameDBService = gameDBService;
    }

    @Override
    public void execute(Update update) {
        Long userId = Bot.getPlayerIdFromUpdate(update);
        Long gameId = Long.parseLong(update.getCallbackQuery().getData().split(" ")[1]);
        GameEntity game = gameDBService.getById(gameId);

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        String date = formatter.format(game.getDate());
        sendMessageService.sendMessage(userId,"Игра состоялась : " + date);

    }
}
