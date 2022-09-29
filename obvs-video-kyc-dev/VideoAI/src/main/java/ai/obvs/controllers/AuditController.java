package ai.obvs.controllers;

import ai.obvs.dto.*;
import ai.obvs.services.VideoKYCRequestsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/videoKYC/audits/")
public class AuditController {

    private VideoKYCRequestsService videoKYCRequestsService;

    public AuditController(VideoKYCRequestsService videoKYCRequestsService) {
        this.videoKYCRequestsService = videoKYCRequestsService;
    }

    @GetMapping("/audits/save")
    public ResponseEntity<?> saveAuditDetail(@RequestBody AuditRequestDto auditRequestDto) {
        videoKYCRequestsService.saveAudit(auditRequestDto);
        return ResponseEntity.ok("");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuditDetail(@PathVariable(value = "id")  Long videoKYCRequestId) {
        AuditDataDto auditDetail = videoKYCRequestsService.getAuditDetail(videoKYCRequestId);
        return ResponseEntity.ok(auditDetail);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAuditList(@PathVariable(value = "id")  Long videoKYCRequestId) {
        AuditDataDto auditDetail = videoKYCRequestsService.getAuditDetail(videoKYCRequestId);
        return ResponseEntity.ok(auditDetail);
    }
}
