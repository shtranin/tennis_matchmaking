package com.kefx.tennis_matchmaking.repo;

import com.kefx.tennis_matchmaking.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GameRepository extends JpaRepository<GameEntity,Long> {
    List<GameEntity> findAllByOwnerId(Long ownerId);

}
