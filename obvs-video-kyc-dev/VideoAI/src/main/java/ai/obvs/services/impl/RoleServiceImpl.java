package ai.obvs.services.impl;

import ai.obvs.dto.RoleBasicDto;
import ai.obvs.model.Permission;
import ai.obvs.model.Role;
import ai.obvs.mapper.PermissionMapper;
import ai.obvs.mapper.RoleMapper;
import ai.obvs.repository.RoleRepository;
import ai.obvs.services.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleBasicDto getById(Long id) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("role was not found"));

        RoleBasicDto roleBasicDto = RoleMapper.MAPPER.roleToRoleBasicDto(role);

        return roleBasicDto;
    }

    @Override
    public List<RoleBasicDto> list() {

        List<Role> roles = roleRepository.findAll();
        return RoleMapper.MAPPER.roleListToRoleBasicDtoList(roles);
    }

    @Override
    public RoleBasicDto create(RoleBasicDto roleBasicDto) {
        if (roleRepository.existsByName(roleBasicDto.getName())) {
            throw new IllegalArgumentException("role name already exist");
        }

        Role role = RoleMapper.MAPPER.roleBasicDtoToRole(roleBasicDto);

        Set<Permission> permissions = role.getPermissions();

        if (permissions.isEmpty()) {
            throw new IllegalArgumentException("At least one permission should be added!");
        }

        Role newRole = roleRepository.save(role);

        return RoleMapper.MAPPER.roleToRoleBasicDto(newRole);
    }

    @Override
    public RoleBasicDto update(Long id, RoleBasicDto roleBasicDto) {

        Optional<Role> role = roleRepository.findById(roleBasicDto.getId());
        if (role.isPresent()) {
            Role r = role.get();
            r.setDescription(roleBasicDto.getDescription());
            Set<Permission> permissions = PermissionMapper.MAPPER.permissionDtoSetToPermissionSet(roleBasicDto.getPermissions());
            r.setPermissions(permissions);

            Role updatedRole = roleRepository.save(r);

            return RoleMapper.MAPPER.roleToRoleBasicDto(updatedRole);
        } else {
            throw new IllegalArgumentException("role was not found");
        }
    }

    @Override
    public void delete(Long id) {
        Optional<Role> role = roleRepository.findById(id);

        if (role.isPresent()) {
            roleRepository.delete(role.get());
        } else {
            throw new IllegalArgumentException("role was not found");
        }
    }

    @Override
    public Optional<Role> getRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }
    @Override
    public List<Role> getRolesByName(List<String> roleNameList) {
        return roleRepository.findAllByNameIn(roleNameList);
    }
}
