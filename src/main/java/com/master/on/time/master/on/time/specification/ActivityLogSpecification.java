package com.master.on.time.master.on.time.specification;

import com.master.on.time.master.on.time.model.ActivityLog;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;

public class ActivityLogSpecification {

    public static Specification<ActivityLog> hasTimestampAfter(LocalDateTime startDate) {
        return (root, query, cb) -> {
            if (startDate == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("timestamp"), startDate);
        };
    }

    public static Specification<ActivityLog> hasTimestampBefore(LocalDateTime endDate) {
        return (root, query, cb) -> {
            if (endDate == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("timestamp"), endDate);
        };
    }

    public static Specification<ActivityLog> hasUsername(String username) {
        return (root, query, cb) -> {
            if (username == null || username.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(cb.lower(root.get("username")), username.toLowerCase());
        };
    }

    public static Specification<ActivityLog> hasActionType(String actionType) {
        return (root, query, cb) -> {
            if (actionType == null || actionType.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(cb.lower(root.get("actionType")), actionType.toLowerCase());
        };
    }

    public static Specification<ActivityLog> hasAffectedEntity(String affectedEntity) {
        return (root, query, cb) -> {
            if (affectedEntity == null || affectedEntity.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(cb.lower(root.get("affectedEntity")),
                    affectedEntity.toLowerCase());
        };
    }
}
