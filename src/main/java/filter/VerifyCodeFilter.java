package filter;


import util.VerifyCodeUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by zya on 2016/9/8.
 */
public class VerifyCodeFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String verifyCode = VerifyCodeUtil.generateVerifyCode(request);
        request.getSession().setAttribute("verifyCode", verifyCode);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {

    }
}
