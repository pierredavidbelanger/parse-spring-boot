package ca.pjer.spring.boot.security.autoconfigure.parse;

import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class ParseFilterSupport extends OncePerRequestFilter {

    private RequestMatcher requestMatcher;
    private ViewResolver viewResolver;
    private String viewName;

    public RequestMatcher getRequestMatcher() {
        return requestMatcher;
    }

    public void setRequestMatcher(RequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }

    public ViewResolver getViewResolver() {
        return viewResolver;
    }

    public void setViewResolver(ViewResolver viewResolver) {
        this.viewResolver = viewResolver;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    @Override
    protected final void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        RequestMatcher requestMatcher = getRequestMatcher();
        if (requestMatcher.matches(request)) {
            ModelAndView modelAndView = new ModelAndView();
            try {
                handleRequest(request, response, modelAndView);
            } catch (Exception e) {
                throw new ServletException("Exception while handling request for " + requestMatcher, e);
            }
            View view = modelAndView.getView();
            if (view == null && modelAndView.getViewName() != null) {
                try {
                    view = getViewResolver().resolveViewName(modelAndView.getViewName(), request.getLocale());
                } catch (Exception e) {
                    throw new ServletException("Exception while resolving view " + modelAndView.getViewName(), e);
                }
            }
            if (view != null) {
                try {
                    view.render(modelAndView.getModel(), request, response);
                } catch (Exception e) {
                    throw new ServletException("Exception while rendering view " + view, e);
                }
            }
        }
        if (!response.isCommitted()) {
            chain.doFilter(request, response);
        }
    }

    protected abstract void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception;

}
