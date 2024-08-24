package com.delta.delta.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;
    private Long receiverId;

    private Long postId;

    @Column(nullable = false)
    private String eventType;

    private LocalDateTime eventCreatedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_stack")
    private NotificationStack notificationStack;

}
