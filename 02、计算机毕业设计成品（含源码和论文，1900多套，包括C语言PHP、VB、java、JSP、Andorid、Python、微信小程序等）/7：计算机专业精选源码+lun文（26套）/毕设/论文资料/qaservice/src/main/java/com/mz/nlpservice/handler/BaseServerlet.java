package com.mz.nlpservice.handler;

import com.google.gson.Gson;
import com.mz.nlpservice.model.ResObj;
import com.mz.nlpservice.model.Status;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by meizu on 2016/7/18.
 */
public class BaseServerlet extends HttpServlet {

    protected static Logger logger = Logger.getLogger(BaseServerlet.class);
    protected final static ResObj SERVER_ERROR = new ResObj(Status.ERROR_SERVER, "server internal error", null);
    protected static Gson gson = new Gson();

    protected <T> T getParameter(HttpServletRequest req, String paramName, T def, Class<T> cls)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String val = req.getParameter(paramName);
        if (val == null || val.equals(""))
            return def;

        Constructor c = cls.getConstructor(String.class);
        return (T)c.newInstance(val);
    }

}
