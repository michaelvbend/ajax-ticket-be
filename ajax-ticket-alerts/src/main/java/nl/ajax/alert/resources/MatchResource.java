package nl.ajax.alert.resources;

import io.dropwizard.hibernate.UnitOfWork;
import nl.ajax.alert.api.request.MatchCallbackRequest;
import nl.ajax.alert.api.response.MatchesResponse;
import nl.ajax.alert.core.MatchService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/matches")
@Produces(MediaType.APPLICATION_JSON)
public class MatchResource {

    private final MatchService matchService;

    public MatchResource(MatchService matchService) {
        this.matchService = matchService;
    }

    @GET
    @UnitOfWork
    public MatchesResponse getMatches() {
        return matchService.getAllMatches();
    }

    @PUT
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public void putMatches(@Valid MatchCallbackRequest matchRequest) {
        matchService.syncMatches(matchRequest);
    }
}
