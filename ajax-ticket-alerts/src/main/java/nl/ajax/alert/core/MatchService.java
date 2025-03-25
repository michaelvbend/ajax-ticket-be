package nl.ajax.alert.core;

import lombok.extern.slf4j.Slf4j;
import nl.ajax.alert.api.request.MatchCallbackRequest;
import nl.ajax.alert.api.MatchDTO;
import nl.ajax.alert.api.response.MatchesResponse;
import nl.ajax.alert.core.types.MatchUpdateListener;
import nl.ajax.alert.db.MatchDAO;
import nl.ajax.alert.db.models.Match;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class MatchService {
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
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
        List<Match> existingMatches = dao.findAllMatches();
        Set<MatchDTO> latestMatches = new HashSet<>(matchCallbackRequest.getMatches());

        for (Match match : existingMatches) {
            MatchDTO existingMatch = new MatchDTO(match.getHomeTeam(), match.getAwayTeam(), match.isSoldOut(), match.getMatchLink());
            if (!latestMatches.contains(existingMatch)) {
                log.info("Deleting match {}", match.getAwayTeam());
                deleteMatchAfterPlayed(match);
            }
        }
        latestMatches.forEach(this::processMatch);
    }

    public void addListener(MatchUpdateListener listener) {
        matchUpdateListeners.add(listener);
    }

    private void notifyListeners(MatchDTO updatedMatch) {
        log.info("Notify listeners for updated matches");
        for (MatchUpdateListener listener : matchUpdateListeners) {
            executorService.submit(() -> listener.onMatchUpdate(updatedMatch));
        }
    }

    private void processMatch(MatchDTO matchDTO) {
        String matchId = matchDTO.getHomeTeam() + "-" + matchDTO.getAwayTeam();
        Optional<Match> existingMatch = dao.findMatchById(matchId);

        existingMatch.ifPresentOrElse(
                match -> updateMatchIfChanged(match, matchDTO),
                () -> addNewMatch(matchId, matchDTO)
        );
        log.info("Match processed: {}", matchDTO);
    }

    private void addNewMatch(String matchId, MatchDTO matchDTO) {
        Match newMatch = new Match();
        newMatch.setId(matchId);
        newMatch.setHomeTeam(matchDTO.getHomeTeam());
        newMatch.setAwayTeam(matchDTO.getAwayTeam());
        newMatch.setSoldOut(matchDTO.isSoldOut());
        newMatch.setMatchLink(matchDTO.getMatchLink());
        newMatch.setLastModified(LocalDateTime.now());
        dao.save(newMatch);

    }

    private void updateMatchIfChanged(Match match, MatchDTO matchDTO) {
        if (match.isSoldOut() != matchDTO.isSoldOut()) {
            match.setSoldOut(matchDTO.isSoldOut());
            match.setMatchLink(matchDTO.getMatchLink());
            match.setLastModified(LocalDateTime.now());
            dao.save(match);
            notifyListeners(matchDTO);
        }
    }

    private void deleteMatchAfterPlayed(Match match) {
        dao.deleteMatch(match);
    }
}
