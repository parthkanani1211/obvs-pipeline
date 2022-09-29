package ai.obvs.services;


import ai.obvs.dto.RoleBasicDto;
import ai.obvs.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    RoleBasicDto getById(Long id);

    List<RoleBasicDto> list();

    RoleBasicDto create(RoleBasicDto roleBasicDto);

    RoleBasicDto update(Long id, RoleBasicDto roleBasicDto);

    void delete(Long id);

    Optional<Role> getRoleByName(String roleName);

    List<Role> getRolesByName(List<String> roleNameList);
}