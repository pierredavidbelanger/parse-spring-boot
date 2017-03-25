package ca.pjer.spring.boot.parse.example;


import ca.pjer.parseclient.Application;
import ca.pjer.parseclient.ParseClient;
import ca.pjer.parseclient.ParseUser;
import ca.pjer.parseclient.Perspective;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Main {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired(required = false)
    ParseClient parseClient;

    @Autowired(required = false)
    Application application;

    @Autowired(required = false)
    @Qualifier("anonymousPerspective")
    Perspective anonymousPerspective;

    @Autowired(required = false)
    @Qualifier("masterPerspective")
    Perspective masterPerspective;

    @PostConstruct
    void test() {
        logger.info("parseClient: {}", parseClient);
        logger.info("application: {}", application);
        logger.info("anonymousPerspective: {}", anonymousPerspective);
        logger.info("masterPerspective: {}", masterPerspective);
        if (masterPerspective != null) {
            for (ParseUser parseUser : masterPerspective.withUsers().query().find()) {
                logger.info("parseUser: {}", parseUser);
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
