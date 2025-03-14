package nl.ajax.alert.core;

import nl.ajax.alert.api.MatchDTO;
import nl.ajax.alert.api.request.MatchCallbackRequest;
import nl.ajax.alert.api.response.MatchesResponse;
import nl.ajax.alert.db.MatchDAO;
import nl.ajax.alert.db.models.Match;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchServiceTest {

    private MatchDAO mockMatchDAO;
    private MatchService matchService;

    @BeforeEach
    void setup() {
        mockMatchDAO = mock(MatchDAO.class);
        matchService = new MatchService(mockMatchDAO);
    }

    @Test
    void testGetAllMatches() {
        Match match1 = new Match();
        match1.setHomeTeam("Ajax");
        match1.setAwayTeam("PSV");
        match1.setSoldOut(false);
        match1.setMatchLink("http://example.com/match1");

        Match match2 = new Match();
        match2.setHomeTeam("Ajax");
        match2.setAwayTeam("Feyenoord");
        match2.setSoldOut(true);
        match2.setMatchLink("http://example.com/match2");

        when(mockMatchDAO.findAllMatches()).thenReturn(Arrays.asList(match1, match2));

        MatchesResponse response = matchService.getAllMatches();

        assertNotNull(response);
        assertEquals(2, response.getMatches().size());
        assertEquals("Ajax", response.getMatches().getFirst().getHomeTeam());
        assertEquals("PSV", response.getMatches().getFirst().getAwayTeam());
        assertFalse(response.getMatches().getFirst().isSoldOut());
        assertEquals("http://example.com/match1", response.getMatches().getFirst().getMatchLink());

        assertEquals("Ajax", response.getMatches().get(1).getHomeTeam());
        assertEquals("Feyenoord", response.getMatches().get(1).getAwayTeam());
        assertTrue(response.getMatches().get(1).isSoldOut());
    }

    @Test
    void testSyncMatches_NewMatch() {
        MatchDTO matchDTO = new MatchDTO("Ajax", "PSV", false, "http://example.com/match");
        MatchCallbackRequest request = new MatchCallbackRequest();
        request.setMatches(List.of(matchDTO));

        when(mockMatchDAO.findMatchById("Ajax-PSV")).thenReturn(null);

        matchService.syncMatches(request);

        ArgumentCaptor<Match> matchCaptor = ArgumentCaptor.forClass(Match.class);
        verify(mockMatchDAO).save(matchCaptor.capture());

        Match savedMatch = matchCaptor.getValue();
        assertEquals("Ajax-PSV", savedMatch.getId());
        assertEquals("Ajax", savedMatch.getHomeTeam());
        assertEquals("PSV", savedMatch.getAwayTeam());
        assertFalse(savedMatch.isSoldOut());
        assertNotNull(savedMatch.getLastModified());
    }

    @Test
    void testSyncMatches_ExistingMatch() {
        MatchDTO matchDTO = new MatchDTO("Ajax", "PSV", true, "http://example.com/match");
        MatchCallbackRequest request = new MatchCallbackRequest();
        request.setMatches(List.of(matchDTO));

        Match existingMatch = new Match();
        existingMatch.setId("Ajax-PSV");
        existingMatch.setHomeTeam("Ajax");
        existingMatch.setAwayTeam("PSV");
        existingMatch.setSoldOut(false);

        when(mockMatchDAO.findMatchById("Ajax-PSV")).thenReturn(existingMatch);

        matchService.syncMatches(request);

        ArgumentCaptor<Match> matchCaptor = ArgumentCaptor.forClass(Match.class);
        verify(mockMatchDAO).save(matchCaptor.capture());

        Match savedMatch = matchCaptor.getValue();
        assertEquals("Ajax-PSV", savedMatch.getId());
        assertEquals(existingMatch.isSoldOut(), savedMatch.isSoldOut());
        assertNotNull(savedMatch.getLastModified());
    }
}