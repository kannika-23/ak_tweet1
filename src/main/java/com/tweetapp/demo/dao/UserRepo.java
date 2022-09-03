package com.tweetapp.demo.dao;

import com.tweetapp.demo.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepo  extends MongoRepository<User, String> {

    @Query("{email:'?0'}")
    User findItemByUserid(String email);

    public long count();

    @Query("{username:'?0'}")
    User findItemByUsername(String username);
}
