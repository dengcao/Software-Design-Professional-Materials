package com.mz.nlpservice.module;

import com.mz.nlpservice.core.Context;
import com.mz.nlpservice.util.Verification;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by gaozhenan on 2017/1/19.
 */
public abstract class AbstractBaseModule implements BaseModule{
    private static Logger logger = Logger.getLogger(AbstractBaseModule.class);
    private static final String PARTERNER = "meizu";


    protected   String calculateUrl(String server, String uri, String deviceID, String sercret) {
        String sign = null;
        try {
            long salt = System.currentTimeMillis() /1000;
            String param = String.format("deviceID=%s&salt=%d&partnerID=%s", deviceID, salt, PARTERNER);
            String url  = uri + "?" + param;
            sign = Verification.getInstance().calculate_sign(url, sercret);
            url = server + url + "&sign=" + sign;
            logger.info("calculte music url:" + url);
            return url;
        } catch (NoSuchAlgorithmException e) {
            logger.error("calculate url error.", e);
        } catch (UnsupportedEncodingException e) {
            logger.error("calculate url error.", e);
        }
        return null;
    }
}
