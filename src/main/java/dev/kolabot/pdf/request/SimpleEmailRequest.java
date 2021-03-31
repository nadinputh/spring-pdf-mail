package dev.kolabot.pdf.request;

import lombok.Data;

@Data
public class SimpleEmailRequest {

    private String to;
    private String subject;
    private String text;

}
