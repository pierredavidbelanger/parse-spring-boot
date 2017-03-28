package ca.pjer.spring.boot.security.autoconfigure.parse;

import ca.pjer.parseclient.Application;
import ca.pjer.parseclient.ParseSession;
import ca.pjer.parseclient.ParseUser;
import ca.pjer.parseclient.Perspective;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class ParseAuthenticationProvider implements AuthenticationProvider {

    private Application application;
    private Perspective anonymousPerspective;

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Perspective getAnonymousPerspective() {
        return anonymousPerspective;
    }

    public void setAnonymousPerspective(Perspective anonymousPerspective) {
        this.anonymousPerspective = anonymousPerspective;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ParseUserAuthentication.class.isAssignableFrom(aClass);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ParseUserAuthentication parseUserAuthentication = (ParseUserAuthentication) authentication;
        String username = String.valueOf(parseUserAuthentication.getPrincipal().getUsername());
        String password = String.valueOf(parseUserAuthentication.getPrincipal().getPassword());
        ParseUser parseUser = getAnonymousPerspective().withUsers().login(username, password, true);
        if (parseUser == null) {
            throw new AuthenticationServiceException("User not found");
        }
        ParseSession parseSession = getApplication().asSession(parseUser.getSessionToken()).withSessions().me();
        if (parseSession == null) {
            throw new AuthenticationServiceException("Session not found");
        }
        return new ParseSessionAuthentication(parseSession);
    }
}
