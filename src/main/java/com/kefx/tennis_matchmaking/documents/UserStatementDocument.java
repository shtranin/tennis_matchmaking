package com.kefx.tennis_matchmaking.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("statement")
public class UserStatementDocument {
    @Id
    private String id;
    private String inProcessOf;
    private int phaseOfProcess;
    private Long ownerId;

    public UserStatementDocument(String inProcessOf, int phaseOfProcess, Long ownerId) {
        this.inProcessOf = inProcessOf;
        this.phaseOfProcess = phaseOfProcess;
        this.ownerId = ownerId;
    }

    public String getInProcessOf() {
        return inProcessOf;
    }

    public void setInProcessOf(String inProcessOf) {
        this.inProcessOf = inProcessOf;
    }

    public int getPhaseOfProcess() {
        return phaseOfProcess;
    }

    public void setPhaseOfProcess(int phaseOfProcess) {
        this.phaseOfProcess = phaseOfProcess;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
