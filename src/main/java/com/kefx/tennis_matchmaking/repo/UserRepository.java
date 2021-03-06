package com.kefx.tennis_matchmaking.repo;

import com.kefx.tennis_matchmaking.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findByOrderByRatingDesc();
    Optional<UserEntity> findById(Long userId);
}
