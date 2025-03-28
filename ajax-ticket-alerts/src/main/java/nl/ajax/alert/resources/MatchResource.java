package nl.ajax.alert.resources;

import io.dropwizard.hibernate.UnitOfWork;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import nl.ajax.alert.api.request.MatchCallbackRequest;
import nl.ajax.alert.api.response.MatchesResponse;
import nl.ajax.alert.core.MatchService;

@Path("/matches")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class MatchResource {

    private final MatchService matchService;

    public MatchResource(MatchService matchService) {
        this.matchService = matchService;
    }

    @GET
    @UnitOfWork
    public MatchesResponse getMatches() {
        log.info("Received request to get matches");
        return matchService.getAllMatches();
    }

    @PUT
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public void putMatches(@NotNull @Valid MatchCallbackRequest matchRequest) {
        log.info("Received request to save matches");
        matchService.syncMatches(matchRequest);
    }
}
