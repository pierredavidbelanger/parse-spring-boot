package ca.pjer.spring.boot.security.autoconfigure.parse;

import ca.pjer.parseclient.Application;
import ca.pjer.parseclient.ParseSession;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Date;

public class ParseTokenServiceImpl implements ParseTokenService {

    private String secret;
    private Application application;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void save(ParseSessionAuthentication authentication, HttpServletRequest request, HttpServletResponse response) {
        String accessToken = createAccessToken(authentication.getPrincipal().getSessionToken(), authentication.getPrincipal().getExpiresAt());
        setCookie("token_type", "Bearer", -1, request, response);
        setCookie("access_token", accessToken, -1, request, response);
    }

    public ParseSessionAuthentication load(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = findCookie("access_token", request, response);
        if (StringUtils.hasText(accessToken)) {
            try {
                Jws<Claims> accessJws = parse(accessToken);
                String sessionToken = accessJws.getBody().getSubject();
                if (StringUtils.isEmpty(accessJws)) {
                    throw new IllegalArgumentException("Empty token subject");
                }
                ParseSession parseSession = getApplication().asSession(sessionToken).withSessions().me();
                if (StringUtils.isEmpty(accessJws)) {
                    throw new IllegalStateException("Session not found");
                }
                return new ParseSessionAuthentication(parseSession);
            } catch (Exception e) {
                unsetCookie("token_type", request, response);
                unsetCookie("access_token", request, response);
            }
        }
        return null;
    }

    protected void setCookie(String name, String value, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    protected void unsetCookie(String name, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    protected String findCookie(String name, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    protected String createAccessToken(String subject, Date expiration) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, encodeStringAsBytes(getSecret()))
                .setSubject(subject).setExpiration(expiration).compact();
    }

    protected Jws<Claims> parse(String token) {
        return Jwts.parser()
                .setSigningKey(encodeStringAsBytes(getSecret()))
                .parseClaimsJws(token);
    }

    protected byte[] encodeStringAsBytes(String s) {
        try {
            return s.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
