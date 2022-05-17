package com.kefx.tennis_matchmaking.commands.specific_commands;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UnknownCommand implements Command {
    private final SendMessageService service;
    private String command = "Бот не предусматривает отправку ему сообщений :) воспользуйтесь пожалуйста /menu)";
    @Autowired
    public UnknownCommand(SendMessageService service) {
        this.service = service;
    }


    @Override
    public void execute(Update update) {
        service.sendMessage(Bot.getPlayerIdFromUpdate(update),command);
    }
}
