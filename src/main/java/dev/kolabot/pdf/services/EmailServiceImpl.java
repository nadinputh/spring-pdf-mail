package dev.kolabot.pdf.services;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.layout.font.FontProvider;
import dev.kolabot.pdf.OrderHelper;
import dev.kolabot.pdf.pojo.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender sender;
    private final ServletContext servletContext;
    private final TemplateEngine templateEngine;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@skybooking.info");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        sender.send(message);

    }

    @Override
    public void sendMessageUsingThymeleafTemplate(String to, String subject) throws MessagingException {
        /* Do Business Logic*/

        Order order = OrderHelper.getOrder();

        /* Create HTML using Thymeleaf template Engine */
        Locale locale = new Locale("km");

        WebContext context = new WebContext(request, response, servletContext, locale);
        context.setVariable("orderEntry", order);
        String orderHtml = templateEngine.process("order", context);

        /* Setup Source and target I/O streams */

        ByteArrayOutputStream target = new ByteArrayOutputStream();

        /*Setup converter properties. */
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");
        converterProperties.setCharset("UTF-8");

//        FontProvider fontProvider = new FontProvider();
//        fontProvider.addFont("static/font/Battambang-Regular.ttf");
//        fontProvider.addStandardPdfFonts();
//        fontProvider.addSystemFonts();

//        converterProperties.setFontProvider(fontProvider);

        /* Call convert method */
        HtmlConverter.convertToPdf(orderHtml, target, converterProperties);

        /* extract output as bytes */
        byte[] bytes = target.toByteArray();

        sendHtmlMessage(to, subject, orderHtml, bytes);

    }

    private void sendHtmlMessage(String to, String subject, String htmlBody, byte[] bytes) throws MessagingException {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("noreply@skybooking.info");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.addAttachment("attachment.pdf", new ByteArrayResource(bytes));
        sender.send(message);

    }

}
