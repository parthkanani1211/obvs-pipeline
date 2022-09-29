package ai.obvs.services;

import ai.obvs.dto.*;

public interface AuditService {
    void saveAudit(AuditRequestDto auditRequestDto);
    AuditDataDto getAuditDetail(Long videoKYCId);
}
