package nl.ajax.alert.resources;

import io.dropwizard.hibernate.UnitOfWork;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import nl.ajax.alert.api.request.SubscriptionRequest;
import nl.ajax.alert.core.SubscribeService;

@Path("/subscribe")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class SubscribeResource {
    private final SubscribeService subscribeService;

    public SubscribeResource(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    @POST
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveSubscriptionDetails(@NotNull @Valid SubscriptionRequest subscriptionRequest) {
        log.info("Received request to save subscription details");
        subscribeService.saveSubscriptionDetails(subscriptionRequest);
    }
}
