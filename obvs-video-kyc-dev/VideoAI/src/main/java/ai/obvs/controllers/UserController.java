package ai.obvs.controllers;

import ai.obvs.dto.AuthenticationResponse;
import ai.obvs.dto.UserDto;
import ai.obvs.exceptions.AuthenticationFailedException;
import ai.obvs.model.User;
import ai.obvs.security.CurrentUser;
import ai.obvs.security.UserPrincipal;
import ai.obvs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        if (currentUser != null) {
            UserDto userDto = userService.findById(currentUser.getUser().getId());
            AuthenticationResponse authenticationResponse = new AuthenticationResponse(userDto, null);
            return ResponseEntity.ok(authenticationResponse);
        } else {
            throw new AuthenticationFailedException("Invalid token");
        }
    }

    @PostMapping("/firstUser")
    public ResponseEntity<?> registerFirstUser(@Valid @RequestBody UserDto userDto) {
        userService.register(userDto);
        return ResponseEntity.ok("");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto) {
        userService.register(userDto);
        return ResponseEntity.ok("");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable(value = "id") Long id) {
        UserDto userDto = userService.findById(id);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getActiveUser(@RequestHeader("x-obvs-org") Long orgId) {
        List<User> users = userService.getAllActiveUsers(orgId);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto, @RequestHeader("x-obvs-org") Long orgId) {
        return ResponseEntity.ok(userService.create(userDto, orgId));
    }

    @PostMapping("/create/firstAdmin")
    public ResponseEntity<?> createAdminUser(@Valid @RequestBody UserDto userDto, @RequestHeader("x-obvs-org") Long orgId) {
        User user = userService.createSuperAdmin(userDto, orgId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getAdminUser(@RequestHeader("x-obvs-org") Long orgId) {
        boolean adminUserExists = userService.isAdminUserExists(orgId);
        return ResponseEntity.ok(adminUserExists);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser( @RequestHeader("x-obvs-org") Long orgId, @Valid @RequestBody UserDto userDto, @PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(userService.update(userDto,orgId, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long id, @RequestHeader("x-obvs-org") Long orgId) {
        userService.deleteUser(id, orgId);
        return ResponseEntity.ok("");
    }

//    @PostMapping("/{id}/logout")
//    public ResponseEntity<?> login(@PathVariable(value="id") Long id,@Valid @RequestBody LogoutRequest logoutRequest) {
//        userService.updateUserLoginStatus(id, LoginStatus.OFFLINE);
//        return ResponseEntity.ok("");
//    }
}
