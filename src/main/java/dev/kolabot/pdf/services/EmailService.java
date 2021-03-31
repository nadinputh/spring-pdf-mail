package dev.kolabot.pdf.services;

import javax.mail.MessagingException;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);

    void sendMessageUsingThymeleafTemplate(String to, String subject) throws MessagingException;

}
