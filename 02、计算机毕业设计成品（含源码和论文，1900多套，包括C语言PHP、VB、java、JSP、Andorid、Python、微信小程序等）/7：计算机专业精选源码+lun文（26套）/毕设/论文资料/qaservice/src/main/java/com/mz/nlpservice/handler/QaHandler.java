package com.mz.nlpservice.handler;

import com.mz.nlpservice.Exception.ParamError;
import com.mz.nlpservice.model.Domain;
import com.mz.nlpservice.model.ResObj;
import com.mz.nlpservice.model.Status;
import com.mz.nlpservice.module.ModuleManager;
import com.mz.nlpservice.module.NewsModule;
import com.mz.nlpservice.util.JsonUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gaozhenan on 2016/9/5.
 */
public class QaHandler extends BaseServerlet{
    private Logger logger = Logger.getLogger(QaHandler.class);

    private Object getNews() {
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ResObj res = null;
        try {
            String question = this.getParameter(req, "question", null, String.class);
            String deviceID = this.getParameter(req, "deviceID", null, String.class);
            if (question == null) {
                throw new ParamError("参数错误");
            }
            res = ModuleManager.getInstance().doWork(deviceID, question);
        } catch (ParamError e) {
            res.setStatus(Status.ERROR_PARAMETER);
            res.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error("channel execute occur error", e);
            if (res != SERVER_ERROR)
                res.setMessage(e.getMessage());
        } finally {
            resp.setHeader("Content-type", "application/json");
            resp.setCharacterEncoding("utf-8");
            resp.getWriter().write(JsonUtil.obj2Json(res));
        }
    }

    public static void main(String[]  args) {

    }
}
