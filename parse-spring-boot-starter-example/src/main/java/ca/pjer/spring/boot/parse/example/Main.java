package ca.pjer.spring.boot.parse.example;

import ca.pjer.parseclient.ParseSession;
import ca.pjer.parseclient.Perspective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Main {

    @RestController
    public static class MainController {

        @Autowired(required = false)
        @Qualifier("masterPerspective")
        Perspective masterPerspective;

        @GetMapping("/index")
        public Object index() {
            return masterPerspective.withObjects("Whatever").query().find();
        }

        @GetMapping("/me")
        public Object me(ParseSession parseSession) {
            return parseSession;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
