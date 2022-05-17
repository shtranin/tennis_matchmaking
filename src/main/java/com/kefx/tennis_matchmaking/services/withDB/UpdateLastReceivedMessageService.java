package com.kefx.tennis_matchmaking.services.withDB;

import com.kefx.tennis_matchmaking.documents.LastUserMessageDocument;
import com.kefx.tennis_matchmaking.repo.LastMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateLastReceivedMessageService {

    private final LastMessageRepository lastMessageRepository;

    @Autowired
    public UpdateLastReceivedMessageService(LastMessageRepository lastMessageRepository) {
        this.lastMessageRepository = lastMessageRepository;
    }

    public void update(Long userId, String chatId, int messageId) {
        LastUserMessageDocument lastMessage = lastMessageRepository.findByOwnerId(userId);
        if (lastMessage == null) {
            lastMessage = new LastUserMessageDocument();
            lastMessage.setOwnerId(userId);
            lastMessage.setLastChatId(chatId);
        }

        lastMessage.addLastMessageId(messageId);

        lastMessageRepository.save(lastMessage);
    }
}
