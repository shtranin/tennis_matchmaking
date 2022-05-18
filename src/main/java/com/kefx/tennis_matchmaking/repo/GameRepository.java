package com.kefx.tennis_matchmaking.repo;

import com.kefx.tennis_matchmaking.entity.GameEntity;
import com.kefx.tennis_matchmaking.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GameRepository extends JpaRepository<GameEntity,Long> {

    List<GameEntity> findAllByWinnerIsOrLoserIs(UserEntity userEntity,UserEntity userEntity1);
    GameEntity getById(Long gameId);

}
