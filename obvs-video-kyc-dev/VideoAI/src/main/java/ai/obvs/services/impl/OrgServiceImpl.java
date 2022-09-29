package ai.obvs.services.impl;

import ai.obvs.dto.Org.OrgRequestDto;
import ai.obvs.dto.Org.OrgResponseDto;
import ai.obvs.mapper.OrgMapper;
import ai.obvs.model.Organization;
import ai.obvs.model.User;
import ai.obvs.repository.OrgRepository;
import ai.obvs.services.OrgService;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
public class OrgServiceImpl implements OrgService {

    private OrgRepository orgRepository;

    public OrgServiceImpl(OrgRepository orgRepository) {
        this.orgRepository = orgRepository;
    }

    @Override
    public OrgResponseDto create(OrgRequestDto orgRequestDto) {
        Organization organization = OrgMapper.MAPPER.ToOrganization(orgRequestDto);
        organization.setCreatedOn(ZonedDateTime.now());
        organization.setCounty(orgRequestDto.getCountry());
        Organization savedOrganization = orgRepository.save(organization);

        return OrgMapper.MAPPER.ToOrganizationDto(savedOrganization);
    }

    @Override
    public OrgResponseDto update(Long orgId, List<User> users) {
        return null;
    }

    @Override
    public Organization update(Long orgId, User user) {
        Optional<Organization> org = orgRepository.findById(orgId);
        if(!org.isPresent()){
            throw new IllegalArgumentException("Organization does not exits");
        }
        org.get().getUsers().add(user);
        return orgRepository.save(org.get());
    }

    @Override
    public List<OrgResponseDto> getAll() {
        List<Organization> organizationList = orgRepository.findAll();
        return OrgMapper.MAPPER.ToOrganizationDtoList(organizationList);
    }

    @Override
    public List<User> getAll(Long id) {
        return null;
    }

    @Override
    public OrgResponseDto getById(Long id) {
        Optional<Organization> optionalOrganization = findById(id);

        if(optionalOrganization.isPresent()){
            Organization organization = optionalOrganization.get();
            return OrgMapper.MAPPER.ToOrganizationDto(organization);
        }

        return new OrgResponseDto();
    }

    private Optional<Organization> findById(Long id) {
        return orgRepository.findById(id);
    }

    @Override
    public Optional<Organization> getOrganizationById(Long id) {
        Optional<Organization> optionalOrganization = findById(id);
        return optionalOrganization;

    }

    @Override
    public OrgResponseDto getByName(String name) {
        Optional<Organization> optionalOrganization = orgRepository.findByName(name);

        if(optionalOrganization.isPresent()){
            Organization organization = optionalOrganization.get();
            return OrgMapper.MAPPER.ToOrganizationDto(organization);
        }

        return new OrgResponseDto();
    }
}
