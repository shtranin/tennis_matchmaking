package com.kefx.tennis_matchmaking.services.other;

import com.kefx.tennis_matchmaking.entity.UserEntity;
import com.kefx.tennis_matchmaking.services.withDB.UserDBService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdatePlayerRatingsService {
    private final UserDBService userDBService;

    public UpdatePlayerRatingsService(UserDBService userDBService) {
        this.userDBService = userDBService;
    }
    public int[] updateRatings(Long winnerId, Long loserId){
        UserEntity winner = userDBService.getById(winnerId);
        UserEntity loser = userDBService.getById(loserId);

        int ratingDelta = winner.getRating() - loser.getRating();
        int accruedRating;
        if(ratingDelta <= 0){
            accruedRating =Math.min((int)(Math.abs(ratingDelta) * 0.12) + 30,80);
        }else{
            accruedRating =Math.max(30 - ((int)(Math.abs(ratingDelta) * 0.08)),7);
        }



        int[] ratings = {accruedRating,accruedRating};
        int loserPlus = loser.getRating() - 1500;
        if(loserPlus < accruedRating){
            ratings[1] = loserPlus;
        }

        winner.setRating(winner.getRating() + ratings[0]);
        loser.setRating(loser.getRating() - ratings[1]);
        userDBService.saveUserEntity(winner);
        userDBService.saveUserEntity(loser);
        return ratings;
    }
}
