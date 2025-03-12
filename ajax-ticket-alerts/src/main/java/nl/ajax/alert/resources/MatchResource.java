package nl.ajax.alert.resources;

import io.dropwizard.hibernate.UnitOfWork;
import nl.ajax.alert.api.MatchCallbackRequest;
import nl.ajax.alert.api.MatchDTO;
import nl.ajax.alert.core.MatchService;

import javax.validation.Valid;
import javax.ws.rs.*;
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
    @UnitOfWork
    public List<MatchDTO> getMatches() {
        return matchService.getAllMatches();
    }

    @PUT
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public void putMatches(@Valid MatchCallbackRequest matchRequest) {
        matchService.updateMatches(matchRequest);
    }
}
