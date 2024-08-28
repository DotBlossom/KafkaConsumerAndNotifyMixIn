package com.delta.delta.controller;

import com.delta.delta.dto.PreferenceDataDto;

import com.delta.delta.service.DataAggToPreferenceService;
import com.delta.delta.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kafkaListener")
public class NotificationController {

    private final NotificationService notificationService;
    private final DataAggToPreferenceService dataAggToPreferenceService;

    @GetMapping(value = "/subscribe/{user_id}", produces = "text/event-stream;charset=UTF-8")
    public SseEmitter subscribe(@PathVariable(value = "user_id") Long userId,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(userId.toString(), lastEventId);
    }

    @GetMapping("/preference/posts")
    public ResponseEntity<PreferenceDataDto> getPreferencePostList() {
        return ResponseEntity.ok(dataAggToPreferenceService.sendPreferData());

    }


}