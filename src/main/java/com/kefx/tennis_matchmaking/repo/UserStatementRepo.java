package com.kefx.tennis_matchmaking.repo;

import com.kefx.tennis_matchmaking.documents.UserStatementDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

 public interface UserStatementRepo extends MongoRepository<UserStatementDocument,String> {
     void deleteByOwnerId(Long ownerId);
     UserStatementDocument findByOwnerId(Long ownerId);
}
