package com.tweetapp.demo.services;

import com.mongodb.client.result.UpdateResult;
import com.tweetapp.demo.dao.CustomRepo;
import com.tweetapp.demo.dao.TweetRepo;
import com.tweetapp.demo.dtomodels.RlyTweet;
import com.tweetapp.demo.dtomodels.TweetDto;
import com.tweetapp.demo.models.Reply;
import com.tweetapp.demo.models.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class TweetServiceImpl implements TweetService{

    @Autowired
    TweetRepo tweetRepo;

    @Autowired
    CustomRepo customRepo;

    public static final String TOPIC_NAME = "tweetTopic";
    public static final String GROUP_ID = "group_id";

    @Override
    public List<Tweet> getAll() {
        try{
            return tweetRepo.findAll();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public List<Tweet> getTweetOfUser(String username) {
        List<Tweet> tweets = tweetRepo.findAllByTweetBy(username);
        return tweets;
    }

    @Override
    public Tweet addTweetOfUser(String username, TweetDto tweetDto) {
        System.out.println(tweetRepo.count());
        Tweet tweet = new Tweet();
        tweet.setTweetBy(username);
        tweet.setTweetContent(tweetDto.getTweetContent());
        tweet.setHashTag(tweetDto.getHashTag());
        tweet.setPostDate(LocalDate.now());
        tweet.setTweetId(tweetRepo.count()+1);
        tweet.setLike(Long.valueOf(0));
        tweet.setTweetByCount(getCount(username));
        tweet.setReply(null);
        try {
           tweet =  tweetRepo.save(tweet);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return tweet;
    }

    private Long getCount(String username) {
        return Long.valueOf(tweetRepo.findAllByTweetBy(username).size()+1);
    }

    @Override
    public boolean updateTweetOfUser(String username, Long id, TweetDto tweetDto) {
        try {
            UpdateResult result = customRepo.updateTweetCont(id, tweetDto.getTweetContent(), tweetDto.getHashTag());
            return true;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }

   // @Override
   @KafkaListener(topics = TOPIC_NAME, groupId = GROUP_ID)
    public void deleteTweetOfUser(String id) {
        try {
            System.out.println(Long.parseLong(id));
            tweetRepo.deleteById(Long.parseLong(id));
           // return true;
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
       // return false;
    }


    @Override
    public boolean likeTweetOfUser(String username, Long id) {
        Tweet tweet = tweetRepo.findBytweetId(id);
        System.out.println(tweet.getTweetBy()+" "+tweet.getTweetId());
        try {
            UpdateResult result = customRepo.updateTweetLike(tweet.getTweetId(), tweet.getLike() + 1);
        }catch (Exception ex){
           System.out.println(ex.getMessage());
        }
        return true;
    }

    @Override
    public boolean replyTweetOfUser(String username, Long id, RlyTweet rlyCont) {
        Reply rly = new Reply();
        rly.setRly(rlyCont.getRlyCont());
        rly.setBy(rlyCont.getRlyBy());
        rly.setReplyDate(LocalDate.now());
        Tweet tweet = tweetRepo.findBytweetId(id);
        List<Reply> rlyList = new ArrayList<>();
        if(tweet.getReply() == null){
            rlyList.add(rly);
            rly.setId(1);
        }else{
            rlyList = tweet.getReply();
            rlyList.add(rly);
            rly.setId(rlyList.size()+1);
        }
        UpdateResult result = customRepo.updateTweetRly(tweet.getTweetId(),rlyList);
        if(result != null){
            return true;
        }
        return false;
    }

    @Override
    public Tweet getTweetbyId(Long id) {
        return tweetRepo.findBytweetId(id);
    }

    private long getrlyId(String username, Long id) {
        List<Tweet> list= tweetRepo.findAllByTweetBy(username);
        Tweet tweet = (Tweet) list.stream().filter(tweet1 -> tweet1.getTweetId().equals(id));
        return tweet.getReply().size()+1;
    }
}
