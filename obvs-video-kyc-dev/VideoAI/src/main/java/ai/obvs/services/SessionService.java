package ai.obvs.services;

import ai.obvs.model.Session;

import java.util.List;
import java.util.Optional;

public interface SessionService {
    Session create(String name);
    List<Session> getAll();
    Optional<Session> getById(Long id);
}
