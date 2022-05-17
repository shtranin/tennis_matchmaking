package com.kefx.tennis_matchmaking.services.forCommands;


import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.documents.UserStatementDocument;
import com.kefx.tennis_matchmaking.documents.Process;
import com.kefx.tennis_matchmaking.repo.UserStatementRepo;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;


@Service
public class RegistrationService {

    private final UserStatementRepo regRepository;
private final SendMessageService sendMessageService;
private final DeleteMessageService deleteMessageService;


    @Autowired
    public RegistrationService(UserStatementRepo regRepository, SendMessageService sendMessageService, DeleteMessageService deleteMessageService) {
        this.regRepository = regRepository;
        this.sendMessageService = sendMessageService;
        this.deleteMessageService = deleteMessageService;
    }

    public void registration(Update update) {
        Long userId = Bot.getPlayerIdFromUpdate(update);
        deleteMessageService.deleteMessage(userId);

        UserStatementDocument statement = new UserStatementDocument(Process.registration.getName(), 1, userId);
        regRepository.save(statement);
        sendMessageService.sendMessage(userId,"Введите пожалуйста имя, под которым вы будете отображаться в таблице");



    }
}
