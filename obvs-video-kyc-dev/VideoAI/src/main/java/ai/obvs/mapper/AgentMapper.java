package ai.obvs.mapper;

import ai.obvs.dto.AgentDto;
import ai.obvs.model.Agent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AgentMapper {
    AgentMapper MAPPER = Mappers.getMapper(AgentMapper.class);

    Agent ToAgent(AgentDto agentDto);
    AgentDto ToAgentDto(Agent agent);
}
