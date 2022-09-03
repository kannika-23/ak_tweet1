package com.tweetapp.demo.kafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class kafkaConsumer {

    public static final String TOPIC_NAME = "tweetTopic";
    public static final String GROUP_ID = "group_id";

    @KafkaListener(topics = "deleteTopic",groupId = GROUP_ID)
    public void consume(Long id){
        System.out.println(id);
    }
}
