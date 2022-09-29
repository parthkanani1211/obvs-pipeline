package ai.obvs.controllers;

import ai.obvs.dto.PermissionDto;
import ai.obvs.services.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/v1/permissions")
public class PermissionController {

    private PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAll() {
        List<PermissionDto> permissions = permissionService.getAll();
        return ResponseEntity.ok(permissions);
    }
}
