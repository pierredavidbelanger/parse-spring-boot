package ca.pjer.spring.boot.security.autoconfigure.parse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ParseTokenSessionAuthenticationStrategy implements SessionAuthenticationStrategy {

    private ParseTokenService parseTokenService;

    public ParseTokenService getParseTokenService() {
        return parseTokenService;
    }

    public void setParseTokenService(ParseTokenService parseTokenService) {
        this.parseTokenService = parseTokenService;
    }

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws SessionAuthenticationException {
        if (authentication instanceof ParseSessionAuthentication) {
            getParseTokenService().save((ParseSessionAuthentication) authentication, request, response);
        }
    }
}
