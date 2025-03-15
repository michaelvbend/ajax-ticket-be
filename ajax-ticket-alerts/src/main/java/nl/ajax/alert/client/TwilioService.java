package nl.ajax.alert.client;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class TwilioService {
    private static final String SMS_MESSAGE = "New Ajax tickets are available!";

    public TwilioService(String accountSid, String authToken) {
            Twilio.init(accountSid, authToken);
    }

    public void sendMessage(List<String> phoneNumList) {
        if (phoneNumList == null || phoneNumList.isEmpty()) {
            return;
        }

        phoneNumList.forEach(phoneNum -> {
            try {
                Message.creator(
                                new PhoneNumber(phoneNum),
                                new PhoneNumber("+16502821571"),
                                SMS_MESSAGE)
                        .create();
                log.info("Twilio SMS sent successfully!");
            } catch (Exception e) {
                log.error("Failed to send SMS: {}", e.getMessage(), e);
            }
        });
    }

    public void sendEmail(List<String> emailList) {
        Email from = new Email("michael@ajaxticketsalert.com");
        String subject = "Ajax Tickets Available!";
        Content content = new Content("text/plain", "New Ajax tickets are available! Get yours now.");
        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));

        emailList.forEach(email -> {
            Email to = new Email(email);
            Mail mail = new Mail(from, subject, to, content);
            Request request = new Request();
            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sg.api(request);
                log.info("Email sent to {}: Status Code: {}", email, response.getStatusCode());
            } catch (IOException ex) {
                log.error("Failed to send email to {}: {}", email, ex.getMessage());
            }
        });
    }
}
