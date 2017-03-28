package ca.pjer.spring.boot.security.autoconfigure.parse;

import ca.pjer.parseclient.ParseSession;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class ParseSessionHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(ParseSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext != null) {
            Authentication authentication = securityContext.getAuthentication();
            if (authentication instanceof ParseSessionAuthentication) {
                ParseSessionAuthentication parseSessionAuthentication = (ParseSessionAuthentication) authentication;
                return parseSessionAuthentication.getPrincipal();
            }
        }
        return null;
    }
}
