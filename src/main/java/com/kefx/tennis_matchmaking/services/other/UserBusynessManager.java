package com.kefx.tennis_matchmaking.services.other;

import com.kefx.tennis_matchmaking.documents.Process;
import com.kefx.tennis_matchmaking.documents.UserStatementDocument;
import com.kefx.tennis_matchmaking.repo.UserStatementRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBusynessManager {
    private final UserStatementRepo userStatementRepo;

    @Autowired
    public UserBusynessManager(UserStatementRepo userStatementRepo) {
        this.userStatementRepo = userStatementRepo;
    }


    public void makeUsersBusy(Long firstUserId,Long secondUserId){
        UserStatementDocument firstDocument = new UserStatementDocument(Process.busy.getName(),1,firstUserId);
        UserStatementDocument secondDocument = new UserStatementDocument(Process.busy.getName(),1,secondUserId);

        userStatementRepo.save(firstDocument);
        userStatementRepo.save(secondDocument);
    }

    public void makeUsersFree(Long firstUserId, Long secondUserId){
        userStatementRepo.deleteByOwnerId(firstUserId);
        userStatementRepo.deleteByOwnerId(secondUserId);
    }
}
