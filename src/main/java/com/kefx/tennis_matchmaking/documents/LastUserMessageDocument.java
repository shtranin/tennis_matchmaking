package com.kefx.tennis_matchmaking.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("last_message")
public class LastUserMessageDocument {
    @Id
    private String id;
    private Long ownerId;
    private String lastChatId;
    private List<Integer> lastMessageId;
    private boolean isClearingBanned;
    public LastUserMessageDocument() {
    }
    public LastUserMessageDocument(Long ownerId, String lastChatId, List<Integer> lastMessageId) {
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

    public String getLastChatId() {
        return lastChatId;
    }
    public void setLastChatId(String lastChatId) {
        this.lastChatId = lastChatId;
    }

    public List<Integer> getLastMessagesId() {
        return lastMessageId;
    }
    public void setLastMessageId(List<Integer> lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public boolean isClearingBanned(){
        return isClearingBanned;
    }
    public void setIsClearingBanned(boolean result){
        isClearingBanned = result;
    }
    public void addLastMessageId(int messageId){
        if(lastMessageId == null){
            lastMessageId = new ArrayList<>();
        }
        lastMessageId.add(messageId);
    }

}
