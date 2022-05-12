package com.kefx.tennis_matchmaking.services.withDB;

import com.kefx.tennis_matchmaking.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kefx.tennis_matchmaking.repo.UserRepository;

import java.util.List;

@Service
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

    public boolean isIdPresent(Long id){

        return repo.findById(id).isPresent();
    }

    public UserEntity getById(Long id){
        return repo.getById(id);
    }
}
