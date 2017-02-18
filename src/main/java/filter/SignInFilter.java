package filter;


import model.User;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by z673413 on 2016/7/26.
 */
public class SignInFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        User user = (User) req.getSession().getAttribute("signInUser");
        String url = req.getRequestURI();

        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        if (user != null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (url.indexOf("signin.html") > 0) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            resp.sendRedirect("signin.html");
        }
    }

    public void destroy() {

    }
}
