package com.mz.nlpservice.module;

import com.mz.nlpservice.model.Domain;
import com.mz.nlpservice.model.ResObj;
import com.mz.nlpservice.model.Status;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaozhenan on 2016/12/20.
 */
public class ModuleManager {
    private static Logger logger = Logger.getLogger(ModuleManager.class);
    private static List<BaseModule> modules = new ArrayList<>();
    private static ResObj res =  new ResObj();
    private static ModuleManager obj = null;

    static {
        res.status = Status.UN_HANDLE;
        res.domain = Domain.UNKNOW;
        res.message = "不能处理请求";
    }

    private ModuleManager() {

    }

    public static ModuleManager getInstance() {
        if (obj == null) {
            synchronized (ModuleManager.class) {
                if (obj == null) {
                    obj = new ModuleManager();
                }
            }
        }
        return obj;
    }

    public static void addModule(BaseModule module) {
        logger.info("add domain handler, domain:" +  module.getDomain());
        modules.add(module);
    }

    public ResObj doWork(String deviceid, String question) {
        try {
            for (BaseModule module : modules) {
                BaseModule.Result res = module.doService(deviceid, question);
                if (res != null) {
                    ResObj result =  new ResObj();
                    result.setDomain(module.getDomain());
                    result.setData(res.data);
                    result.setNlpResult(res.nlpResutl);
                    return result;
                }
            }
        } catch (Exception e) {
            ResObj result =  new ResObj();
            result.setStatus(Status.HANDLE_ERR);
            result.setMessage(e.getMessage());
            return result;
        }
        return res;
    }
}
