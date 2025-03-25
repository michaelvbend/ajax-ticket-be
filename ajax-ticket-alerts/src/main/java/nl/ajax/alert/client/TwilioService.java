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

    public void sendEmail(List<String> emailList, MatchDTO matchDTO) {
        if (emailList == null || emailList.isEmpty()) {
            log.warn("Email list is empty. No emails sent.");
            return;
        }

        emailList.forEach(email -> sendSingleEmail(email, matchDTO));
    }

    private void sendSingleEmail(String email, MatchDTO matchDTO) {
        Email from = new Email(twilioEmail);
        Mail mail = getMail(email, matchDTO, from);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint(SENDGRID_ENDPOINT);
            request.setBody(mail.build());

            Response response = sendGridClient.api(request);
            log.info("Email sent to {}: Status Code: {}", email, response.getStatusCode());
        } catch (IOException ex) {
            log.error("Failed to send email to {}: {}", email, ex.getMessage(), ex);
        }
    }

    private static Mail getMail(String email, MatchDTO matchDTO, Email from) {
        Email to = new Email(email);
        String subject = String.format("Ajax - %s Tickets Available!", matchDTO.getAwayTeam());
        String emailBody = """
                    New Ajax tickets for the match against %s are available!
                    Get yours now: %s
                
                    If you no longer wish to receive these emails, click here to unsubscribe: https://ajaxticketsalert.com/unsubscribe
                """.formatted(matchDTO.getAwayTeam(), matchDTO.getMatchLink());
        Content content = new Content("text/plain", emailBody);
        return new Mail(from, subject, to, content);
    }
}
