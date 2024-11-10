package com.mz.nlpservice.filter;

import com.mz.nlpservice.model.ResObj;
import com.mz.nlpservice.model.Status;
import com.mz.nlpservice.util.JsonUtil;
import com.mz.nlpservice.util.Verification;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;


/**
 * Created by meizu on 2016/7/21.
 */
public class VerifyFilter implements Filter {

    private static Logger logger = Logger.getLogger(VerifyFilter.class);
    private static ResObj authorError = new ResObj(Status.ERROR_VERIFY, "verify error", null);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    private String getRemoteIP(HttpServletRequest req) {
        String ip = req.getHeader("X-Real-IP");
        if (ip != null)
            return ip;
        else
            return req.getRemoteAddr();
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        servletResponse.setCharacterEncoding("utf-8");
        ((HttpServletResponse)servletResponse).setHeader("Content-type", "application/json");
        try {
            Integer status = Verification.getInstance().verifyRequest(req);
            String remoteIp = this.getRemoteIP(req);
            long begin = System.currentTimeMillis();
            if (status == Status.SUCCESS) {
                filterChain.doFilter(servletRequest, servletResponse);
                long end = System.currentTimeMillis();
                logger.info(String.format("verify ok, time:%d, ip:%s, partnerID:%s, deviceID:%s, uri:%s, query string:%s",
                        (end-begin), remoteIp, req.getParameter("partnerID"),
                        req.getParameter("deviceID"), req.getRequestURI(), req.getQueryString()));
            } else {
                long end = System.currentTimeMillis();
                logger.info(String.format("verify error, time:%d, ip:%s, partnerID:%s, deviceID:%s, status:%d, uri:%s, query string:%s",
                        (end-begin), remoteIp, req.getParameter("partnerID"),
                        req.getParameter("deviceID"), status, req.getRequestURI(), req.getQueryString()));
                ResObj res = new ResObj();
            	res.setStatus(status);
                servletResponse.getWriter().write(JsonUtil.obj2Json(res));

            }
        } catch (NoSuchAlgorithmException e) {
            logger.error("verify occur exception.", e);
            servletResponse.getWriter().write(JsonUtil.obj2Json(authorError));
        }
    }

    @Override
    public void destroy() {

    }
}
