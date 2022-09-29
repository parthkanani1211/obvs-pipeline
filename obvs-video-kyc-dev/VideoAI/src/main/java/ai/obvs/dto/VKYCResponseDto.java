package ai.obvs.dto;

import java.util.List;

public class VKYCResponseDto extends VideoKYCRequestBaseDto {
    private String initiatedBy;
    private CustomerDto customerDto;
//    private List<VideoSessionAttendeesDto> videoSessionAttendeesDto;
    private List<KYCDto> kycDtoList;
    private AgentDto agentDto;
    private AgentDto auditorDto;

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public CustomerDto getCustomerDto() {
        return customerDto;
    }

    public void setCustomerDto(CustomerDto customerDto) {
        this.customerDto = customerDto;
    }

//    public List<VideoSessionAttendeesDto> getVideoSessionAttendeesDto() {
//        return videoSessionAttendeesDto;
//    }
//
//    public void setVideoSessionAttendeesDto(List<VideoSessionAttendeesDto> videoSessionAttendeesDto) {
//        this.videoSessionAttendeesDto = videoSessionAttendeesDto;
//    }

    public AgentDto getAgentDto() {
        return agentDto;
    }

    public void setAgentDto(AgentDto agentDto) {
        this.agentDto = agentDto;
    }

    public AgentDto getAuditorDto() {
        return auditorDto;
    }

    public void setAuditorDto(AgentDto auditorDto) {
        this.auditorDto = auditorDto;
    }

    public List<KYCDto> getKycDtoList() {
        return kycDtoList;
    }

    public void setKycDtoList(List<KYCDto> kycDtoList) {
        this.kycDtoList = kycDtoList;
    }
}
