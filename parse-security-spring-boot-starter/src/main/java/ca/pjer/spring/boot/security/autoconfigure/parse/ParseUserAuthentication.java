package ca.pjer.spring.boot.security.autoconfigure.parse;

import ca.pjer.parseclient.ParseUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;

public class ParseUserAuthentication extends AbstractAuthenticationToken {

    private final ParseUser parseUser;

    public ParseUserAuthentication(ParseUser parseUser) {
        super(Collections.<GrantedAuthority>emptySet());
        this.parseUser = parseUser;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public ParseUser getPrincipal() {
        return parseUser;
    }
}
