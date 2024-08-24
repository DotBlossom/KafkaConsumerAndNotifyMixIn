package com.delta.delta.consumer;

import com.delta.delta.dto.NotificationsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageListenerService {

    Logger log = LoggerFactory.getLogger(KafkaMessageListenerService.class);

//    @KafkaListener(topics = "my-topic1", groupId = "my-group-n")
//    @KafkaListener(topics = "${topic.name}", groupId = "${spring.kafka.consumer.group-id}")
//    public void consume(String message) {
//        log.info("consumer1 consume the message {} ", message);
//    }
//
//    @KafkaListener(topics = "${topic.name}",groupId = "${spring.kafka.consumer.group-id}")
//    public void consume2(String message) {
//        log.info("consumer2 consume the message {} ", message);
//    }
//
//    @KafkaListener(topics = "${topic.name}",groupId = "${spring.kafka.consumer.group-id}")
//    public void consume3(String message) {
//        log.info("consumer3 consume the message {} ", message);
//    }
//
//    @KafkaListener(topics = "${topic.name}",groupId = "${spring.kafka.consumer.group-id}")
//    public void consume4(String message) {
//        log.info("consumer4 consume the message {} ", message);
//    }

    @KafkaListener(topics = "${notifications.topic-name}", groupId = "${notifications.group-id}")
    public void consumeObject(NotificationsDto notificationsDto) {
        //get info msg

        //



        log.info("logged {} ", notificationsDto.toString());
    }
}