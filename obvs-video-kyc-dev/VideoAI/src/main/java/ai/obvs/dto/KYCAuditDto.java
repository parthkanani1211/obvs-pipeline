package ai.obvs.dto;

import ai.obvs.Enums.VideoKYCRequestStatus;

import java.util.List;

public class KYCAuditDto {
    private Long id;
    private VideoKYCRequestStatus status;
    private String comment;
    private CustomerDto customerDto;
    private String customerLocation;
    private AgentDto agentDto;
    List<KYCResourcesDto> kycResourcesDtoList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerDto getCustomerDto() {
        return customerDto;
    }

    public void setCustomerDto(CustomerDto customerDto) {
        this.customerDto = customerDto;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }

    public AgentDto getAgentDto() {
        return agentDto;
    }

    public void setAgentDto(AgentDto agentDto) {
        this.agentDto = agentDto;
    }

    public List<KYCResourcesDto> getKycResourcesDtoList() {
        return kycResourcesDtoList;
    }

    public void setKycResourcesDtoList(List<KYCResourcesDto> kycResourcesDtoList) {
        this.kycResourcesDtoList = kycResourcesDtoList;
    }

    public VideoKYCRequestStatus getStatus() {
        return status;
    }

    public void setStatus(VideoKYCRequestStatus status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
