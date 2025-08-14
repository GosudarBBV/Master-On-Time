package com.master.on.time.master.on.time.repository;

import com.master.on.time.master.on.time.model.Notification;
import com.master.on.time.master.on.time.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
}
