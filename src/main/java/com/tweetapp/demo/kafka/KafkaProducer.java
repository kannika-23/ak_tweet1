package com.tweetapp.demo.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {

    public static final String TOPIC_NAME = "tweetTopic";
    public static final String GROUP_ID = "group_id";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message){
        log.info(String.format("Message sent -> %s", message));
        kafkaTemplate.send(TOPIC_NAME, message);
    }
}
