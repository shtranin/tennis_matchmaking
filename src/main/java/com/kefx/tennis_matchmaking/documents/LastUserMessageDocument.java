package com.kefx.tennis_matchmaking.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("last_message")
public class LastUserMessageDocument {
    @Id
    private String id;
    private Long ownerId;
    private String lastChatId;
    private int lastMessageId;

    public LastUserMessageDocument(Long ownerId, String lastChatId, int lastMessageId) {
        this.ownerId = ownerId;
        this.lastChatId = lastChatId;
        this.lastMessageId = lastMessageId;
    }

    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public LastUserMessageDocument() {
    }
    public String getLastChatId() {
        return lastChatId;
    }
    public void setLastChatId(String lastChatId) {
        this.lastChatId = lastChatId;
    }
    public int getLastMessageId() {
        return lastMessageId;
    }
    public void setLastMessageId(int lastMessageId) {
        this.lastMessageId = lastMessageId;
    }
}
