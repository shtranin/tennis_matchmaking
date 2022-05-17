package com.kefx.tennis_matchmaking.repo;

import com.kefx.tennis_matchmaking.documents.LastUserMessageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LastMessageRepository extends MongoRepository<LastUserMessageDocument, Long> {
    LastUserMessageDocument findByOwnerId(Long ownerId);
    boolean existsByOwnerId(Long ownerId);


}
