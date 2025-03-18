package nl.ajax.alert.client;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.ASM;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import io.dropwizard.lifecycle.Managed;
import lombok.extern.slf4j.Slf4j;
import nl.ajax.alert.AjaxAlertingConfiguration;
import nl.ajax.alert.api.MatchDTO;
import nl.ajax.alert.db.models.Match;

import java.io.IOException;
import java.util.List;

@Slf4j
public class TwilioService {
    private static final String SMS_MESSAGE = "New Ajax tickets are available!";
    private static final String SENDGRID_ENDPOINT = "mail/send";

    private final SendGrid sendGridClient;
    private final String twilioPhoneNumber;
    private final String twilioEmail;

    public TwilioService(AjaxAlertingConfiguration config) {
//            Twilio.init(config.getTwilioAccountSid(), config.getTwilioAuthToken()); // Only for SMS required
        this.twilioPhoneNumber = config.getTwilioPhoneNumber();
        this.twilioEmail = config.getTwilioEmail();
        this.sendGridClient = new SendGrid(config.getSendGridApiKey());
    }

    public void sendMessage(List<String> phoneNumList) {
        if (phoneNumList == null || phoneNumList.isEmpty()) {
            log.warn("Phone number list is empty. No SMS sent.");
            return;
        }
        phoneNumList.forEach(this::sendSms);
    }

    private void sendSms(String phoneNum) {
        try {
            Message.creator(new PhoneNumber(phoneNum), new PhoneNumber(twilioPhoneNumber), SMS_MESSAGE)
                    .create();
            log.info("Twilio SMS sent successfully to {}", phoneNum);
        } catch (Exception e) {
            log.error("Failed to send SMS to {}: {}", phoneNum, e.getMessage(), e);
        }
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
        Email to = new Email(email);
        String subject = String.format("Ajax - %s Tickets Available!", matchDTO.getAwayTeam());
        String emailBody = """
                    New Ajax tickets for the match against %s are available!
                    Get yours now: %s
                
                    If you no longer wish to receive these emails, click here to unsubscribe: <%%asm_group_unsubscribe_raw_url%%>
                """.formatted(matchDTO.getAwayTeam(), matchDTO.getMatchLink());
        Content content = new Content("text/plain", emailBody);
        Mail mail = new Mail(from, subject, to, content);
        ASM asm = new ASM();
        asm.setGroupId(232083);
        mail.setASM(asm);
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
}
