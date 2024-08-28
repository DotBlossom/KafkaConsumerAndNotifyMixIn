package com.delta.delta.consumer;

import com.delta.delta.dto.NotificationsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageListenerService {

    Logger log = LoggerFactory.getLogger(KafkaMessageListenerService.class);




    //@KafkaListener(topics = "${notifications.topic-name}", groupId = "${notifications.group-id-2}",
    //    filter = "headers['eventType-header'] == 'D'")
    //public void consumeAgg(NotificationsDto notificationsDto) {
        //get info msg

    //    log.info("logged DataAggs {} ", notificationsDto.toString());
    //}
}