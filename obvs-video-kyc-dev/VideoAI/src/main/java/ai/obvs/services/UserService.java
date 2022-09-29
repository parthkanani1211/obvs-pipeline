package ai.obvs.services;

import ai.obvs.dto.UserDto;
import ai.obvs.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void registerFirstUser(UserDto userDto);
    void register(UserDto userDto);
    boolean isUserExists(Long id);
    UserDto findById(Long id);
    boolean isUserRegistered(Long mobileNumber);
    Optional<User> getUserByMobileNumber(Long mobileNumber);

    Optional<User> getUserById(Long id);
//    void updateUserLoginStatus(Long userId, LoginStatus loginStatus);
	<T> T create(UserDto userDto,Long orgId);

    <T> T createSuperAdmin(UserDto userDto, Long orgId);

    <T> T createAdmin(UserDto userDto, Long orgId);
	User updateUser(UserDto userDto, Long userId);

    <T> T update(UserDto userDto, Long orgId, Long userId);

    List<User> getAllActiveUsers(Long orgId);
	void deleteUser(Long userId,Long orgId);
    Boolean isAdminUserExists(Long orgId);
}
