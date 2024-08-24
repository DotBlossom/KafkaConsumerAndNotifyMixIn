package com.delta.delta.service;

import com.delta.delta.dto.NotificationsDto;
import com.delta.delta.entity.Notification;
import com.delta.delta.entity.NotificationStack;
import com.delta.delta.repository.EmitterRepository;

import com.delta.delta.repository.NotificationRepository;
import com.delta.delta.repository.NotificationStackRepository;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    //chore: logIn 이후, react에서 불러오면 이거 만들어줘야함 notification;; + cnt ++

    private final EmitterRepository emitterRepository;
    private final NotificationStackRepository notificationStackRepository;
    private final NotificationRepository notificationRepository;

    private static final long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private static final int MAX_NOTIFICATIONS_COUNT = 10;

    Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @KafkaListener(topics = "${notifications.topic-name}", groupId = "${notifications.group-id}")
    public void consumeObject(NotificationsDto dto) {
        Long receiverId = dto.getReceiverId();
        String emitterReceiverId = receiverId.toString();

        Notification notification = Notification.builder()
                .senderId(dto.getSenderId())
                .receiverId(dto.getReceiverId())
                .eventCreatedTime(LocalDateTime.now())
                .eventType(dto.getEventType())
                .build();




        NotificationStack stack = notificationStackRepository.findByOwnerId(receiverId);
        // 잘못된 코드
        if (stack != null) {
            if (stack.getStackLength() >= MAX_NOTIFICATIONS_COUNT) {
                stack.getNotifications().remove(MAX_NOTIFICATIONS_COUNT - 1);

                // query호출..
            }



            notification.setNotificationStack(stack);
            notificationRepository.save(notification);



        } else {


            NotificationStack newStack = NotificationStack.builder()
                    .ownerId(dto.getReceiverId())
                    .build();

            List<Notification> LN = new ArrayList<>();
            LN.add(notification);


            newStack.setNotifications(LN);
            notification.setNotificationStack(newStack);
            notificationStackRepository.save(newStack);

        }

        // cnt ++ -> notificationStckRepoSave.


        // emitter가 여려개 연결된 경우? notifi 말고 확장 or 페이지 여려개
        // kafka listen -> notification 저장 -> eventSend(sendToClient)
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByReceiverId(emitterReceiverId);
        if (!sseEmitters.isEmpty()) {
            sseEmitters.forEach(
                    (key, emitter) -> {
                        emitterRepository.saveEventCache(key, notification);
                        sendToClient(emitter, key, notification);
                    }
            );

        } else {
            //logout push
            emitterRepository.saveEventCache(emitterReceiverId, notification);
        }
    }


    public void sendToClient(SseEmitter emitter, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .data(data));
            log.info("Kafka로 부터 전달 받은 메세지 전송. emitterId : {}, message : {}", emitterId, data);
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
            log.error("메시지 전송 에러 : {}", e);
        }
    }

    public SseEmitter subscribe(String userId, String lastEventId) {
        String emitterId = userId + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        log.info("emitterId : {} 사용자 emitter 연결 ", emitterId);

        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            emitterRepository.deleteById(emitterId);
        });
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitterRepository.deleteById(emitterId);
        });

        sendToClient(emitter, emitterId, "connected!"); // 503 에러방지 더미 데이터


        //send clinet 미수신 data -> 다시 보내주기.
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByReceiverId(userId);
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }


        return emitter;
    }

    @Scheduled(fixedRate = 60 * 1000 * 5) //heartbeat 메세지 전달.
    public void sendHeartbeat() {
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitters();
        sseEmitters.forEach((key, emitter) -> {
            try {
                emitter.send(SseEmitter.event().id(key).name("heartbeat").data(""));
            } catch (IOException e) {
                emitterRepository.deleteById(key);
                log.error("heartBeat is failed: {}", e.getMessage());
            }
        });
    }
}
