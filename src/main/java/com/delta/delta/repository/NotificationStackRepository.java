package com.delta.delta.repository;

import com.delta.delta.entity.NotificationStack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationStackRepository extends JpaRepository<NotificationStack, Long> {
    NotificationStack findByOwnerId(Long ownerId);
}
