package ca.mikegabelmann.judgement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class JudgementApplication {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(JudgementApplication.class);


    /**
     * Entry point to start application.
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        //change default application property name, useful for deploying to application container, instead of standalone
        System.setProperty("spring.config.name", "application");

        SpringApplication.run(JudgementApplication.class, args);
    }

}
