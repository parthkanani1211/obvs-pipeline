package ai.obvs.services;

import ai.obvs.dto.Org.OrgRequestDto;
import ai.obvs.dto.Org.OrgResponseDto;
import ai.obvs.model.Organization;
import ai.obvs.model.User;

import java.util.List;
import java.util.Optional;

public interface OrgService {
    OrgResponseDto create(OrgRequestDto orgRequestDto);

    OrgResponseDto update(Long orgId, List<User> users);
    Organization update(Long orgId, User user);
    List<OrgResponseDto> getAll();

    OrgResponseDto getById(Long id);

    Optional<Organization> getOrganizationById(Long id);

    List<User> getAll(Long id);

    OrgResponseDto getByName(String name);
}
