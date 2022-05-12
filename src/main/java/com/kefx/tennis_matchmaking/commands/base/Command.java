package com.kefx.tennis_matchmaking.commands.base;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {

    void execute(Update update);
}
