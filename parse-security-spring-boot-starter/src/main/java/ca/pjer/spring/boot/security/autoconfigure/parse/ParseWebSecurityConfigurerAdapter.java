package ca.pjer.spring.boot.security.autoconfigure.parse;

import ca.pjer.parseclient.Application;
import ca.pjer.parseclient.Perspective;
import ca.pjer.spring.boot.autoconfigure.parse.ParseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.ViewResolver;

@Configuration
@EnableWebSecurity
public class ParseWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Autowired
    ParseProperties parseProperties;

    @Autowired
    ParseSecurityProperties parseSecurityProperties;

    @Autowired
    Application application;

    @Autowired
    @Qualifier("anonymousPerspective")
    Perspective anonymousPerspective;

    @Autowired
    ViewResolver viewResolver;

    @Bean
    @ConditionalOnMissingBean(name = "parseTokenService")
    ParseTokenService parseTokenService() {
        ParseTokenServiceImpl bean = new ParseTokenServiceImpl();
        bean.setSecret(parseProperties.getMasterKey());
        bean.setApplication(application);
        return bean;
    }

    @Bean
    @ConditionalOnMissingBean(name = "parseSessionAuthenticationStrategy")
    SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        ParseTokenSessionAuthenticationStrategy bean = new ParseTokenSessionAuthenticationStrategy();
        bean.setParseTokenService(parseTokenService());
        return bean;
    }

    @Bean
    @ConditionalOnProperty(prefix = "parse.security", name = "secure.enabled", matchIfMissing = true)
    ParseSecureFilter parseSecureFilter() throws Exception {
        ParseSecureFilter bean = new ParseSecureFilter();
        bean.setRequestMatcher(new AntPathRequestMatcher(parseSecurityProperties.getSecure().getUri()));
        bean.setParseTokenService(parseTokenService());
        return bean;
    }

    @Bean
    @ConditionalOnProperty(prefix = "parse.security", name = "login.enabled", matchIfMissing = true)
    ParseLoginFilter parseLoginFilter() throws Exception {
        ParseLoginFilter bean = new ParseLoginFilter();
        bean.setRequestMatcher(new AntPathRequestMatcher(parseSecurityProperties.getLogin().getUri()));
        bean.setViewResolver(viewResolver);
        bean.setViewName(parseSecurityProperties.getLogin().getView());
        bean.setAuthenticationManager(authenticationManager());
        bean.setSessionStrategy(sessionAuthenticationStrategy());
        // bean.setSuccessHandler();
        // bean.setFailureHandler();
        // TODO: bean.set failure success ...
        return bean;
    }

    @Bean
    @ConditionalOnMissingBean(name = "parseAuthenticationProvider")
    ParseAuthenticationProvider parseAuthenticationProvider() {
        ParseAuthenticationProvider bean = new ParseAuthenticationProvider();
        bean.setApplication(application);
        bean.setAnonymousPerspective(anonymousPerspective);
        return bean;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if (parseSecurityProperties.getLogin().isEnabled()) {
            ParseLoginFilter filter = parseLoginFilter();
            http.addFilterAfter(filter, SecurityContextPersistenceFilter.class);
            http.authorizeRequests().requestMatchers(filter.getRequestMatcher()).permitAll();
        }

        if (parseSecurityProperties.getSecure().isEnabled()) {
            ParseSecureFilter filter = parseSecureFilter();
            http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
            http.authorizeRequests().requestMatchers(filter.getRequestMatcher()).authenticated();
        }
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(parseAuthenticationProvider());
    }
}
