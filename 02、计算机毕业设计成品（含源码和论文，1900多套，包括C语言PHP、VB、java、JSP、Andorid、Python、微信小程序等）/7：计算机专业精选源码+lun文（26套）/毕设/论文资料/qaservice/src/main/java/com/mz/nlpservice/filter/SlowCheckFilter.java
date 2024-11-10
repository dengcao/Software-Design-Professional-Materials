package com.mz.nlpservice.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by meizu on 2016/7/21.
 */
public class SlowCheckFilter implements Filter {
    //record 50ms query
    private static long longQuery = 50;
    private static Logger logger = Logger.getLogger(SlowCheckFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long begin = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse);
        long end = System.currentTimeMillis();
        long interval = end - begin;
        if (end - begin > longQuery) {
            if (servletRequest instanceof HttpServletRequest) {
                HttpServletRequest req = (HttpServletRequest)servletRequest;
                String url = req.getRequestURI() + "?" + req.getQueryString();
                logger.warn(url + String.format(" occur:%dms", interval));
            }
        }
    }

    @Override
    public void destroy() {

    }
}
