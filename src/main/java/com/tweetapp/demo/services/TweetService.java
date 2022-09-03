package com.tweetapp.demo.services;

import com.tweetapp.demo.dtomodels.RlyTweet;
import com.tweetapp.demo.dtomodels.TweetDto;
import com.tweetapp.demo.models.Tweet;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface TweetService {

    List<Tweet> getAll();

    List<Tweet> getTweetOfUser(String userName);

    Tweet addTweetOfUser(String userName, TweetDto tweetDto);

    boolean updateTweetOfUser(String userName, Long id, TweetDto tweetDto);

    void deleteTweetOfUser(String userName, Long id);

    boolean likeTweetOfUser(String userName, Long id);

    boolean replyTweetOfUser(String userName, Long id, RlyTweet rlyCont);

    public Tweet getTweetbyId(Long id);
}
