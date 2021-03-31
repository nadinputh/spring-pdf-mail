package dev.kolabot.pdf.controller;

import dev.kolabot.pdf.request.SimpleEmailRequest;
import dev.kolabot.pdf.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final EmailService emailService;

    @PostMapping("email/simple")
    public String sendSimpleMail(@RequestBody SimpleEmailRequest request) {
        emailService.sendSimpleMessage(request.getTo(), request.getSubject(), request.getText());

        return "Sent";
    }

    @PostMapping("email/html")
    public String sendHtmlMail(@RequestBody SimpleEmailRequest request) throws MessagingException {
        emailService.sendMessageUsingThymeleafTemplate(request.getTo(), request.getSubject());

        return "Sent";
    }

}
