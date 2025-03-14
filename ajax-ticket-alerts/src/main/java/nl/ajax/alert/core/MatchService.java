package nl.ajax.alert.core;

import nl.ajax.alert.api.request.MatchCallbackRequest;
import nl.ajax.alert.api.MatchDTO;
import nl.ajax.alert.api.response.MatchesResponse;
import nl.ajax.alert.db.MatchDAO;
import nl.ajax.alert.db.models.Match;

import java.time.LocalDateTime;
import java.util.List;

public class MatchService {
    private final MatchDAO dao;

    public MatchService(MatchDAO matchDAO) {
        this.dao = matchDAO;
    }

    public MatchesResponse getAllMatches() {
        List<Match> listOfMatches = dao.findAllMatches();
        return new MatchesResponse(listOfMatches
                .stream()
                .map(match -> new MatchDTO(
                        match.getHomeTeam(),
                        match.getAwayTeam(),
                        match.isSoldOut(),
                        match.getMatchLink()))
                .toList());
    }

    public void syncMatches(MatchCallbackRequest matchCallbackRequest) {
        for (MatchDTO match : matchCallbackRequest.getMatches()) {
            String matchId = match.getHomeTeam() + "-" + match.getAwayTeam();
            Match matchFound = dao.findMatchById(matchId);
            if (matchFound == null) {
                addNewMatch(matchId, match);
            } else {
               updateMatch(matchFound);
            }
        }
    }

    private void addNewMatch(String matchId, MatchDTO match) {
        Match newMatch = new Match();
        newMatch.setId(matchId);
        newMatch.setHomeTeam(match.getHomeTeam());
        newMatch.setAwayTeam(match.getAwayTeam());
        newMatch.setSoldOut(match.isSoldOut());
        newMatch.setLastModified(LocalDateTime.now());
        dao.save(newMatch);
    }

    private void updateMatch(Match match) {
        match.setSoldOut(match.isSoldOut());
        match.setLastModified(LocalDateTime.now());
        dao.save(match);
    }
}
