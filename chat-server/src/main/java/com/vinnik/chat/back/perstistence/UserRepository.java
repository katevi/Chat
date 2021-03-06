package com.vinnik.chat.back.perstistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "users", path = "user")
public interface UserRepository extends MongoRepository<User, String> {

    User findByUserName(@Param("userName") String name);
    User findByFullName(@Param("fullName") String fullName);
    User findByUserId(@Param("id") String id);

}
