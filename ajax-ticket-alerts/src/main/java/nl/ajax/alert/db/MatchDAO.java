package nl.ajax.alert.db;

import io.dropwizard.hibernate.AbstractDAO;
import nl.ajax.alert.db.models.Match;
import org.hibernate.SessionFactory;

import java.util.List;

public class MatchDAO extends AbstractDAO<Match> {

    public MatchDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Match> findAllMatches() {
        return query("FROM Match").list();
    }

    public Match findMatchById(String id) {
        return get(id);
    }

    public void save(Match match) {
        persist(match);
    }
}