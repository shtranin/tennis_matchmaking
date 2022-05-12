package com.kefx.tennis_matchmaking.services.other;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.documents.LastUserMessageDocument;
import com.kefx.tennis_matchmaking.repo.LastMessageRepository;
import com.kefx.tennis_matchmaking.services.withDB.UserDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Transactional
public class DeleteMessageService {
    private final Bot bot;
    private final LastMessageRepository lastMessageRepository;
    @Autowired
    public DeleteMessageService(@Lazy Bot bot, LastMessageRepository lastMessageRepository) {
        this.bot = bot;
        this.lastMessageRepository = lastMessageRepository;

    }

    public void deleteMessage(Update update) {
        DeleteMessage deleteMessage;
        Long userId = Bot.getPlayerIdFromUpdate(update);
        LastUserMessageDocument lastMessage = lastMessageRepository.findByOwnerId(userId);
        if (lastMessage != null) {
            String chatId = lastMessage.getLastChatId();
            int messageId = lastMessage.getLastMessageId();
            deleteMessage = new DeleteMessage(chatId, messageId);
            try {
                bot.execute(deleteMessage);
                lastMessageRepository.delete(lastMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
    public void deleteMessage(Long userId) {
        DeleteMessage deleteMessage;
        LastUserMessageDocument lastMessage = lastMessageRepository.findByOwnerId(userId);
        if (lastMessage != null) {
            String chatId = lastMessage.getLastChatId();
            int messageId = lastMessage.getLastMessageId();
            deleteMessage = new DeleteMessage(chatId, messageId);
            try {
                bot.execute(deleteMessage);
                lastMessageRepository.delete(lastMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean isExistWithOwnerId(Long ownerId) {
    return lastMessageRepository.existsByOwnerId(ownerId);

    }
}
