package com.tweetapp.demo.controllers;

import com.tweetapp.demo.dtomodels.RlyTweet;
import com.tweetapp.demo.dtomodels.TweetDto;
//import com.tweetapp.demo.kafka.KafkaProducer;
import com.tweetapp.demo.models.Tweet;
import com.tweetapp.demo.services.TweetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@Slf4j
public class TweetController {

//    @Autowired
//    private KafkaProducer kafkaProducer;

    @Autowired
    TweetService tweetService;

    @GetMapping(value = "/tweets/all")
    public List<Tweet> getAllTweets(){
        log.info("In getAll tweets");
        return tweetService.getAll();
    }

    @GetMapping(value="/profile/{username}/posts")
    public List<Tweet> getUsersTweet(@PathVariable String username){
        log.info("In get users tweets");
        return tweetService.getTweetOfUser(username);
    }

    @GetMapping(value ="/profile/{username}")
    public ResponseEntity<Object> getUsersDetails(@PathVariable String username){
        log.info("In get user details");
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("profileUsername",username);
        res.put("counts",tweetService.getTweetOfUser(username).size());
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @GetMapping(value="/tweets/post/{id}")
    public Tweet getTweet(@PathVariable Long id ){
        log.info("In get tweets by Id");
        return tweetService.getTweetbyId(id);
    }

    @PostMapping(value="/tweets/{username}/add")
    public ResponseEntity<Object> postUsersTweet(@PathVariable String username, @RequestBody TweetDto tweetDto){
        log.info("In post user Tweet");
        Tweet tweet = tweetService.addTweetOfUser(username,tweetDto);
        if(tweet==null)
            return new ResponseEntity<>("notCreated",null, HttpStatus.CREATED);
        return new ResponseEntity<>(tweet.getTweetId(),null, HttpStatus.CREATED);
    }

    @PostMapping(value="/tweets/post/{id}/update/{username}")
    public ResponseEntity<Object> updateUsersTweet(@PathVariable String username, @PathVariable Long id, @RequestBody TweetDto tweetDto ){
        log.info("In update user Tweet");
        if(!tweetService.updateTweetOfUser(username,id, tweetDto))
            return ResponseEntity.badRequest().body("Not Updated");
        return new ResponseEntity<>("updated",null, HttpStatus.CREATED);
    }

    @DeleteMapping(value="/tweets/{username}/delete/{id}")
    public ResponseEntity<Object> deleteUsersTweet(@PathVariable String username, @PathVariable Long id){
        log.info("In delete user Tweet");
       /* if(!tweetService.deleteTweetOfUser(username,id))
            return ResponseEntity.badRequest().body("not deleted");//need to impl
        return ResponseEntity.accepted().body("deleted");*/
        try {
            //kafkaProducer.sendMessage(String.valueOf(id));
            tweetService.deleteTweetOfUser(username, id);
            return new ResponseEntity<>("done", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Application has faced an issue",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value="/tweets/{username}/like/{id}")
    public ResponseEntity<Object> likeUsersTweet(@PathVariable String username, @PathVariable Long id){
        log.info("In like user Tweet");
        if(!tweetService.likeTweetOfUser(username,id))
            return ResponseEntity.badRequest().body("not updated");
        return ResponseEntity.ok("liked");
    }

    @PostMapping(value="/tweets/{username}/reply/{id}")
    public ResponseEntity<Object> replyUsersTweet(@PathVariable String username, @PathVariable Long id
            , @RequestBody @Valid RlyTweet rlyCont){
        log.info("In Reply user Tweet");
        if(!tweetService.replyTweetOfUser(username,id, rlyCont)) {
            return ResponseEntity.badRequest().body("not updated");
        }else {
            return ResponseEntity.ok(HttpStatus.CREATED);
        }
    }

}
