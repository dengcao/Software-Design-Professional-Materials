package com.mz.nlpservice.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gaozhenan on 2016/8/10.
 */
public class TimeoutCache {

    private static ConcurrentHashMap<String, Entry> map =  new ConcurrentHashMap(100);
    private static TimeoutCache object;

    private static class Entry {
        private long expire;
        private Object obj;

        public Entry(Object obj, long expire) {
            this.expire = expire;
            this.obj = obj;
        }

        public long getExpire() {
            return this.expire;
        }

        public Object getObj() {
            return this.obj;
        }
    }

    public static TimeoutCache getInstance() {
        if (object == null) {
            synchronized (TimeoutCache.class) {
                if (object == null)
                    object = new TimeoutCache();

            }
        }
        return object;
    }

    private TimeoutCache() {

    }

    public static <T> T getObj(String key) {
        Entry val = map.get(key);
        if (val != null && System.currentTimeMillis() < val.getExpire()) {
            return (T)val.getObj();
        } else {
            if (val != null)
                map.remove(key);
            return null;
        }
    }

    public static void setObj(String key, Object obj, long timeout) {
        long expire = System.currentTimeMillis() + timeout;
        Entry entry = new Entry(obj, expire);
        map.put(key, entry);
    }
}
