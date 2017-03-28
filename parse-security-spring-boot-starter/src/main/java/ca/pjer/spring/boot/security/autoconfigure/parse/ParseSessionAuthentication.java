package ca.pjer.spring.boot.security.autoconfigure.parse;

import ca.pjer.parseclient.ParseSession;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class ParseSessionAuthentication extends AbstractAuthenticationToken {

    private final ParseSession parseSession;

    public ParseSessionAuthentication(ParseSession parseSession) {
        super(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        setAuthenticated(true);
        this.parseSession = parseSession;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public ParseSession getPrincipal() {
        return parseSession;
    }
}
