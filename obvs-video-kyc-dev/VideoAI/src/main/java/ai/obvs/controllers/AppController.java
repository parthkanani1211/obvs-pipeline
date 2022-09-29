package ai.obvs.controllers;

import ai.obvs.dto.AppDto;
import ai.obvs.services.AppService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/v1/services")
public class AppController extends BaseController {

    private AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @PostMapping("/")
    public ResponseEntity<?> post(@Valid @RequestBody AppDto appDto) {
        appService.create(appDto);
        return ResponseEntity.ok("");
    }

    @GetMapping("/")
    public ResponseEntity<?> get() {
        List<AppDto> services = appService.getAll();
        return ResponseEntity.ok(services);
    }

}
