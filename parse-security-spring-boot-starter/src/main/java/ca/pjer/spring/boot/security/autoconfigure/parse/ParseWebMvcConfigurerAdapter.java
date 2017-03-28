package ca.pjer.spring.boot.security.autoconfigure.parse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class ParseWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Bean
    ParseSessionHandlerMethodArgumentResolver parseSessionHandlerMethodArgumentResolver() {
        ParseSessionHandlerMethodArgumentResolver bean = new ParseSessionHandlerMethodArgumentResolver();
        return bean;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(parseSessionHandlerMethodArgumentResolver());
    }
}
