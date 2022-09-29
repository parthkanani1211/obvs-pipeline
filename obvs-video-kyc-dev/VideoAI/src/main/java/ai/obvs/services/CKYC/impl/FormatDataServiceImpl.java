package ai.obvs.services.CKYC.impl;

import ai.obvs.Enums.KYCResourceType;
import ai.obvs.Enums.WorkflowStepType;
import ai.obvs.dto.Aadhar.AadharVerificationData;
import ai.obvs.dto.Aadhar.Address;
import ai.obvs.dto.CKYC.AddressFields;
import ai.obvs.dto.CKYC.CustomerProfileDto;
import ai.obvs.dto.CKYC.NameFields;
import ai.obvs.dto.KYCVerificationData;
import ai.obvs.mapper.CKYCDataMapper;
import ai.obvs.model.*;
import ai.obvs.services.*;
import ai.obvs.services.CKYC.CKYCDataService;
import ai.obvs.services.CKYC.FormatDataService;
import ai.obvs.services.CKYC.MasterDataService;
import ai.obvs.services.CKYC.model.CKYCResponseData;
import ai.obvs.services.CKYC.model.DocDetail;
import ai.obvs.services.CKYC.model.RequestData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Transactional
public class FormatDataServiceImpl implements FormatDataService {

    @Autowired
    private CBRequestsService cbRequestsService;

    @Autowired
    private CKYCDataService ckycDataService;

    @Autowired
    private KYCService kycService;

    @Autowired
    private KYCDataService kycDataService;

    @Autowired
    private MasterDataService masterDataService;

    @Autowired
    private PANCardDataExtractionService panCardDataExtractionService;

    @Autowired
    private AadharCardDataVerificationService aadharCardDataVerificationService;

    @Override
    public CKYCResponseData getCKYCData(CBRequest cbRequest, VideoKYC videoKYC, CKYCProfileData ckycProfileData) {
        CKYCResponseData ckycResponseData = new CKYCResponseData();
        ckycResponseData.setRef_id(cbRequest.getRefId());
        ckycResponseData.setAction("POST_VCIP_DATA");
        RequestData requestData = new RequestData();

        Customer customer = videoKYC.getCustomer();
        Agent agent = videoKYC.getAgent();
        Agent auditor = videoKYC.getAuditor();
        List<VideoSession> videoSessions = videoKYC.getVideoSessions();
        if (videoSessions.size() == 0) {
            throw new IllegalArgumentException("CKYC data doesn't exits for the customer. Please schedule Video KYC session first.");
        }

        VideoSession videoSession = videoSessions.get(videoSessions.size() - 1);
        List<KYC> kycList = kycService.getKYCByVideoSessionId(videoSession.getId());
        List<DocDetail> docDetails = new ArrayList<>();
        for (KYC kyc : kycList) {
            WorkflowStepType workflowStepType = kyc.getWorkflowStep().getWorkflowStepType();
            switch (workflowStepType) {
                case DOCUMENT:
                    switch (kyc.getDocument().getName()) {
                        case PAN:
                            KYCVerificationData kycVerificationData = new KYCVerificationData();
                            try {
                                kycVerificationData = new ObjectMapper().readValue(kyc.getKYCVerificationData(), KYCVerificationData.class);
                            } catch (JsonProcessingException e) {
                            }
                            requestData.setPan_no(kycVerificationData.getMatchResults().get(0).getMatchValue());

                            byte[] content = kycService.getContent(kyc.getId(), KYCResourceType.PAN);
                            String encodedString = Base64.getEncoder().encodeToString(content);
                            DocDetail docDetail = new DocDetail();
                            docDetail.setDoc_no("1");
                            docDetail.setDoc_name(KYCResourceType.PAN.getValue());
                            docDetail.setDoc_img(encodedString);
                            docDetails.add(docDetail);
                            break;
                        case AADHAR:
                            kycVerificationData = new KYCVerificationData();
                            try {
                                kycVerificationData = new ObjectMapper().readValue(kyc.getKYCVerificationData(), KYCVerificationData.class);
                            } catch (JsonProcessingException e) {
                            }
                            requestData.setUidi(kycVerificationData.getMatchResults().get(0).getValue());
                            requestData.setCus_nm_as_per_uidi(kycVerificationData.getMatchResults().get(1).getMatchValue());
                            requestData.setGender(kycVerificationData.getMatchResults().get(2).getMatchValue());
                            requestData.setDate_of_birth(formatDate(kycVerificationData.getMatchResults().get(3).getMatchValue(), "yyyy-MM-dd"));

                            AddressFields addressFields = new AddressFields();
                            Optional<KYCData> kycDataOptional = kycDataService.getKYCData(kyc);
                            if (kycDataOptional.isPresent()) {
                                String verificationData = kycDataOptional.get().getVerificationData();
                                try {
                                    JSONObject responseJsonObject = (JSONObject) new JSONParser().parse(verificationData);
                                    int statusCode = Integer.valueOf(responseJsonObject.get("status_code").toString());
                                    if (statusCode == 200) {
                                        Object jsonData = responseJsonObject.get("data");
                                        if (jsonData != null) {
                                            String data = jsonData.toString();
                                            AadharVerificationData aadharVerificationData = new ObjectMapper().readValue(data, AadharVerificationData.class);
                                            Address address = aadharVerificationData.getAddress();
                                            addressFields.setCounty(address.getCountry());
                                            addressFields.setState(address.getState());
                                            addressFields.setDistrict(address.getDist());
                                            addressFields.setCity(address.getVtc() + ", " + address.getSubdist());
                                            addressFields.setPincode(aadharVerificationData.getZip());
                                            addressFields.setStreetAddress(address.getHouse() + ", " + address.getStreet() + ", " + address.getLandmark());
                                            addressFields.setArea(address.getLoc() + ", " + address.getPo());
                                        }
                                    }

                                } catch (JsonProcessingException | ParseException e) {
                                }
                            }
                            requestData.setPermanent_add(addressFields);

                            String aadharFaceImageData = aadharCardDataVerificationService.getFaceImageData(kyc);
                            requestData.setCust_photo(aadharFaceImageData);

                            content = kycService.getContent(kyc.getId(), KYCResourceType.AADHAR_FRONT);
                            String aadharFrontEncodedString = Base64.getEncoder().encodeToString(content);
                            docDetail = new DocDetail();
                            docDetail.setDoc_no("2");
                            docDetail.setDoc_name(KYCResourceType.AADHAR_FRONT.getValue());
                            docDetail.setDoc_img(aadharFrontEncodedString);
                            docDetails.add(docDetail);

                            content = kycService.getContent(kyc.getId(), KYCResourceType.AADHAR_BACK);
                            String aadharBackEncodedString = Base64.getEncoder().encodeToString(content);
                            docDetail = new DocDetail();
                            docDetail.setDoc_no("3");
                            docDetail.setDoc_name(KYCResourceType.AADHAR_BACK.getValue());
                            docDetail.setDoc_img(aadharBackEncodedString);
                            docDetails.add(docDetail);

                            requestData.setProf_of_address_doc_img(aadharBackEncodedString);

                            break;
                    }
                    break;
                case OTHER:
                    switch (ai.obvs.Enums.WorkflowStep.valueOf(kyc.getWorkflowStep().getWorkflowStep().getName())) {
                        case FACE:
                            byte[] content = kycService.getContent(kyc.getId(), KYCResourceType.FACE);
                            String faceEncodedString = Base64.getEncoder().encodeToString(content);
                            requestData.setCust_selfie(faceEncodedString);
                            break;
                        case SIGNATURE:
                            content = kycService.getContent(kyc.getId(), KYCResourceType.SIGN);
                            String signatureEncodedString = Base64.getEncoder().encodeToString(content);
                            requestData.setCust_sign(signatureEncodedString);
                            break;
                    }
                    break;

            }
        }

        CustomerProfileDto customerProfileDto = CKYCDataMapper.MAPPER.ToCKYCProfileDto(ckycProfileData);

        NameFields customerName = new NameFields();
        String prefixCode = ckycProfileData.getCustomerName().getPrefix();
        String namePrefix = masterDataService.getNamePrefix(prefixCode);
        customerName.setPrefix(namePrefix);
        customerName.setFirstName(customer.getFirstName());
        customerName.setLastName(customer.getLastName());
        requestData.setCust_nm(customerName);


        if (customerProfileDto.getFatherName() != null) {
            requestData.setFather_nm(customerProfileDto.getFatherName());
            prefixCode = customerProfileDto.getFatherName().getPrefix();
            requestData.getFather_nm().setPrefix(masterDataService.getNamePrefix(prefixCode));
        }
        if (customerProfileDto.getMotherName() != null) {
            requestData.setMother_nm(customerProfileDto.getMotherName());
            prefixCode = customerProfileDto.getMotherName().getPrefix();
            requestData.getMother_nm().setPrefix(masterDataService.getNamePrefix(prefixCode));
        }
        String maritalStatusCode = ckycProfileData.getMaritalStatus();
//            String maritalStatus = masterDataService.getMaritalStatus(maritalStatusCode);
        requestData.setMarital_status(maritalStatusCode);
        requestData.setNationality(ckycProfileData.getNationality());

        String communityCode = ckycProfileData.getCommunity();
//        String community = masterDataService.getCommunity(communityCode);
        requestData.setCommunity(communityCode);

        String residencyCode = ckycProfileData.getResidency();
//        String residencyStatus = masterDataService.getResidencyStatus(residencyCode);
        requestData.setResidency_status(residencyCode);

        String constitutionCode = ckycProfileData.getConstitution();
//        String constitution = masterDataService.getConstitution(constitutionCode);
        requestData.setConstitution(constitutionCode);

        String occupationCode = ckycProfileData.getOccupation();
//        String occupation = masterDataService.getOccupation(occupationCode);
        requestData.setOccupation(occupationCode);

        requestData.setSame_as_permanent_add(String.valueOf(ckycProfileData.isLocalAddressSameAsPermanent()));
        requestData.setLocal_add(customerProfileDto.getAddress());

        if (agent != null) {
            requestData.setMkr_user_id(agent.getId().toString());
            requestData.setMkr_entered_dt(formatDate(videoSession.getStartTime()));
        }
        if (auditor != null) {
            requestData.setCkr_user_id(auditor.getId().toString());
            requestData.setCkr_verify_dt(formatDate(videoKYC.getUpdatedOn()));
        }

        String auditStatus = videoKYC.getVideoKYCRequestStatus().name();
        requestData.setStatus(auditStatus);

        String comment = videoKYC.getComment();
        requestData.setReject_reason(comment);

        requestData.setDoc_dtl(docDetails);

        ckycResponseData.setRequest_data(requestData);
        return ckycResponseData;
    }

    private String formatDate(ZonedDateTime zonedDateTime) {
        String dateFormat = "dd/MM/yyyy HH:mm:ss";
        return formatDate(zonedDateTime, dateFormat);
    }

    private String formatDate(String dateValue, String currentFormat) {
        try {
            Date date = new SimpleDateFormat(currentFormat).parse(dateValue);
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("Asia/Kolkata"));
            return formatDate(zonedDateTime, "dd/MM/yyyy");
        } catch (Exception ex) {

        }
        return dateValue;
    }

    private String formatDate(ZonedDateTime zonedDateTime, String dateFormat) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
//            String dateTimeString = zonedDateTime.toLocalDateTime().format(formatter);
            ZonedDateTime convertZonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
            String dateTimeString = formatter.format(convertZonedDateTime);
            String formatedString = convertZonedDateTime.toLocalDateTime().format(formatter);


            return dateTimeString;
        } catch (Exception ex) {
        }
        return zonedDateTime.toLocalDateTime().toString();
    }
}
