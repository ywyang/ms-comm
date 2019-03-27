package demo.ms.common.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.client.RestTemplate;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@Order(1)
@WebFilter(urlPatterns = "/*",filterName = "JwtSecurityFilter")
public class JwtSecurityFilter implements Filter {

    @Autowired
    JwtAccessTokenConverter jwtAccessTokenConverter;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthentication jwtAuthentication = new JwtAuthentication(authentication,jwtAccessTokenConverter);
        SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
