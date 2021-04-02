package dev.kolabot.pdf.controller;

import dev.kolabot.pdf.request.SimpleEmailRequest;
import dev.kolabot.pdf.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final EmailService emailService;
    private final MessageSource messageSource;
    private final HttpServletRequest servletRequest;

    @PostMapping("email/simple")
    @ResponseBody
    public String sendSimpleMail(@RequestBody SimpleEmailRequest request) {
        emailService.sendSimpleMessage(request.getTo(), request.getSubject(), request.getText());

        return messageSource.getMessage("sent", null, servletRequest.getLocale());
    }

    @PostMapping("email/html")
    @ResponseBody
    public String sendHtmlMail(@RequestBody SimpleEmailRequest request) throws MessagingException {
        emailService.sendMessageUsingThymeleafTemplate(request.getTo(), request.getSubject());

        return messageSource.getMessage("sent", null, servletRequest.getLocale());
    }

}
