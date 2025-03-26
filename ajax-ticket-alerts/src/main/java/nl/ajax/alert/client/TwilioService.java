package nl.ajax.alert.client;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import lombok.extern.slf4j.Slf4j;
import nl.ajax.alert.AjaxAlertingConfiguration;
import nl.ajax.alert.api.MatchDTO;
import nl.ajax.alert.db.models.Subscription;

import java.io.IOException;
import java.util.List;

@Slf4j
public class TwilioService {
    private static final String SENDGRID_ENDPOINT = "mail/send";

    private final SendGrid sendGridClient;
    private final String twilioEmail;

    public TwilioService(AjaxAlertingConfiguration config) {
        this.twilioEmail = config.getTwilioEmail();
        this.sendGridClient = new SendGrid(config.getSendGridApiKey());
    }

    public void sendEmail(List<Subscription> subscriptionList, MatchDTO matchDTO) {
        if (subscriptionList == null || subscriptionList.isEmpty()) {
            log.warn("subscriptionList is empty. No emails sent.");
            return;
        }

        subscriptionList.forEach(subscriber -> sendSingleEmail(subscriber, matchDTO));
    }

    private void sendSingleEmail(Subscription subscriber, MatchDTO matchDTO) {
        Email from = new Email(twilioEmail);
        Mail mail = getMail(subscriber, matchDTO, from);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint(SENDGRID_ENDPOINT);
            request.setBody(mail.build());

            Response response = sendGridClient.api(request);
            log.info("Email sent to {}: Status Code: {}", subscriber.getEmail(), response.getStatusCode());
        } catch (IOException ex) {
            log.error("Failed to send email to {}: {}", subscriber.getEmail(), ex.getMessage(), ex);
        }
    }

    private static Mail getMail(Subscription subscription, MatchDTO matchDTO, Email from) {
        Email to = new Email(subscription.getEmail());
        String subject = String.format("Ajax - %s Tickets Available!", matchDTO.getAwayTeam());
        String emailBody = """
                    New Ajax tickets for the match against %s are available!
                    Get yours now: %s
                
                    If you no longer wish to receive these emails, click here to unsubscribe: https://ajaxticketsalert.com/unsubscribe?user-token=%s
                """.formatted(matchDTO.getAwayTeam(), matchDTO.getMatchLink(), subscription.getUserToken());
        Content content = new Content("text/plain", emailBody);
        return new Mail(from, subject, to, content);
    }
}
