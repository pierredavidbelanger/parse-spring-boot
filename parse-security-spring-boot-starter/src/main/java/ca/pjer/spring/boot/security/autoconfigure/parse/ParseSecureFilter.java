package ca.pjer.spring.boot.security.autoconfigure.parse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ParseSecureFilter extends ParseFilterSupport {

    private ParseTokenService parseTokenService;

    public ParseTokenService getParseTokenService() {
        return parseTokenService;
    }

    public void setParseTokenService(ParseTokenService parseTokenService) {
        this.parseTokenService = parseTokenService;
    }

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
        Authentication authentication = getParseTokenService().load(request, response);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}
