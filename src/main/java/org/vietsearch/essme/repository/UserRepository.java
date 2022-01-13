package org.vietsearch.essme.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.vietsearch.essme.model.user.User;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUid(String uid);
}
