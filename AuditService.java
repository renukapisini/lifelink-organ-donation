package com.lifetrace.backend.service;

import com.lifetrace.backend.model.AuditLog;
import com.lifetrace.backend.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void log(
            String action,
            String entityType,
            Long entityId,
            String oldValue,
            String newValue
    ) {

        String performedBy = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        AuditLog audit = AuditLog.builder()
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .oldValue(oldValue)
                .newValue(newValue)
                .performedBy(performedBy)
                .timestamp(LocalDateTime.now())
                .build();

        auditLogRepository.save(audit);
    }
}
