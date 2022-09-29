package ai.obvs.config;

import ai.obvs.controllers.*;
import ai.obvs.repository.*;
import ai.obvs.security.JwtTokenProvider;
import ai.obvs.security.SmsAuthenticationProvider;
import ai.obvs.security.UserDetailService;
import ai.obvs.services.*;
import ai.obvs.services.CKYC.CKYCDataService;
import ai.obvs.services.CKYC.CKYCProfileVideoKYCService;
import ai.obvs.services.CKYC.FormatDataService;
import ai.obvs.services.CKYC.MasterDataService;
import ai.obvs.services.CKYC.impl.CKYCDataServiceImpl;
import ai.obvs.services.CKYC.impl.CKYCProfileVideoKYCServiceImpl;
import ai.obvs.services.CKYC.impl.FormatDataServiceImpl;
import ai.obvs.services.CKYC.impl.MasterDataServiceImpl;
import ai.obvs.services.impl.*;
import ai.obvs.services.notification.NotificationService;
import ai.obvs.services.notification.PropertiesData;
import ai.obvs.services.notification.SMSService;
import ai.obvs.services.notification.impl.NotificationServiceImpl;
import ai.obvs.services.notification.impl.TwilioSMSServiceImpl;
import ai.obvs.services.ocr.ExtractDataService;
import ai.obvs.services.ocr.GoogleVisionOcrEngine;
import ai.obvs.services.ocr.OCRProperties;
import ai.obvs.services.ocr.OcrEngine;
import liquibase.pro.packaged.B;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Properties;

@Configuration
public class BeansConfig {

    @Bean
    public CustomerProfileController getCustomerProfileController() {
        return new CustomerProfileController();
    }

    @Bean
    public CKYCDataService getCKYCDataService() {
        return new CKYCDataServiceImpl();
    }

    @Bean
    public NotificationService getNotificationService(){
        return new NotificationServiceImpl();
    }

    @Bean
    public PropertiesData getPropertiesData(){
        return new PropertiesData();
    }

    @Bean
    public MasterDataService getMasterDataService() {
        return new MasterDataServiceImpl();
    }

    @Bean
    public CBRequestsService getCBRequestsService() {
        return new CBRequestsServiceImpl();
    }

    @Bean
    public FormatDataService getFormatDataService() {
        return new FormatDataServiceImpl();
    }

    @Bean
    public CKYCProfileVideoKYCService getCKYCProfileVideoKYCService() {
        return new CKYCProfileVideoKYCServiceImpl();
    }

    @Bean
    public VideoKYCAPIsController getVideoKYCAPIController() {
        return new VideoKYCAPIsController();
    }

    @Bean
    public OpenViduController getOpenViduController(OpenViduService openViduService) {
        return new OpenViduController(openViduService);
    }

    @Bean
    public OpenViduService getOpenViduService() {
        return new OpenViduService();
    }

    @Bean
    public CustomerController getCustomerController(CustomerService customerService) {
        return new CustomerController(customerService);
    }

    @Bean
    public CustomerService getCustomerService(CustomerRepository customerRepository, AppRepository appRepository, UserService userService, RoleService roleService, OrgService orgService) {
        return new CustomerServiceImpl(customerRepository, appRepository, userService, roleService, orgService);
    }

    @Bean
    public AgentController getAgentController(AgentService agentService) {
        return new AgentController(agentService);
    }

    @Bean
    public AgentService getAgentService(AgentRepository agentRepository, UserService userService, RoleService roleService, OrgService orgService) {
        return new AgentServiceImpl(agentRepository, userService, roleService, orgService);
    }

    @Bean
    public AppController getAppController(AppService appService) {
        return new AppController(appService);
    }

    @Bean
    public AppService getAppService(AppRepository appRepository) {
        return new AppServiceImpl(appRepository);
    }

    @Bean
    public VideoKYCRequestsController getVideoKYCRequestsController(VideoKYCRequestsService videoKYCRequestsService, KYCService kycService,
                                                                    FileStorageService fileStorageService, SMSService smsService,
                                                                    VideoKYCRequestsRecordingService videoKYCRequestsRecordingService) {
        return new VideoKYCRequestsController(videoKYCRequestsService, kycService, fileStorageService, smsService, videoKYCRequestsRecordingService);
    }

    @Bean
    public VideoKYCRequestsService getVideoKYCRequestsService(VideoSessionService videoSessionService, VideoParticipantService videoParticipantService, KYCService kycService,
                                                              VideoKYCRepository videoKYCRepository, UserService userService, CustomerService customerService, AgentService agentService,
                                                              AuditRepository auditRepository, GeoLocationService geoLocationService, OrgService orgService) {
        return new VideoKYCRequestsServiceImpl(videoSessionService, videoParticipantService, kycService, videoKYCRepository, userService, customerService, agentService,
                auditRepository, geoLocationService, orgService);
    }

    @Bean
    public VideoSessionService getVideoSessionService(VideoSessionRepository videoSessionRepository, VideoParticipantService videoParticipantService) {
        return new VideoSessionServiceImpl(videoSessionRepository, videoParticipantService);
    }

    @Bean
    public VideoKYCRequestsRecordingService getVideoKYCRequestsRecordingService(OpenViduService openViduService, RESTService restService) {
        return new VideoKYCRequestsRecordingServiceImpl(openViduService, restService);
    }

    @Bean
    public VideoParticipantService getVideoParticipantService(VideoParticipantRepository videoParticipantRepository, LocationRepository locationRepository) {
        return new VideoParticipantServiceImpl(videoParticipantRepository, locationRepository);
    }

    @Bean
    public PermissionController getPermissionsController(PermissionService permissionService) {
        return new PermissionController(permissionService);
    }

    @Bean
    public PermissionService getPermissionService(PermissionRepository permissionRepository) {
        return new PermissionServiceImpl(permissionRepository);
    }

    @Bean
    public AuthenticationController getAuthenticationController(AuthenticationService authenticationService) {
        return new AuthenticationController(authenticationService);
    }

    @Bean
    public AuthenticationService getAuthenticationService(UserService userService, AuthenticationManager authenticationManager,
                                                          JwtTokenProvider jwtTokenProvider, OTPService otpService,
                                                          CustomerService customerService) {
        return new AuthenticationServiceImpl(userService, authenticationManager, jwtTokenProvider, otpService, customerService);
    }

    @Bean
    public JwtTokenProvider getJWTTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public OTPService getOTPService() {
        return new OTPService();
    }

    @Bean
    public SMSService getSMSService() {
        return new TwilioSMSServiceImpl();
    }

    @Bean
    public KYCController getKYCController(KYCService kycService) {
        return new KYCController(kycService);
    }

    @Bean
    public WorkflowController getWorkflowController(WorkflowService workflowService, DocumentService documentService) {
        return new WorkflowController(workflowService, documentService);
    }

    @Bean
    public WorkflowService getWorkflowService(OrgService orgService, WorkflowRepository workflowRepository, WorkflowStepRepository workflowStepRepository,
                                              WorkflowStepDocumentsRepository workflowStepDocumentsRepository, DocumentService documentService) {
        return new WorkflowServiceImpl(orgService, workflowRepository, workflowStepRepository, workflowStepDocumentsRepository, documentService);
    }

    @Bean
    public DocumentService getDocumentService(DocumentRepository documentRepository, OrgService orgService) {
        return new DocumentServiceImpl(documentRepository, orgService);
    }

    @Bean
    public KYCService getKYCService(KYCRepository kycRepository,
                                    ResourceRepository resourceRepository, CustomerService customerService,
                                    PANCardService panCardService, AadharCardService aadharCardService,
                                    FaceRecognitionService faceRecognitionService, SignatureService signatureService,
                                    WorkflowService workflowService, DocumentService documentService) {
        return new KYCServiceImpl(kycRepository, resourceRepository, customerService, panCardService, aadharCardService,
                faceRecognitionService, signatureService, workflowService, documentService);
    }

    @Bean
    public PANCardService getPanCardService(PANCardDataExtractionService panCardDataExtractionService, PANCardDataVerificationService panCardDataVerificationService) {
        return new PANCardService(panCardDataExtractionService, panCardDataVerificationService);
    }

    @Bean
    public AadharCardService getAadharCardService(AadharCardDataExtractionService aadharCardDataExtractionService, AadharCardDataVerificationService aadharCardDataVerificationService,
                                                  AadharVerificationCachingService AadharVerificationCachingService) {
        return new AadharCardService(aadharCardDataExtractionService, aadharCardDataVerificationService, AadharVerificationCachingService);
    }

    @Bean
    public AadharVerificationCachingService getAadharVerificationCachingService() {
        return new AadharVerificationCachingService();
    }

    @Bean
    public KYCDataService getKYCDataService(KYCDataRepository kycDataRepository) {
        return new KYCDataServiceImpl(kycDataRepository);
    }

    @Bean
    public FaceRecognitionService getFaceRecognitionService(BioIdVerificationService bioIdVerificationService) {
        return new FaceRecognitionService(bioIdVerificationService);
    }

    @Bean
    public BioIdVerificationService getBioVerificationService(RESTService restService) {
        return new BioIdVerificationServiceImpl(restService);
    }

    @Bean
    public SignatureService getSignatureService() {
        return new SignatureService();
    }

    @Bean
    public PANCardDataExtractionService getPanCardDataExtractionService(KYCDataExtractionService kycDataExtractionService, KYCDataService kycDataService) {
        return new PANCardDataExtractionService(kycDataExtractionService, kycDataService);
    }

    @Bean
    public PANCardDataVerificationService getPanCardVerificationService(KYCDataVerificationService kycDataVerificationService, KYCDataService kycDataService) {
        return new PANCardDataVerificationService(kycDataVerificationService, kycDataService);
    }

    @Bean
    public AadharCardDataExtractionService getAadharCardDataExtractionService(KYCDataExtractionService kycDataExtractionService, KYCDataService kycDataService) {
        return new AadharCardDataExtractionService(kycDataExtractionService, kycDataService);
    }

    @Bean
    public AadharCardDataVerificationService getAadharCardVerificationService(KYCDataVerificationService kycDataVerificationService, KYCDataService kycDataService) {
        return new AadharCardDataVerificationService(kycDataVerificationService, kycDataService);
    }


    @Bean
    public KYCDataExtractionService getDataExtractionService(RESTService restService) {
        return new KYCDataExtractionServiceImpl(restService);
    }

    @Bean
    public KYCDataVerificationService getDataVerificationService(RESTService restService) {
        return new AADHARAPIKYCDataVerificationServiceImpl(restService);
    }

    @Bean
    RESTService getRestService() {
        return new ApacheHttpClientRESTServiceImpl();
    }

    @Bean
    public UserDetailService getUserDetailService(UserRepository userRepository) {
        return new UserDetailService(userRepository);
    }

    @Bean
    public SmsAuthenticationProvider getSmsAuthenticationProvider(UserDetailService userDetailService) {
        return new SmsAuthenticationProvider(userDetailService);
    }

    @Bean
    public FileStorageService getFileStorageService() {
        return new FileStorageService();
    }

    @Bean
    public AuditController getAuditController(VideoKYCRequestsService videoKYCRequestsService) {
        return new AuditController(videoKYCRequestsService);
    }

    @Bean
    public AuditService getAuditService(VideoKYCRequestsService videoKYCRequestsService, AuditRepository auditRepository) {
        return new AuditServiceImpl(videoKYCRequestsService, auditRepository);
    }

    @Bean
    public GeoLocationService getGeoLocationService() {
        return new GeoLocationServiceImpl();
    }

    @Bean
    public OrganizationController getOrganizationController(OrgService orgService) {
        return new OrganizationController(orgService);
    }

    @Bean
    public OrgService getOrgService(OrgRepository orgRepository) {
        return new OrgServiceImpl(orgRepository);
    }

    @Bean
    public EmailService getEmailService(JavaMailSender emailSender) {
        return new EmailServiceImpl(emailSender);
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("my.gmail@gmail.com");
        mailSender.setPassword("password");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    public OCRController getOCRController() {
        return new OCRController();
    }

    @Bean
    public OcrEngine getGoogleVisionOcrEngine() {
        return new GoogleVisionOcrEngine();
    }

    @Bean
    public ExtractDataService getExtractDataService() {
        return new ExtractDataService();
    }

    @Bean
    public OCRProperties getOCRProperties() {
        return new OCRProperties();
    }
}
