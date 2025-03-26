package nl.ajax.alert.db;

import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import nl.ajax.alert.db.models.Match;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

@Slf4j
public class MatchDAO extends AbstractDAO<Match> {

    public MatchDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Match> findAllMatches() {
        return query("FROM Match").list();
    }

    public Optional<Match> findMatchById(String id) {
        return Optional.ofNullable(get(id));
    }

    public void save(Match match) {
        persist(match);
    }

    public void deleteMatch(Match match) {
        try {
            currentSession().remove(match);
        } catch (Exception e) {
            log.error("Failed to delete match: {}", match.getId(), e);
            throw e;
        }
    }
}