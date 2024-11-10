package com.mz.nlpservice.core;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;

import java.io.*;
import java.util.*;

/**
 * Created by meizu on 2016/6/29.
 */
public class Context {
    private static Logger LOG = Logger.getLogger(Context.class.getName());
    private static Context instance;
    private static  Properties properties = null;
    private static Map<String, Object> beanMap = null;
    private static final String COMMENT = "#";
    private static final String PROPERTIES = ".properties";
    private static final String XML = ".xml";

    private Context() {

    }
    public synchronized static Context getInstance() {
         if (instance == null)
             instance = new Context();
        return instance;
    }

    /**
     * copy from XmlConfiguration
     * @param confPath
     */
    public void  parseFile(String confPath) throws IOException {

        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(confPath);
        BufferedReader br =  new BufferedReader(new InputStreamReader(ins));

        ArrayList<String> props = new ArrayList<>();
        ArrayList<String> xmls = new ArrayList<>();
        while (true) {
            String line = br.readLine();
            if (line == null)
                break;
            if (line.startsWith(COMMENT)) {
                continue;
            }
            if (line.toLowerCase().endsWith(PROPERTIES)) {
                props.add(line);
            } else if (line.toLowerCase().endsWith(XML)) {
                xmls.add(line);
            } else if (line.trim().equals("")) {

            } else {
                LOG.warn("unknown param:"  +  line);
            }
        }

        try
        {
            // Add System Properties
            properties = new Properties();
            properties.putAll(System.getProperties());

            // For all arguments, load properties
            for (String arg : props)
            {
                if (arg.toLowerCase(Locale.ENGLISH).endsWith(".properties"))
                    properties.load(Resource.newClassPathResource(arg).getInputStream());
            }

            // For all arguments, parse XMLs
            XmlConfiguration last = null;
            Object[] obj = new Object[xmls.size()];
            for (int i = 0; i < xmls.size(); i++)
            {
                XmlConfiguration configuration = new XmlConfiguration(Resource.newClassPathResource(xmls.get(i)).getURI().toURL());
                if (last != null)
                    configuration.getIdMap().putAll(last.getIdMap());
                if (properties.size() > 0)
                {
                    Map<String, String> ps = new HashMap<>();
                    for (Object key : properties.keySet())
                    {
                        ps.put(key.toString(),String.valueOf(properties.get(key)));
                    }
                    configuration.getProperties().putAll(ps);
                }
                obj[i] = configuration.configure();
                last = configuration;
            }
            beanMap = last.getIdMap();
            // For all objects created by XmlConfigurations, start them if they are lifecycles.
            for (int i = 0; i < obj.length; i++)
            {
                if (obj[i] instanceof LifeCycle)
                {
                    LifeCycle lc = (LifeCycle)obj[i];
                    if (!lc.isRunning())
                        lc.start();
                }
            }
        }
        catch (Exception e)
        {
            LOG.warn("parse config file error", e);
        }
    }

    public Object getObject(String key) {
        return this.beanMap.get(key);
    }

    public String getProperty(String key) {
        return (String) this.properties.get(key);
    }

    public Integer getIntProperty(String key, int defVal) {
        String val = (String)this.properties.get(key);
        if (val != null)
            return Integer.parseInt(val);
        else
            return defVal;
    }

    public Long getLongPorperty(String key, long defVal) {
        String val = (String)this.properties.get(key);
        if (val != null)
            return Long.parseLong(val);
        else
            return defVal;
    }
}
