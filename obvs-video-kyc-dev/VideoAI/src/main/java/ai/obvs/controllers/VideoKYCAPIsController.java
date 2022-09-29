package ai.obvs.controllers;

import ai.obvs.Enums.Roles;
import ai.obvs.dto.*;
import ai.obvs.exceptions.AuthenticationFailedException;
import ai.obvs.mapper.CBRequestMapper;
import ai.obvs.model.*;
import ai.obvs.security.CurrentUser;
import ai.obvs.security.UserPrincipal;
import ai.obvs.services.*;
import ai.obvs.services.CKYC.CKYCDataService;
import ai.obvs.services.CKYC.CKYCProfileVideoKYCService;
import ai.obvs.services.CKYC.FormatDataService;
import ai.obvs.services.CKYC.MasterDataService;
import ai.obvs.services.CKYC.model.CKYCResponseData;
import ai.obvs.services.notification.NotificationService;
import ai.obvs.services.notification.PropertiesData;
import ai.obvs.services.notification.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/v1/banking")
public class VideoKYCAPIsController {

    @Autowired
    private MasterDataService masterDataService;
    @Autowired
    private CBRequestsService cbRequestsService;
    @Autowired
    private VideoKYCRequestsService videoKYCRequestsService;
    @Autowired
    private CKYCDataService ckycDataService;
    @Autowired
    private CKYCProfileVideoKYCService ckycProfileVideoKYCService;
    @Autowired
    private UserService userService;
    @Autowired
    private FormatDataService formatDataService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PropertiesData propertiesData;

    @PostMapping("/api/init")
    public ResponseEntity<?> initRequest(@RequestHeader("x-obvs-org") Long orgId,
                                         @Valid @RequestBody CBRequestDto cbRequestDto,
                                         @CurrentUser UserPrincipal currentUser) {
        validateUser(currentUser);
//        emailService.sendSimpleMessage("neha0319@gmail.com","Test Email", "Testing email from Obvious Technology.");
//        try {
//            smsService.sendMessage(8460107732L,"Hello");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        }

        CBRequest cbRequest = CBRequestMapper.MAPPER.ToCBRequest(cbRequestDto);
        CBRequest savedCBRequest = cbRequestsService.InitiateRequest(cbRequest);
        CBRequestDto savedCBRequestDto = CBRequestMapper.MAPPER.ToCCBRequestDto(savedCBRequest);
        String initURL = notificationService.sendVideoKYCInitURL(cbRequest.getMobileNumber(), cbRequest.getRefId());
        if (propertiesData.getSmsEnabled()) {
            return ResponseEntity.ok(savedCBRequestDto);
        } else
            return ResponseEntity.ok(initURL);
    }

    @GetMapping("/api/ckyc")
    public ResponseEntity<?> getCKYC(@RequestHeader("x-obvs-org") Long orgId,
                                     @Valid @RequestBody CBRequestDto cbRequestDto,
                                     @CurrentUser UserPrincipal currentUser) {

        validateUser(currentUser);

        CBRequest cbRequest = cbRequestsService.getCBRequestByRefId(cbRequestDto.getRefId());

        CKYCResponseData ckycResponseData = getCkycResponseData(orgId, cbRequest);
        return ResponseEntity.ok(ckycResponseData);
    }

    private void validateUser(UserPrincipal currentUser) {
        Optional<User> optionalUser = userService.getUserById(currentUser.getUser().getId());
        if (!optionalUser.isPresent()) {
            throw new AuthenticationFailedException("UnAuthorized user. Please use valid user to perform API calls.");
        }
        User user = optionalUser.get();
        List<Role> roleList = user.getRoles().stream().filter(x -> x.getName().equals(Roles.APIUSER.getValue())).collect(Collectors.toList());
        if (roleList.size() == 0) {
            throw new AuthenticationFailedException("UnAuthorized user. Please use valid user to perform API calls.");
        }
    }

    private CKYCResponseData getCkycResponseData(Long orgId, CBRequest cbRequest) {
        Optional<User> userOptional = userService.getUserByMobileNumber(cbRequest.getMobileNumber());
        if (userOptional.isPresent()) {
            List<CKYCVRequest> ckycvRequestList = ckycProfileVideoKYCService.findAllByRefId(cbRequest.getRefId());
            if (ckycvRequestList.size() > 0) {
                CKYCVRequest ckycvRequest = ckycvRequestList.get(0);
                Optional<CKYCProfileVKYCRequest> ckycProfileVKYCRequestOptional = ckycProfileVideoKYCService.findByCKYCVRequest(ckycvRequest);
                CKYCProfileData ckycProfileData = new CKYCProfileData();
                if (ckycProfileVKYCRequestOptional.isPresent()) {
                    CKYCProfileVKYCRequest ckycProfileVKYCRequest = ckycProfileVKYCRequestOptional.get();
                    ckycProfileData = ckycProfileVKYCRequest.getCkycProfileData();
                    VideoKYC videoKYCRequest = ckycvRequest.getVideoKYC();
                    CKYCResponseData ckycResponseData = formatDataService.getCKYCData(cbRequest, videoKYCRequest, ckycProfileData);
                    return ckycResponseData;
                }
            }
        }
        throw new IllegalArgumentException("Video KYC session is not initiated yet.");
    }

    @GetMapping("/api/{CBRequestId}/ckyc")
    public ResponseEntity<?> getCKYC(@RequestHeader("x-obvs-org") Long orgId, @PathVariable(value = "CBRequestId") Long cbRequestId,
                                     @CurrentUser UserPrincipal currentUser) {
        validateUser(currentUser);
        CBRequest cbRequest = cbRequestsService.getCBRequest(cbRequestId);
        CKYCResponseData ckycResponseData = getCkycResponseData(orgId, cbRequest);
        return ResponseEntity.ok(ckycResponseData);
    }
}
