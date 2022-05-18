package com.kefx.tennis_matchmaking.commands.phaseCommand;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.commands.base.Redirector;
import com.kefx.tennis_matchmaking.entity.UserEntity;
import com.kefx.tennis_matchmaking.repo.UserRepository;
import com.kefx.tennis_matchmaking.repo.UserStatementRepo;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
import com.kefx.tennis_matchmaking.services.withDB.UserDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component
@Transactional
public class TakingNameCommand implements Command {
    private final SendMessageService sendMessageService;
    private final UserDBService userDBService;
    private final DeleteMessageService deleteMessageService;
    private final UserStatementRepo userStatementRepo;
    private final Redirector redirector;
    @Autowired
    public TakingNameCommand(SendMessageService sendMessageService, UserDBService userDBService, DeleteMessageService deleteMessageService, UserRepository userRepository, UserStatementRepo userStatementRepo, Redirector redirector) {
        this.sendMessageService = sendMessageService;
        this.userDBService = userDBService;
        this.deleteMessageService = deleteMessageService;
        this.userStatementRepo = userStatementRepo;
        this.redirector = redirector;
    }

    @Override
    public void execute(Update update) {
        String proposedName = update.getMessage().getText().trim();
        Long userId = Bot.getPlayerIdFromUpdate(update);
        if(proposedName.length() > 13){
            sendMessageService.sendMessage(userId,"Имя не может быть длиннее 13 символов. Введите пожалуйста имя короче");
            return;
        }

        UserEntity userEntity = userDBService.getById(userId);
        userEntity.setName(proposedName);

        userDBService.saveUserEntity(userEntity);
        userStatementRepo.deleteByOwnerId(userEntity.getId());

        sendMessageService.sendMessage(Bot.getPlayerIdFromUpdate(update),"Ваше имя записано");
        deleteMessageService.banClearing(Bot.getPlayerIdFromUpdate(update));
        redirector.redirectAtCommand("/menu",update);

    }
}
