package ca.pjer.spring.boot.security.autoconfigure.parse;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "parse.security", name = "enabled", matchIfMissing = true)
@ComponentScan
public class ParseSecurityAutoConfiguration {
}
