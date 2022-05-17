package com.kefx.tennis_matchmaking.services.other;

import com.kefx.tennis_matchmaking.Bot;
import com.kefx.tennis_matchmaking.documents.LastUserMessageDocument;
import com.kefx.tennis_matchmaking.repo.LastMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
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


    public void deleteMessage(Long userId) {
        DeleteMessage deleteMessage;
        LastUserMessageDocument lastMessages = lastMessageRepository.findByOwnerId(userId);
        if (lastMessages != null) {
            String chatId = lastMessages.getLastChatId();
            for (int messageId : lastMessages.getLastMessagesId()){
                deleteMessage = new DeleteMessage(chatId, messageId);
                try {
                    bot.execute(deleteMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            lastMessageRepository.delete(lastMessages);
        }
    }
    public boolean isExistWithOwnerId(Long ownerId) {
    return lastMessageRepository.existsByOwnerId(ownerId);
    }

    public boolean isClearingBanned(Long ownerId){
        boolean isClearBanned;
        LastUserMessageDocument lastUserMessageDocument = lastMessageRepository.findByOwnerId(ownerId);

            isClearBanned = lastUserMessageDocument.isClearingBanned();
            lastUserMessageDocument.setIsClearingBanned(false);

    return isClearBanned;
    }
    public void banClearing(Long ownerId){
        LastUserMessageDocument lastUserMessageDocument = lastMessageRepository.findByOwnerId(ownerId);
        lastUserMessageDocument.setIsClearingBanned(true);
        lastMessageRepository.save(lastUserMessageDocument);
    }
}
