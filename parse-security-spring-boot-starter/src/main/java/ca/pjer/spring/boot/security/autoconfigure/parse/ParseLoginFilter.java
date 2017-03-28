package ca.pjer.spring.boot.security.autoconfigure.parse;

import ca.pjer.parseclient.ParseUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ParseLoginFilter extends ParseFilterSupport {

    private AuthenticationManager authenticationManager;
    private SessionAuthenticationStrategy sessionStrategy;
    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public SessionAuthenticationStrategy getSessionStrategy() {
        return sessionStrategy;
    }

    public void setSessionStrategy(SessionAuthenticationStrategy sessionStrategy) {
        this.sessionStrategy = sessionStrategy;
    }

    public AuthenticationSuccessHandler getSuccessHandler() {
        return successHandler;
    }

    public void setSuccessHandler(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    public AuthenticationFailureHandler getFailureHandler() {
        return failureHandler;
    }

    public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {

        if (request.getMethod().equalsIgnoreCase("POST")) {

            String username = request.getParameter("username");
            String password = request.getParameter("password");

            Authentication authentication = new ParseUserAuthentication(new ParseUser(username, password, null));

            try {

                authentication = getAuthenticationManager().authenticate(authentication);

                if (getSessionStrategy() != null) {
                    getSessionStrategy().onAuthentication(authentication, request, response);
                }

                if (getSuccessHandler() != null) {
                    getSuccessHandler().onAuthenticationSuccess(request, response, authentication);
                }

            } catch (AuthenticationException e) {

                if (getFailureHandler() != null) {
                    getFailureHandler().onAuthenticationFailure(request, response, e);
                }
            }
        }

        if (!response.isCommitted()) {
            modelAndView.setViewName(getViewName());
        }
    }
}
