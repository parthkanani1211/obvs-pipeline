package ai.obvs.services.impl;

import ai.obvs.dto.PermissionDto;
import ai.obvs.model.Permission;
import ai.obvs.mapper.PermissionMapper;
import ai.obvs.repository.PermissionRepository;
import ai.obvs.services.PermissionService;

import java.util.List;

public class PermissionServiceImpl implements PermissionService {

    private PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<PermissionDto> getAll() {
        List<Permission> permissions = permissionRepository.findAll();
        return PermissionMapper.MAPPER.permissionSetToPermissionDtoList(permissions);
    }
}
