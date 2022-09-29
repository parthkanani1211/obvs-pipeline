package ai.obvs.services;

import ai.obvs.dto.AgentDto;
import ai.obvs.model.Agent;

import java.util.Optional;
import java.util.Set;

public interface AgentService {
    AgentDto create(AgentDto agentDto);
    AgentDto create(Long orgId, AgentDto agentDto);

    AgentDto update(Long orgId, AgentDto agentDto);

    Set<AgentDto> getAll();
    Optional<Agent> getById(Long id);
}
