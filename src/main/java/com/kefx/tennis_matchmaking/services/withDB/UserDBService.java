package com.kefx.tennis_matchmaking.services.withDB;

import com.kefx.tennis_matchmaking.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kefx.tennis_matchmaking.repo.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserDBService {

    private final UserRepository repo;
    @Autowired
    public UserDBService(UserRepository repo) {
        this.repo = repo;
    }

    public void saveUserEntity(UserEntity userEntity){
        repo.save(userEntity);
    }

    public List<UserEntity> getSortedByRatingUsers(){
        return repo.findByOrderByRatingDesc();
    }

    public void deleteRecord(Long id){
        repo.delete(repo.getById(id));
    }

    public boolean isIdPresentAndNotDeleted(Long id){
            Optional<UserEntity> optional = repo.findById(id);
        return optional.isPresent() && !optional.get().isDeleted();

    }

    public UserEntity getById(Long id){
        Optional<UserEntity> optional = repo.findById(id);
        return optional.orElse(null);
    }
}
