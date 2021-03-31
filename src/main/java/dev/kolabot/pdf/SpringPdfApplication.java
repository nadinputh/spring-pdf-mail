package dev.kolabot.pdf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class SpringPdfApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringPdfApplication.class, args);
    }

}
