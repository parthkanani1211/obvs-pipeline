package ai.obvs.controllers;

import ai.obvs.dto.AgentDto;
import ai.obvs.mapper.AgentMapper;
import ai.obvs.model.Agent;
import ai.obvs.services.AgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@RequestMapping("/v1/agents")
public class AgentController {

    private AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

//    @PostMapping("/")
//    public ResponseEntity<?> post(@Valid @RequestBody AgentDto agentDto) {
//        AgentDto savedAgentDto = agentService.create(agentDto);
//        return ResponseEntity.ok(savedAgentDto);
//    }

    @PostMapping("/")
    public ResponseEntity<?> post(@RequestHeader("x-obvs-org") Long orgId, @Valid @RequestBody AgentDto agentDto) {
        AgentDto savedAgentDto = agentService.create(orgId, agentDto);
        return ResponseEntity.ok(savedAgentDto);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAll() {
        Set<AgentDto> agents = agentService.getAll();
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable(value="id") Long id) {
        Optional<Agent> optionalAgent = agentService.getById(id);

        if(optionalAgent.isPresent()){
            Agent agent = optionalAgent.get();
            AgentDto agentDto = AgentMapper.MAPPER.ToAgentDto(agent);
            return ResponseEntity.ok(agentDto);
        }
        return ResponseEntity.ok("Agent not found");
    }

}
