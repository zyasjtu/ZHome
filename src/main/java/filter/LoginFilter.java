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
 * Created by zya on 2016/9/8.
 */
public class LoginFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        User user = (User) req.getSession().getAttribute("loginUser");
        String url = req.getRequestURI();

        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        if (user != null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (url.indexOf("login") > 0) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            resp.sendRedirect("index.jsp");
        }
    }

    public void destroy() {

    }
}
