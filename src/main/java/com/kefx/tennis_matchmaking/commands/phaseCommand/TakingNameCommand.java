package com.kefx.tennis_matchmaking.commands.phaseCommand;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.commands.base.Command;
import com.kefx.tennis_matchmaking.commands.base.Redirector;
import com.kefx.tennis_matchmaking.entity.UserEntity;
import com.kefx.tennis_matchmaking.repo.UserRepository;
import com.kefx.tennis_matchmaking.repo.UserStatementRepo;
import com.kefx.tennis_matchmaking.services.forCommands.SendMessageService;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component

public class TakingNameCommand implements Command {
    private final SendMessageService sendMessageService;
    private final DeleteMessageService deleteMessageService;
    private final UserRepository userRepository;
    private final UserStatementRepo userStatementRepo;
    private final Redirector redirector;
    @Autowired
    public TakingNameCommand(SendMessageService sendMessageService, DeleteMessageService deleteMessageService, UserRepository userRepository, UserStatementRepo userStatementRepo, Redirector redirector) {
        this.sendMessageService = sendMessageService;
        this.deleteMessageService = deleteMessageService;
        this.userRepository = userRepository;
        this.userStatementRepo = userStatementRepo;
        this.redirector = redirector;
    }

    @Override
    public void execute(Update update) {
        String proposedName = update.getMessage().getText().trim();

        UserEntity userEntity = UserEntity.getNewEntityFromUpdate(update);
        userEntity.setName(proposedName);

        userRepository.save(userEntity);
        userStatementRepo.deleteByOwnerId(userEntity.getId());

        sendMessageService.sendMessage(Bot.getPlayerIdFromUpdate(update),"Ваше имя записано");
        deleteMessageService.banClearing(Bot.getPlayerIdFromUpdate(update));
        redirector.redirectAtCommand("/menu",update);

    }
}