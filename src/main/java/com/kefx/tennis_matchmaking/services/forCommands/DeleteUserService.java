package com.kefx.tennis_matchmaking.services.forCommands;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.repo.UserRepository;
import com.kefx.tennis_matchmaking.services.other.DeleteMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class DeleteUserService {
    private final UserRepository userRepository;
    private final DeleteMessageService deleteMessageService;
    private final SendMessageService sendMessageService;

    @Autowired
    public DeleteUserService(UserRepository userRepository, DeleteMessageService deleteMessageService, SendMessageService sendMessageService) {
        this.userRepository = userRepository;
        this.deleteMessageService = deleteMessageService;
        this.sendMessageService = sendMessageService;

    }
    public void deleteUser(Update update){
        Long userId = Bot.getPlayerIdFromUpdate(update);
        userRepository.deleteById(userId);
        sendMessageService.sendMessage(userId,"Вы были удалены");

    }
}
