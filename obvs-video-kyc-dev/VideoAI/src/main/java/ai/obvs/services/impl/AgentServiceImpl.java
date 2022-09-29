package ai.obvs.services.impl;

import ai.obvs.Enums.Roles;
import ai.obvs.dto.AgentDto;
import ai.obvs.mapper.AgentMapper;
import ai.obvs.model.Agent;
import ai.obvs.model.Organization;
import ai.obvs.model.Role;
import ai.obvs.model.User;
import ai.obvs.repository.AgentRepository;
import ai.obvs.services.AgentService;
import ai.obvs.services.OrgService;
import ai.obvs.services.RoleService;
import ai.obvs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
public class AgentServiceImpl implements AgentService {

    private AgentRepository agentRepository;
    private RoleService roleService;
    private UserService userService;
    private OrgService orgService;

    @Autowired
    private OTPService otpService;

    public AgentServiceImpl(AgentRepository agentRepository, UserService userService, RoleService roleService, OrgService orgService) {
        this.agentRepository = agentRepository;
        this.userService = userService;
        this.roleService = roleService;
        this.orgService = orgService;
    }

    @Override
    public AgentDto create(AgentDto agentDto) {
        Agent agent = AgentMapper.MAPPER.ToAgent(agentDto);
        Optional<User> optionalUser = userService.getUserByMobileNumber(agent.getMobileNumber());
        if (optionalUser.isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        Optional<Role> optionalAgentRole = roleService.getRoleByName("Maker");
        if (optionalAgentRole.isPresent()) {
            Set<Role> roles = new HashSet<>();
            roles.add(optionalAgentRole.get());
            agent.setRoles(roles);
        }
        agentDto.setCreatedOn(ZonedDateTime.now());
        agentDto.setUpdatedOn(ZonedDateTime.now());
        Agent savedAgent = agentRepository.save(agent);
        return AgentMapper.MAPPER.ToAgentDto(savedAgent);
    }

    @Override
    public AgentDto create(Long orgId, AgentDto agentDto) {
        Agent agent = AgentMapper.MAPPER.ToAgent(agentDto);
        Optional<User> optionalUser = userService.getUserByMobileNumber(agent.getMobileNumber());
        if (optionalUser.isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        Set<Role> roles = new HashSet<>();
        if (agentDto.getRoles().size() > 0) {
            List<String> roleNameList = agentDto.getRoles().stream().map(roleBaseDto -> roleBaseDto.getName()).collect(Collectors.toList());
            List<Role> rolesByName = roleService.getRolesByName(roleNameList);
            roles.addAll(rolesByName);
        }
        agent.setRoles(roles);

        Optional<Organization> organizationById = orgService.getOrganizationById(orgId);
        if (organizationById.isPresent()) {
            List<Organization> organizations = agent.getOrganizations();
            Organization organization = organizationById.get();
            if (!organizations.contains(organization)) {
                organization.getUsers().add(agent);
                organizations.add(organization);
                agent.setOrganizations(organizations);
            }
        }
        agentDto.setCreatedOn(ZonedDateTime.now());
        agentDto.setUpdatedOn(ZonedDateTime.now());

        List<Role> roleList = roles.stream().filter(x -> x.getName().equals(Roles.APIUSER.getValue())).collect(Collectors.toList());
        if(roleList.size() > 0){
            int code = otpService.generateOTP(agent.getMobileNumber());
            agent.setCode(String.valueOf(code));
        }

        Agent savedAgent = agentRepository.save(agent);
        return AgentMapper.MAPPER.ToAgentDto(savedAgent);
    }

    @Override
    public AgentDto update(Long orgId, AgentDto agentDto) {
        Agent agent = AgentMapper.MAPPER.ToAgent(agentDto);
        Set<Role> roles = new HashSet<>();
        if (agentDto.getRoles().size() > 0) {
            List<String> roleNameList = agentDto.getRoles().stream().map(roleBaseDto -> roleBaseDto.getName()).collect(Collectors.toList());
            List<Role> rolesByName = roleService.getRolesByName(roleNameList);
            roles.addAll(rolesByName);
        }
        agent.setRoles(roles);

        Optional<Agent> optionalAgent = agentRepository.findById(agentDto.getId());
        if(optionalAgent.isPresent()){
            Agent agentToSave = optionalAgent.get();
            agentToSave.setRoles(agent.getRoles());
            agentToSave.setEmployeeId(agent.getEmployeeId());
            agentToSave.setEmployeeName(agent.getEmployeeName());
            agentToSave.setActive(agent.isActive());
            agentToSave.setFirstName(agent.getFirstName());
            agentToSave.setLastName(agent.getLastName());
            agentToSave.setMobileNumber(agent.getMobileNumber());
            agentToSave.setEmailAddress(agent.getEmailAddress());
            agentToSave.setUpdatedOn(ZonedDateTime.now());

            List<Organization> organizations = agentToSave.getOrganizations();
            Optional<Organization> organizationById = orgService.getOrganizationById(orgId);
            if (organizationById.isPresent()) {
                Organization organization = organizationById.get();
                if (!organizations.contains(organization)) {
                    organization.getUsers().add(agent);
                    organizations.add(organization);
                    agent.setOrganizations(organizations);
                }
            }

            Agent savedAgent = agentRepository.save(agentToSave);
            return AgentMapper.MAPPER.ToAgentDto(savedAgent);
        }
        throw new IllegalArgumentException("Unable to update the user. User doesn't not exists.");
    }

    @Override
    public Set<AgentDto> getAll() {
        List<Agent> agents = agentRepository.findAll();
        return agents.stream().map(agent -> AgentMapper.MAPPER.ToAgentDto(agent)).collect(Collectors.toSet());
    }

    @Override
    public Optional<Agent> getById(Long id) {
        return agentRepository.findById(id);
    }
}
