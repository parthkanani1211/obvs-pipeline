package ai.obvs.services.impl;

import ai.obvs.model.Session;
import ai.obvs.repository.SessionRepository;
import ai.obvs.services.SessionService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class SessionServiceImpl implements SessionService {

    private SessionRepository sessionRepository;

    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Session create(String sessionName) {
        Session session = new Session();
        session.setName(sessionName);
        sessionRepository.save(session);
        return session;
    }

    @Override
    public List<Session> getAll() {
        List<Session> sessionList = sessionRepository.findAll();
        return sessionList;
    }

    @Override
    public Optional<Session> getById(Long id) {
        Optional<Session> optionalSession = sessionRepository.findById(id);
        if (optionalSession.isPresent()) {
            Session session = optionalSession.get();
            return Optional.of(session);
        }
        return  Optional.empty();
    }

}
