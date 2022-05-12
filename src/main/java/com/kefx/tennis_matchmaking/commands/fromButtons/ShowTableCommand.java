package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.services.forCommands.ShowTableService;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ShowTableCommand implements Command {
    private final DeleteMessageService deleteMessageService;
    private final ShowTableService showTableService;

    @Autowired
    public ShowTableCommand(DeleteMessageService deleteMessageService, ShowTableService showTableService) {
        this.deleteMessageService = deleteMessageService;
        this.showTableService = showTableService;
    }

    @Override
    public void execute(Update update) {
        deleteMessageService.deleteMessage(update);
        showTableService.showTable(update);

    }
}
