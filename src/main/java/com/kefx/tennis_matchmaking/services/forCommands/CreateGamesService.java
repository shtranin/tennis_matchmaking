package com.kefx.tennis_matchmaking.services.forCommands;

import com.kefx.tennis_matchmaking.entity.GameEntity;
import com.kefx.tennis_matchmaking.entity.UserEntity;
import com.kefx.tennis_matchmaking.repo.GameRepository;
import com.kefx.tennis_matchmaking.services.withDB.GameDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class CreateGamesService {
    private final GameDBService gameDBService;

    @Autowired
    public CreateGamesService(GameDBService gameDBService) {
        this.gameDBService = gameDBService;
    }

    public void createGame(Long winnerId, String winnerName, Long loserId, String loserName, int[] accruedRatings){
        GameEntity gameForWinner = new GameEntity(winnerId,winnerName,loserId,loserName,true,accruedRatings[0],new Date());
        GameEntity gameForLoser = new GameEntity(loserId,loserName,winnerId,winnerName,false,accruedRatings[1],new Date());
        gameDBService.save(gameForWinner);
        gameDBService.save(gameForLoser);

    }
}
