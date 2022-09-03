package com.tweetapp.demo.dao;

import com.mongodb.client.result.UpdateResult;
import com.tweetapp.demo.models.Reply;
import com.tweetapp.demo.models.Tweet;
import com.tweetapp.demo.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CustomRepoImpl implements CustomRepo{

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public UpdateResult updateUserPassword(String email, String password) {
        log.info("In update user Password");
            Query query = new Query(Criteria.where("email").is(email));
            Update update = new Update();
            update.set("password", password);
            UpdateResult result = mongoTemplate.updateFirst(query, update, User.class);
            return result;
    }

    @Override
    public UpdateResult updateTweetRly(Long tweetId, List<Reply> rlyList) {
        log.info("In update Tweet Rly");
        Query query = new Query(Criteria.where("tweetId").is(tweetId));
        Update update = new Update();
        update.set("reply", rlyList);
        UpdateResult result = mongoTemplate.updateFirst(query, update, Tweet.class);
        return result;
    }

    @Override
    public UpdateResult updateTweetLike(Long tweetId, long l) {
        log.info("In update tweet Like");
        Query query = new Query(Criteria.where("tweetId").is(tweetId));
        Update update = new Update();
        update.set("like", l);
        UpdateResult result = mongoTemplate.updateFirst(query, update, Tweet.class);
        if(result == null){
            log.info("not updated like");
        }
        return result;
    }

    @Override
    public UpdateResult updateTweetCont(Long tweetId, String tweetCont, String hashTag) {
        log.info("In update tweet content");
        Query query = new Query(Criteria.where("tweetId").is(tweetId));
        Update update = new Update();
        update.set("tweetContent", tweetCont);
        update.set("hashTag", hashTag);
        UpdateResult result = mongoTemplate.updateFirst(query, update, Tweet.class);
        return result;
    }

}
