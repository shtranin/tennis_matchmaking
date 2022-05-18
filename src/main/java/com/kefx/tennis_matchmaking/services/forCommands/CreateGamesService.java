package com.kefx.tennis_matchmaking.services.forCommands;

import com.kefx.tennis_matchmaking.entity.GameEntity;
import com.kefx.tennis_matchmaking.entity.UserEntity;
import com.kefx.tennis_matchmaking.services.other.UpdatePlayerRatingsService;
import com.kefx.tennis_matchmaking.services.withDB.GameDBService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class CreateGamesService {
    private final GameDBService gameDBService;
    private final UpdatePlayerRatingsService updatePlayerRatingsService;



    @Autowired
    public CreateGamesService(GameDBService gameDBService, UpdatePlayerRatingsService updatePlayerRatingsService) {
        this.gameDBService = gameDBService;
        this.updatePlayerRatingsService = updatePlayerRatingsService;
    }

    public int[] createGame(UserEntity winner,UserEntity loser){
        int[] accruedRatings = updatePlayerRatingsService.updateRatings(winner,loser);
        GameEntity game = new GameEntity(winner,loser,accruedRatings,new Date());
        gameDBService.save(game);
        return  accruedRatings;
    }
}
