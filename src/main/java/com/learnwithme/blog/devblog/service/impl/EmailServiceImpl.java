package com.learnwithme.blog.devblog.service.impl;


import com.learnwithme.blog.devblog.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service; // <-- यह एनोटेशन बहुत महत्वपूर्ण है
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service //
public class EmailServiceImpl implements EmailService {

    @Autowired
    private SesClient sesClient;

    @Value("${app.sender.email}")
    private String senderEmail;

    @Override
    public void sendSimpleMessage(String to, String subject, String textBody) {
        try {
            Destination destination = Destination.builder()
                    .toAddresses(to)
                    .build();

            Content subjectContent = Content.builder()
                    .data(subject)
                    .build();

            Content bodyContent = Content.builder()
                    .data(textBody)
                    .build();

            Body body = Body.builder()
                    .text(bodyContent)
                    .build();

            Message message = Message.builder()
                    .subject(subjectContent)
                    .body(body)
                    .build();

            SendEmailRequest request = SendEmailRequest.builder()
                    .source(senderEmail)
                    .destination(destination)
                    .message(message)
                    .build();

            sesClient.sendEmail(request);
            System.out.println("Email sent successfully to " + to);

        } catch (SesException e) {
            System.err.println("Email sending failed: " + e.awsErrorDetails().errorMessage());
        }
    }
}
