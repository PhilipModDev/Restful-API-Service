package com.backend.service.interfaces;

import com.backend.service.core.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User,Integer> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByPassword(String password);
}
