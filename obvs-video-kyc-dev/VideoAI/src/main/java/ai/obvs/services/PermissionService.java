package ai.obvs.services;


import ai.obvs.dto.PermissionDto;

import java.util.List;

public interface PermissionService {
    List<PermissionDto> getAll();
}