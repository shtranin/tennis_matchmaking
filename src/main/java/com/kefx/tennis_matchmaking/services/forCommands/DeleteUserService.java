package com.kefx.tennis_matchmaking.services.forCommands;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.entity.UserEntity;
import com.kefx.tennis_matchmaking.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Transactional
public class DeleteUserService {
    private final UserRepository userRepository;
    private final SendMessageService sendMessageService;

    @Autowired
    public DeleteUserService(UserRepository userRepository, SendMessageService sendMessageService) {
        this.userRepository = userRepository;
        this.sendMessageService = sendMessageService;

    }
    public void deleteUser(Update update){
        Long userId = Bot.getPlayerIdFromUpdate(update);
        UserEntity user = userRepository.getById(userId);
        user.setDeleted();
        user.setRating(0);
        userRepository.save(user);
        sendMessageService.sendMessage(userId,"Вы были удалены");

    }
}
