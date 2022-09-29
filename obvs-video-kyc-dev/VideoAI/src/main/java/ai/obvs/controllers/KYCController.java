package ai.obvs.controllers;

import ai.obvs.services.KYCService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/KYC")
public class KYCController {

    private KYCService kycService;

    public KYCController(KYCService kycService) {
        this.kycService = kycService;
    }

//    @PostMapping("/")
//    public ResponseEntity<?> post(@RequestParam(value = "kycContent") MultipartFile file, @RequestParam("kycData") String kycDtoString) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        KYCDto kycDto = objectMapper.readValue(kycDtoString, KYCDto.class);
//        KYCDto savedKYCDto = kycService.Save(kycDto, file.getBytes());
//        return ResponseEntity.ok(savedKYCDto);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getKYCContent(@PathVariable(value="id") Long id) {
        byte[] content = kycService.getContent(id);
        return ResponseEntity.ok(new ByteArrayResource(content));
    }
}
