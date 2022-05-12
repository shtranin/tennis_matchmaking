package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.commands.base.Redirector;
import com.kefx.tennis_matchmaking.documents.Process;
import com.kefx.tennis_matchmaking.documents.UserStatementDocument;
import com.kefx.tennis_matchmaking.repo.UserStatementRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CreateGameButton implements Command {

    private final Redirector redirector;
    private final UserStatementRepo userStatementRepo;
    @Autowired
    public CreateGameButton(Redirector redirector, UserStatementRepo userStatementRepo) {
        this.redirector = redirector;
        this.userStatementRepo = userStatementRepo;
    }


    @Override
    public void execute(Update update) {
        UserStatementDocument statementDocument = new UserStatementDocument(Process.choosingUser.getName(),1, Bot.getPlayerIdFromUpdate(update));
        userStatementRepo.save(statementDocument);
        redirector.redirectAtCommand("/showTable",update);
    }

}
