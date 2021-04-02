package dev.kolabot.pdf.controller;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.source.ByteArrayOutputStream;
import dev.kolabot.pdf.OrderHelper;
import dev.kolabot.pdf.pojo.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class PDFController {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private TemplateEngine templateEngine;

    @RequestMapping(path = "/")
    public String getOrderPage(Model model) {
        Order order = OrderHelper.getOrder();
        model.addAttribute("orderEntry", order);
        return "invoice";
    }

    @RequestMapping(path = "/pdf")
    public ResponseEntity<?> getPDF(HttpServletRequest request, HttpServletResponse response) {

        /* Do Business Logic*/

        Order order = OrderHelper.getOrder();

        /* Create HTML using Thymeleaf template Engine */

        WebContext context = new WebContext(request, response, servletContext, request.getLocale());
        context.setVariable("orderEntry", order);
        String orderHtml = templateEngine.process("invoice", context);

        /* Setup Source and target I/O streams */

        ByteArrayOutputStream target = new ByteArrayOutputStream();

        /*Setup converter properties. */
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");
        converterProperties.setCharset("UTF-8");

        /* Call convert method */
        HtmlConverter.convertToPdf(orderHtml, target, converterProperties);

        /* extract output as bytes */
        byte[] bytes = target.toByteArray();


        /* Send the response as downloadable PDF */

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .body(bytes);

    }

}
