package com.mz.nlpservice.plugin;

import com.mz.nlpservice.model.CommonParam;

/**
 * Created by meizu on 2016/7/27.
 */
public class ArtificialService {

    private static Service service;

    public static void setService(Service ser) {
        ArtificialService.service = ser;
    }

    public static Object doService(CommonParam cp) throws Exception{
        if (service == null)
            throw new Exception("no service support");
        return service.doService(cp);
    }
}
