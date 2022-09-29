package ai.obvs.services.impl;

import ai.obvs.Enums.Roles;
import ai.obvs.dto.AgentDto;
import ai.obvs.dto.CustomerDto;
import ai.obvs.dto.RoleBaseDto;
import ai.obvs.dto.UserDto;
import ai.obvs.mapper.UserMapper;
import ai.obvs.model.Organization;
import ai.obvs.model.Role;
import ai.obvs.model.User;
import ai.obvs.repository.OrgRepository;
import ai.obvs.repository.UserRepository;
import ai.obvs.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrgService orgService;

    @Autowired
    private OrgRepository orgRepository;

    @Autowired
    private AgentService agentService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OTPService otpService;

    @Override
    public void registerFirstUser(UserDto userDto) {
        User user = UserMapper.MAPPER.ToUser(userDto);
        Optional<Role> optionalAdminRole = roleService.getRoleByName("Admin");
        if (optionalAdminRole.isPresent()) {
            Set<Role> roles = new HashSet<>();
            roles.add(optionalAdminRole.get());
//            Set<Role> roles = Set.of(optionalAdminRole.get());
            user.setRoles(roles);
        }
        userRepository.save(user);
    }

    @Override
    public <T> T create(UserDto userDto, Long orgId) {
        Optional<Organization> organizationById = orgService.getOrganizationById(orgId);
        if (!organizationById.isPresent()) {
            throw new IllegalArgumentException("Organization does not exits");
        }
        List<RoleBaseDto> roleDTO = userDto.getRoles();
        List<String> roleNames = roleDTO.stream().map(r -> r.getName()).collect(Collectors.toList());
        if (roleNames.isEmpty()) {
            throw new IllegalArgumentException("No roles present with specified names");
        }

        Optional<User> byMobileNumber = userRepository.findByMobileNumber(userDto.getMobileNumber());
        if (byMobileNumber.isPresent()) {
            throw new IllegalArgumentException("User already exits");
        }

        if (roleNames.contains("Maker") || roleNames.contains("Checker")) {
            return (T) agentService.create(orgId, new AgentDto(userDto));
        } else if (roleNames.contains("Customer")) {
            return (T) customerService.create(orgId, new CustomerDto(userDto));
        }
        return createUser(userDto, orgId, roleNames);
    }

    @Override
    public <T> T createSuperAdmin(UserDto userDto, Long orgId) {
        if (isAdminUserExists(orgId)) {
            throw new IllegalArgumentException("First Admin is already created for org :" + orgId);
        }
        return createUser(userDto, orgId, Arrays.asList("SuperAdmin","Admin"));
    }

    @Override
    public <T> T createAdmin(UserDto userDto, Long orgId) {
        if (isAdminUserExists(orgId)) {
            throw new IllegalArgumentException("First Admin is already created for org :" + orgId);
        }
        return createUser(userDto, orgId, Arrays.asList("Admin"));
    }

    private <T> T createUser(UserDto userDto, Long orgId, List<String> roleNames) {
        User user = UserMapper.MAPPER.ToUser(userDto);

        List<Role> dbRoles = roleService.getRolesByName(roleNames);
        Set<Role> roles = new HashSet<>();
        dbRoles.stream().forEach(r -> {
            roles.add(r);
        });
        user.setRoles(roles);
        user.setCreatedOn(ZonedDateTime.now());
        user.setUpdatedOn(ZonedDateTime.now());
        user = userRepository.save(user);
        orgService.update(orgId, user);
        return (T) UserMapper.MAPPER.ToUserDto(user);
    }

    @Override
    public void register(UserDto userDto) {
        User user = UserMapper.MAPPER.ToUser(userDto);
        Set<Role> roles = user.getRoles();
        List<Role> roleList = roles.stream().filter(x -> x.getName().equals(Roles.APIUSER.getValue())).collect(Collectors.toList());
        if(roleList.size() > 0){
            int code = otpService.generateOTP(user.getMobileNumber());
            user.setCode(String.valueOf(code));
        }
        userRepository.save(user);
    }

    @Override
    public boolean isUserExists(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.isPresent();
    }

    @Override
    public UserDto findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return UserMapper.MAPPER.ToUserDto(optionalUser.get());
        }
        return null;
    }

    @Override
    public boolean isUserRegistered(Long mobileNumber) {
        Optional<User> userOptional = userRepository.findByMobileNumber(mobileNumber);
        return userOptional.isPresent();
    }

    @Override
    public Optional<User> getUserByMobileNumber(Long mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateUser(UserDto UserDto, Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException(String.format("No user exists for user Id %s", userId));
        }
        User userToUpdate = UserMapper.MAPPER.ToUser(UserDto);
        userToUpdate.setId(userId);
        userToUpdate.setUpdatedOn(ZonedDateTime.now());
        return userRepository.save(userToUpdate);
    }

    @Override
    public <T> T update(UserDto userDto, Long orgId, Long userId) {
        Optional<Organization> organizationById = orgService.getOrganizationById(orgId);
        if (!organizationById.isPresent()) {
            throw new IllegalArgumentException("Organization does not exits");
        }
        List<RoleBaseDto> roleDTO = userDto.getRoles();
        if(roleDTO.isEmpty()){
            throw new IllegalArgumentException("Please select roles");
        }

        List<String> roleNames = roleDTO.stream().map(r -> r.getName()).collect(Collectors.toList());
        if (roleNames.isEmpty()) {
            throw new IllegalArgumentException("No roles present with specified names");
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.getMobileNumber().equals(userDto.getMobileNumber())) {
                userDto.setId(user.getId());
                if (roleNames.contains("Maker") || roleNames.contains("Checker")) {
                    return (T) agentService.update(orgId, new AgentDto(userDto));
                } else if (roleNames.contains("Customer")) {
                    return (T) customerService.update(new CustomerDto(userDto));
                }
                User userToUpdate = UserMapper.MAPPER.ToUser(userDto);
                userToUpdate.setUpdatedOn(ZonedDateTime.now());
                return (T) userRepository.save(userToUpdate);
            }
            else {
                throw new IllegalArgumentException("Invalid mobile number");
            }
        }
        throw new IllegalArgumentException(String.format("No user exists for user Id %s", userId));

    }

    @Override
    public List<User> getAllActiveUsers(Long orgId) {
        List<User> users = userRepository.findActiveUserByOrganization(orgId);
        if (users.isEmpty() || users.size() == 0) {
            logger.error("No active users exists for org :" + orgId);
            throw new IllegalArgumentException("No active users exists for org :" + orgId);
        }
        return users;
    }

    @Override
    public Boolean isAdminUserExists(Long orgId) {
        Optional<Role> role = roleService.getRoleByName("admin");
        List<User> users = userRepository.findByOrganizationAndRole(orgId, role.get().getId());
        if (users.isEmpty() || users.size() == 0) {
            logger.debug("No admin user exists for org :" + orgId);
            return false;
        }
        return true;
    }

    @Override
    public void deleteUser(Long userId, Long orgId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException(String.format("No user exists for user Id %s", userId));
        }
        optionalUser.get().setActive(false);
        userRepository.save(optionalUser.get());
    }

//    @Override
//    public void updateUserLoginStatus(Long userId, LoginStatus loginStatus) {
//        Optional<User> userOptional = userRepository.findById(userId);
//        if(userOptional.isPresent()){
//            User user = userOptional.get();
//            user.setLoginStatus(loginStatus);
//            userRepository.save(user);
//        }
//    }
}
