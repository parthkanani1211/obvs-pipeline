package ai.obvs.controllers;

import ai.obvs.dto.ApiResponse;
import ai.obvs.dto.RoleBasicDto;
import ai.obvs.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class RoleController {

    @Autowired
    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/roles/list")
    public ResponseEntity<?>  list() {
        List<RoleBasicDto> roles = roleService.list();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<?>  getUserById(@PathVariable(value="id") Long id) {
        RoleBasicDto role = roleService.getById(id);
        return ResponseEntity.ok(role);
    }

    @PostMapping("/roles")
    public ResponseEntity<?> post(@RequestBody RoleBasicDto roleBasicDto) {
        RoleBasicDto newRole = roleService.create(roleBasicDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/roles/{roleName}")
                .buildAndExpand(newRole.getName()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Role created successfully"));
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<?> put(@PathVariable(value="id") Long id, @RequestBody RoleBasicDto roleBasicDto) {

        roleService.update(id, roleBasicDto);
        return ResponseEntity.ok(new ApiResponse(true, "Role updated successfully"));
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<?> delete(@PathVariable(value="id") Long id) {

        roleService.delete(id);
        return ResponseEntity.ok().build();
    }
}
