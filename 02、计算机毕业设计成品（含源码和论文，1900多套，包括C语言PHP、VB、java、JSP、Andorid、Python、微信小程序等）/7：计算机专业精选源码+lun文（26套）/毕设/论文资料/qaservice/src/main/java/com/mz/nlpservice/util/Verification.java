package com.mz.nlpservice.util;

import com.mz.nlpservice.db.LicenseHelper;
import com.mz.nlpservice.model.Device;
import com.mz.nlpservice.model.License;
import com.mz.nlpservice.model.Status;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by meizu on 2016/7/19.
 * request url parterid=XX&salt=XXX&sign=XX
 * parterid=XX&salt=XXX&secret=XX make sha-1 get sign
 */
public class Verification {
    private static  Logger logger = Logger.getLogger(Verification.class);
    private static Verification obj;
    private static final String OPCODE = "&sign=";
    private static MessageDigest md = null;
    
    private static String LICENSE_COLL = "license";
	private static String DEVICE_COLL = "license_device";
    
    private static ConcurrentHashMap<String, License> licenseMap = new ConcurrentHashMap<String, License>();
    private static ConcurrentHashMap<String, Device> deviceMap = new ConcurrentHashMap<String, Device>();

    private final static long tiemInterval = 3600*1000;
    private final static boolean saltTs = false;

    private Verification() throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance("SHA-1");

    }

    public static Verification getInstance() throws NoSuchAlgorithmException {
        if (obj == null)
            synchronized (Verification.class) {
                if (obj == null)
                    obj = new Verification();
            }
        return obj;
    }

    public static String b2s(byte[] messageDigest) {
        StringBuffer hexString = new StringBuffer();
        // 字节数组转换为 十六进制 数
        for (int i = 0; i < messageDigest.length; i++) {
            String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexString.append(0);
            }
            hexString.append(shaHex);
        }
        return hexString.toString();
    }

    public String calculate_sign(String url, String secret)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String varUrl = url + "&secret=" + secret;
        MessageDigest tm = null;
        try {
            tm = (MessageDigest)md.clone();
        } catch (CloneNotSupportedException e) {
            logger.error("sha-1 not support clone");
            try {
                tm = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e1) {
                logger.error("no sha-1 Algorithm");
                throw e1;
            }
        }

        byte[] res = new byte[0];
        try {
            res = tm.digest(varUrl.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("verify error unsupport encode", e);
            throw e;
        }
        String resStr = b2s(res);
        return  resStr;
    }

    public Integer check_get(String url, String secret) {
        int i = url.indexOf(OPCODE);
        if (i < 0)
            return Status.ERROR_PARAMETER;
        String opcode = url.substring(i + OPCODE.length());
        String varUrl = url.substring(0, i);

        String resStr = null;
        try {
            resStr = this.calculate_sign(varUrl, secret);
        } catch (NoSuchAlgorithmException e) {
            return Status.ERROR_SERVER;
        } catch (UnsupportedEncodingException e) {
            return Status.ERROR_ENCODING;
        }
        if (resStr.equals(opcode))
            return Status.SUCCESS;
        else
            return Status.ERROR_VERIFY;
    }
    
    
    public Integer check_post(String url, String secret) {
    	return check_get(url, secret);
    }
    
    private Integer verifyGet(HttpServletRequest  request) {
    	String queryStr = request.getQueryString();
        if (queryStr == null) {
            return Status.ERROR_OTHER;
        }
		int i = queryStr.indexOf("salt=");
        String partnerID = request.getParameter("partnerID");
        if (this.saltTs) {
            try {
                long timestamp = Long.parseLong(request.getParameter("salt")) * 1000;
                if (Math.abs(System.currentTimeMillis() - timestamp) > tiemInterval) {
                    return Status.ERROR_SALT;
                }
            } catch (Exception e) {
                return Status.ERROR_PARAMETER;
            }
        }

        if (i < 0 || partnerID == null || partnerID.equals("")) {
            return Status.ERROR_PARAMETER;
        }
        License license = this.getLicense(partnerID);
        if (license == null)
        	return Status.ERROR_NO_LICENSE;
        
        String verifyUrl = request.getRequestURI() + "?" + request.getQueryString();
        Integer res = this.check_get(verifyUrl, license.getSecret());
        if (res != Status.SUCCESS || license.getType() == License.TypeCode.ALLOW_ALL.val)
        	return res;
        
        String deviceID = request.getParameter("deviceID");
        if (deviceID == null || deviceID.equals(""))
        	return Status.ERROR_PARAMETER;
		
        Device dev = getDevice(partnerID, deviceID);
        if (dev != null)
        	return Status.SUCCESS;
        else
        	return Status.ERROR_NO_CLIENT;
    }
    
    public Integer verifyRequest(HttpServletRequest request) {
    	if (request.getMethod().equals("GET")) {
    		return verifyGet(request);
    	} else if (request.getMethod().equals("POST")) {
    		return verifyGet(request);
    	} else {
    		return Status.ERROR_UNSUPPORT_METHOD;
    	}
    }
    
    public License getLicense(String parterID) {
    	if (this.licenseMap.contains(parterID)) {
    		return licenseMap.get(parterID);
    	} else {
    		License license = LicenseHelper.findLicense(parterID);
    		if (license != null)
    			licenseMap.put(parterID, license);
    		return license;
    	}
    	
    }
    
    public Device getDevice(String parterID, String deviceID) {
    	String key = parterID + "_" + deviceID;
    	if (this.deviceMap.contains(key)) {
    		return this.deviceMap.get(deviceID);
    	} else {
    		Device device = LicenseHelper.findDevice(parterID, deviceID);
    		if (device != null)
    			deviceMap.put(key, device);
    		return device;
    	}
    }

}
