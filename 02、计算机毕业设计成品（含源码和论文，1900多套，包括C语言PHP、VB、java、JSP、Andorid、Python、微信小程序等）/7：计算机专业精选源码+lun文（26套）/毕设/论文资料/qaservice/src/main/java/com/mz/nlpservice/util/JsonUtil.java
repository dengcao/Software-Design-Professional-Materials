package com.mz.nlpservice.util;

import com.google.gson.Gson;
import org.bson.*;

/**
 * Created by meizu on 2016/7/21.
 */
public class JsonUtil {
    private static Gson gson = new Gson();

    public static String obj2Json(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T json2Obj(Document doc, Class<T> c) {
        return gson.fromJson(gson.toJson(doc), c);
    }
}
