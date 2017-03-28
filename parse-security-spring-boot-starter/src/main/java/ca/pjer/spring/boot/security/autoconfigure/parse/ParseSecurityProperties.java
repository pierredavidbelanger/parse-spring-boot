package ca.pjer.spring.boot.security.autoconfigure.parse;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "parse.security")
public class ParseSecurityProperties {

    private boolean enabled = true;

    private FilterProperties secure =
            new FilterProperties("/**");

    private FilterWithViewProperties login =
            new FilterWithViewProperties("/login", "ca/pjer/parse/security/login");

    private FilterWithViewProperties forgot =
            new FilterWithViewProperties("/forgot", "ca/pjer/parse/security/forgot");

    private FilterWithViewProperties register =
            new FilterWithViewProperties("/register", "ca/pjer/parse/security/register");

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public FilterProperties getSecure() {
        return secure;
    }

    public FilterWithViewProperties getLogin() {
        return login;
    }

    public FilterWithViewProperties getForgot() {
        return forgot;
    }

    public FilterWithViewProperties getRegister() {
        return register;
    }

    public static class FilterProperties {

        private boolean enabled = true;
        private String uri;

        public FilterProperties() {
        }

        public FilterProperties(String uri) {
            this.uri = uri;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

    public static class FilterWithViewProperties extends FilterProperties {

        private String view;

        public FilterWithViewProperties() {
        }

        public FilterWithViewProperties(String uri, String view) {
            super(uri);
            this.view = view;
        }

        public String getView() {
            return view;
        }

        public void setView(String view) {
            this.view = view;
        }
    }
}
