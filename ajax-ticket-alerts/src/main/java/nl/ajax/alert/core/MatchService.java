package nl.ajax.alert.core;

import nl.ajax.alert.api.MatchCallbackRequest;
import nl.ajax.alert.api.MatchDTO;
import nl.ajax.alert.db.MatchDAO;
import nl.ajax.alert.db.models.Match;

import java.time.LocalDateTime;
import java.util.List;

public class MatchService {
    private final MatchDAO dao;

    public MatchService(MatchDAO matchDAO) {
        this.dao = matchDAO;
    }

    public List<MatchDTO> getAllMatches() {
        List<Match> listOfMatches = dao.findAllMatches();
        return listOfMatches
                .stream()
                .map(match -> new MatchDTO(
                        match.getHomeTeam(),
                        match.getAwayTeam(),
                        match.isSoldOut(),
                "https://resale.ajax.nl/secured/content" + match.getId())).toList();
    }

    public void updateMatches(MatchCallbackRequest matchCallbackRequest) {
        for (MatchDTO match : matchCallbackRequest.getMatches()) {
            String matchId = match.getHomeTeam() + "-" + match.getAwayTeam();
            Match matchFound = dao.findMatchById(matchId);
            if (matchFound == null) {
                Match newMatch = new Match();
                newMatch.setId(matchId);
                newMatch.setHomeTeam(match.getHomeTeam());
                newMatch.setAwayTeam(match.getAwayTeam());
                newMatch.setSoldOut(match.isSoldOut());
                newMatch.setLastModified(LocalDateTime.now());
                dao.save(newMatch);
            } else {
                matchFound.setSoldOut(match.isSoldOut());
                matchFound.setLastModified(LocalDateTime.now());
                dao.save(matchFound);
            }
        }
    }

}
