package nl.ajax.alert.resources;

import nl.ajax.alert.api.Match;
import nl.ajax.alert.core.MatchService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/matches")
@Produces(MediaType.APPLICATION_JSON)
public class MatchResource {

    private final MatchService matchService;

    public MatchResource(MatchService matchService) {
        this.matchService = matchService;
    }

    @GET
    public List<Match> getMatches() {
        System.out.println("getMatches");
        return List.of();
    }
}
