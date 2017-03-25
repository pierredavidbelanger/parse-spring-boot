package ca.pjer.spring.boot.autoconfigure.parse;


import ca.pjer.parseclient.Application;
import ca.pjer.parseclient.ParseClient;
import ca.pjer.parseclient.Perspective;
import ca.pjer.parseclient.support.SimpleConfigurations;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Configuration;

@org.springframework.context.annotation.Configuration
@ConditionalOnProperty(prefix = "parse", name = "serverUri", matchIfMissing = true)
@ComponentScan
public class ParseAutoConfiguration {

    @Autowired
    ParseProperties parseProperties;

    @Bean
    ObjectMapper parseClientJaxRsClientConfigurationObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    Configuration parseClientJaxRsClientConfiguration(@Autowired
                                                      @Qualifier("parseClientJaxRsClientConfigurationObjectMapper")
                                                              ObjectMapper objectMapper) {
        return SimpleConfigurations.jerseyWithJackson(objectMapper);
    }

    @Bean(destroyMethod = "close")
    Client parseClientJaxRsClient(@Autowired
                                  @Qualifier("parseClientJaxRsClientConfiguration")
                                          Configuration configuration) {
        return ClientBuilder.newClient(configuration);
    }

    @Bean
    ParseClient parseClient(@Autowired
                            @Qualifier("parseClientJaxRsClient")
                                    Client client) {
        return ParseClient.create(client, parseProperties.getServerUri());
    }

    @Bean
    @ConditionalOnProperty(prefix = "parse", name = "applicationId")
    Application application(@Autowired ParseClient parseClient) {
        Application application = parseClient.application(parseProperties.getApplicationId());
        if (parseProperties.getRestApiKey() != null) {
            application = application.usingRestApiKey(parseProperties.getRestApiKey());
        }
        if (parseProperties.getMasterKey() != null) {
            application = application.usingMasterKey(parseProperties.getMasterKey());
        }
        return application;
    }

    @Bean
    @ConditionalOnBean(Application.class)
    Perspective anonymousPerspective(@Autowired Application application) {
        return application.asAnonymous();
    }

    @Bean
    @ConditionalOnBean(Application.class)
    @ConditionalOnProperty(prefix = "parse", name = "masterKey")
    Perspective masterPerspective(@Autowired Application application) {
        return application.asMaster();
    }
}
