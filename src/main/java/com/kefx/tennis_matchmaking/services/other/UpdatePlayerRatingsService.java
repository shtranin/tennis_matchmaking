package com.kefx.tennis_matchmaking.services.other;

import com.kefx.tennis_matchmaking.entity.UserEntity;
import com.kefx.tennis_matchmaking.services.withDB.UserDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdatePlayerRatingsService {
    private final UserDBService userDBService;

    private final int MAX_GETTING_RATING = 80;
    private final int MIN_GETTING_RATING = 7;
    private final int BASIC_RATING = 30;

    @Autowired
    public UpdatePlayerRatingsService(UserDBService userDBService) {
        this.userDBService = userDBService;
    }
    public int[] updateRatings(UserEntity winner, UserEntity loser){

        int ratingDelta = winner.getRating() - loser.getRating();
        int accruedRating;

        if(ratingDelta <= 0){
            accruedRating =Math.min((int)(Math.abs(ratingDelta) * 0.12) + BASIC_RATING,MAX_GETTING_RATING);
        }else{
            accruedRating =Math.max(BASIC_RATING - ((int)(Math.abs(ratingDelta) * 0.08)),MIN_GETTING_RATING);
        }


        int[] ratings = {accruedRating,accruedRating};
        if(loser.getRating() < accruedRating){
            ratings[1] = loser.getRating();
        }

        winner.setRating(winner.getRating() + ratings[0]);
        loser.setRating(loser.getRating() - ratings[1]);
        userDBService.saveUserEntity(winner);
        userDBService.saveUserEntity(loser);
        return ratings;
    }
}
