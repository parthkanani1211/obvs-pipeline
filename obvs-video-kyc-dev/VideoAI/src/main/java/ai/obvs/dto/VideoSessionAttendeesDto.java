package ai.obvs.dto;

import ai.obvs.Enums.VideoSessionStatus;
import ai.obvs.model.VideoKYC;

import java.time.ZonedDateTime;

public class VideoSessionAttendeesDto extends VideoSessionDto {
    private AgentDto agentDto;
    private AgentDto auditorDto;

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
}
