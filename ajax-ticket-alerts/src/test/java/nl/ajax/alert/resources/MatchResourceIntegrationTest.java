package nl.ajax.alert.resources;

import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.ajax.alert.AjaxAlertingApplication;
import nl.ajax.alert.AjaxAlertingConfiguration;
import nl.ajax.alert.TestAjaxAlertingApplication;
import nl.ajax.alert.api.MatchDTO;
import nl.ajax.alert.api.request.MatchCallbackRequest;
import nl.ajax.alert.api.response.MatchesResponse;
import nl.ajax.alert.client.TwilioService;
import nl.ajax.alert.db.models.Match;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(DropwizardExtensionsSupport.class)
class MatchResourceIntegrationTest {
    private static final TwilioService twilioServiceMock = Mockito.mock(TwilioService.class);

    @RegisterExtension
    static final DropwizardAppExtension<AjaxAlertingConfiguration> RULE =
            new DropwizardAppExtension<>(TestAjaxAlertingApplication.class, "test-config.yml");

    private SessionFactory sessionFactory;

    @BeforeEach
    void setup() {
        TestAjaxAlertingApplication.setTwilioServiceMock(twilioServiceMock);
        sessionFactory = ((AjaxAlertingApplication) RULE.getApplication()).getHibernateBundle().getSessionFactory();

    }

    @AfterEach
    void cleanUp() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createMutationQuery("DELETE FROM Match").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void testGetMatches() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(new Match("1", "Ajax", "PSV", false, "http://example.com/match1", LocalDateTime.now()));
            session.persist(new Match("2", "Ajax", "Feyenoord", true, "http://example.com/match2", LocalDateTime.now()));
            session.getTransaction().commit();
        }

        Response response = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/matches")
                .request()
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        MatchesResponse actualResponse = response.readEntity(MatchesResponse.class);
        assertEquals(2, actualResponse.getMatches().size());
    }

    @Test
    void testPutMatches() {
        MatchCallbackRequest matchRequest = new MatchCallbackRequest();
        matchRequest.setMatches(List.of(new MatchDTO("Ajax", "PSV", false, "http://example.com/match")));

        Response response = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/matches")
                .request()
                .put(Entity.entity(matchRequest, MediaType.APPLICATION_JSON));
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        Match savedMatch;
        try (Session session = sessionFactory.openSession()) {
            savedMatch = session.get(Match.class, "Ajax-PSV");
        }

        assertEquals("Ajax", savedMatch.getHomeTeam());
        assertEquals("PSV", savedMatch.getAwayTeam());
        assertFalse(savedMatch.isSoldOut());
    }
}