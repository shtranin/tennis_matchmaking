package com.kefx.tennis_matchmaking.services.withDB;

import com.kefx.tennis_matchmaking.entity.GameEntity;
import com.kefx.tennis_matchmaking.entity.UserEntity;
import com.kefx.tennis_matchmaking.repo.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameDBService {
    private final GameRepository gameRepository;
    @Autowired
    public GameDBService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void save(GameEntity gameEntity){
        gameRepository.save(gameEntity);
    }
    public List<GameEntity> getAllGamesById(UserEntity userEntity){
        return gameRepository.findAllByWinnerIsOrLoserIs(userEntity,userEntity);
    }
    public GameEntity getById(Long gameId){
       return gameRepository.getById(gameId);
    }
}
