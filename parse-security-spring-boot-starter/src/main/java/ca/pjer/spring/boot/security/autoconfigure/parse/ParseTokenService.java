package ca.pjer.spring.boot.security.autoconfigure.parse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ParseTokenService {

    void save(ParseSessionAuthentication authentication, HttpServletRequest request, HttpServletResponse response);

    ParseSessionAuthentication load(HttpServletRequest request, HttpServletResponse response);

}
