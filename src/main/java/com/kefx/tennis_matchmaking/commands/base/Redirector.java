package com.kefx.tennis_matchmaking.commands.base;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.specific_commands.MenuCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component
public class Redirector {
    private final CommandContainer container;

    @Autowired
    public Redirector(@Lazy CommandContainer container) {
        this.container = container;
    }

    public void redirectAtCommand(String commandName, Update update) {
        Command command = container.receiveCommand(commandName);
        command.execute(update);

    }

    public void redirectAtCommand(String commandName, Long redirectedUserId) {
        Command command = container.receiveCommand(commandName);

        switch(commandName){
            case "/menu":
                MenuCommand menuCommand = (MenuCommand) command;
                menuCommand.realExecute(redirectedUserId);
        }

    }
}
