package com.kefx.tennis_matchmaking.commands.fromButtons;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.commands.base.Redirector;
import com.kefx.tennis_matchmaking.documents.Process;
import com.kefx.tennis_matchmaking.documents.UserStatementDocument;
import com.kefx.tennis_matchmaking.repo.UserStatementRepo;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import com.kefx.tennis_matchmaking.services.withDB.UserDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CreateGameButton implements Command {
    private final UserDBService userDBService;
    private final Redirector redirector;
    private final UserStatementRepo userStatementRepo;
    private final SendMessageService sendMessageService;
    @Autowired
    public CreateGameButton(UserDBService userDBService, Redirector redirector, UserStatementRepo userStatementRepo, SendMessageService sendMessageService) {
        this.userDBService = userDBService;
        this.redirector = redirector;
        this.userStatementRepo = userStatementRepo;
        this.sendMessageService = sendMessageService;
    }


    @Override
    public void execute(Update update) {
        Long userId = Bot.getPlayerIdFromUpdate(update);
        if(userDBService.isIdPresentAndNotDeleted(userId)){
            UserStatementDocument statementDocument = new UserStatementDocument(Process.choosingUser.getName(),1, userId);
            userStatementRepo.save(statementDocument);
            redirector.redirectAtCommand("/showTable",update);
        }else{
            sendMessageService.sendMessage(userId,"Пожалуйста зарегистрируйтесь, прежде чем создать рейтинговый матч");
        }

    }

}
