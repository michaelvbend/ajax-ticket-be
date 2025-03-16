package nl.ajax.alert.core;

import lombok.extern.slf4j.Slf4j;
import nl.ajax.alert.api.request.MatchCallbackRequest;
import nl.ajax.alert.api.MatchDTO;
import nl.ajax.alert.api.response.MatchesResponse;
import nl.ajax.alert.core.types.MatchUpdateListener;
import nl.ajax.alert.db.MatchDAO;
import nl.ajax.alert.db.models.Match;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class MatchService {
    private final List<MatchUpdateListener> matchUpdateListeners = new ArrayList<>();
    private final MatchDAO dao;

    public MatchService(MatchDAO matchDAO) {
        this.dao = matchDAO;
    }

    public MatchesResponse getAllMatches() {
        List<MatchDTO> matchDTOs = dao.findAllMatches().stream()
                .map(match -> new MatchDTO(
                        match.getHomeTeam(),
                        match.getAwayTeam(),
                        match.isSoldOut(),
                        match.getMatchLink()
                ))
                .toList();
        return new MatchesResponse(matchDTOs);
    }

    public void syncMatches(MatchCallbackRequest matchCallbackRequest) {
        matchCallbackRequest.getMatches().forEach(this::processMatch);
    }

    public void addListener(MatchUpdateListener listener) {
        matchUpdateListeners.add(listener);
    }

    private void notifyListeners(MatchDTO updatedMatch) {
        log.info("Notify listeners for updated matches");
        for (MatchUpdateListener listener : matchUpdateListeners) {
            new Thread(() -> listener.onMatchUpdate(updatedMatch)).start();
        }
    }

    private void processMatch(MatchDTO matchDTO) {
        String matchId = matchDTO.getHomeTeam() + "-" + matchDTO.getAwayTeam();
        Optional<Match> existingMatch = dao.findMatchById(matchId);

        if (existingMatch.isPresent()) {
            updateMatchIfChanged(existingMatch.get(), matchDTO);
        } else {
            addNewMatch(matchId, matchDTO);
        }
        log.info("Match processed: {}", matchDTO);
    }

    private void addNewMatch(String matchId, MatchDTO matchDTO) {
        Match newMatch = new Match();
        newMatch.setId(matchId);
        newMatch.setHomeTeam(matchDTO.getHomeTeam());
        newMatch.setAwayTeam(matchDTO.getAwayTeam());
        newMatch.setSoldOut(matchDTO.isSoldOut());
        newMatch.setLastModified(LocalDateTime.now());
        dao.save(newMatch);

    }

    private void updateMatchIfChanged(Match match, MatchDTO matchDTO) {
        if (match.isSoldOut() != matchDTO.isSoldOut()) {
            match.setSoldOut(matchDTO.isSoldOut());
            match.setLastModified(LocalDateTime.now());
            dao.save(match);
            notifyListeners(matchDTO);
        }
    }
}
