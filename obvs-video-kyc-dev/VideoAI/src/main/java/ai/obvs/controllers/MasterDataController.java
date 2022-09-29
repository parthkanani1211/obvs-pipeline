package ai.obvs.controllers;

import ai.obvs.model.MasterData.*;
import ai.obvs.services.CKYC.MasterDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/v1/masterdata")
public class MasterDataController {

    @Autowired
    private MasterDataService masterDataService;

    @PostMapping("/nameprefix/list")
    public ResponseEntity<?> getNamePrefixList(@RequestHeader("x-obvs-org") Long orgId) {
        List<NamePrefix> allNamePrefix = masterDataService.findAllNamePrefix();
        return ResponseEntity.ok(allNamePrefix);
    }

    @PostMapping("/maritalstatus/list")
    public ResponseEntity<?> getMaritalStatusList(@RequestHeader("x-obvs-org") Long orgId) {
        List<MaritalStatus> allMaritalStatus = masterDataService.findAllMaritalStatus();
        return ResponseEntity.ok(allMaritalStatus);
    }

    @GetMapping("/occupation/list")
    public ResponseEntity<?> getOccupationList(@RequestHeader("x-obvs-org") Long orgId) {
        List<Occupation> allOccupation = masterDataService.findAllOccupation();
        return ResponseEntity.ok(allOccupation);
    }

    @GetMapping("/community/list")
    public ResponseEntity<?> getCommunityList(@RequestHeader("x-obvs-org") Long orgId) {
        List<Community> allCommunities = masterDataService.findAllCommunities();
        return ResponseEntity.ok(allCommunities);
    }

    @GetMapping("/constitution/list")
    public ResponseEntity<?> getConstitutionList(@RequestHeader("x-obvs-org") Long orgId) {
        List<Constitution> allConstitution = masterDataService.findAllConstitution();
        return ResponseEntity.ok(allConstitution);
    }

    @GetMapping("/residencystatus/list")
    public ResponseEntity<?> getResidencyStatusList(@RequestHeader("x-obvs-org") Long orgId) {
        List<ResidencyStatus> allResidencyStatus = masterDataService.findAllResidencyStatus();
        return ResponseEntity.ok(allResidencyStatus);
    }

}
