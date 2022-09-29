package ai.obvs.controllers;

import ai.obvs.dto.Org.OrgRequestDto;
import ai.obvs.dto.Org.OrgResponseDto;
import ai.obvs.services.OrgService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/v1/orgs")
public class OrganizationController {

    private OrgService orgService;

    public OrganizationController(OrgService orgService) {
        this.orgService = orgService;
    }

    @PostMapping("/")
    public ResponseEntity<?> post(@Valid @RequestBody OrgRequestDto orgRequestDto) {
        OrgResponseDto orgResponseDto = orgService.create(orgRequestDto);
        return ResponseEntity.ok(orgResponseDto);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAll() {
        List<OrgResponseDto> orgResponseDtoList = orgService.getAll();
        return ResponseEntity.ok(orgResponseDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable(value="id") Long id) {
        OrgResponseDto orgResponseDto = orgService.getById(id);
        return ResponseEntity.ok(orgResponseDto);
    }

    @GetMapping("/")
    public ResponseEntity<?> get(@Valid @RequestParam String orgName) {
//        String orgName = orgRequestDto.getName();
        OrgResponseDto orgResponseDto = orgService.getByName(orgName);
        return ResponseEntity.ok(orgResponseDto);
    }
}
