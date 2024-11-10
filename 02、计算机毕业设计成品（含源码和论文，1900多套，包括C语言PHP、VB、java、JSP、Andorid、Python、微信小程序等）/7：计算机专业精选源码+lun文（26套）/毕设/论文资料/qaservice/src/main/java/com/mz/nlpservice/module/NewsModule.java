package com.mz.nlpservice.module;


import com.gargoylesoftware.htmlunit.html.TableRowGroup;
import com.mz.nlpservice.handler.QaHandler;
import com.mz.nlpservice.model.Domain;
import com.mz.nlpservice.model.NewsData;
import com.mz.nlpservice.model.NewsRes;
import com.mz.nlpservice.model.Status;
import com.mz.nlpservice.util.HttpUtil;
import com.mz.nlpservice.util.JsonUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gaozhenan on 2016/9/7.
 */
public class NewsModule implements BaseModule{
    private static Logger logger = Logger.getLogger(NewsModule.class);
    private static HashMap<String, String> CHANNEL_MAP = new HashMap();
    private static String url = "http://120.27.120.241:8100/music/news?channel=%s&search=%s&id=%s";

    private String pattern = "(.*)新闻";
    private List<String> verbs  = (List<String>) Arrays.asList("听", "播放", "来一段");
    private String endStr = "(的新闻|地新闻)";

    private Pattern r = Pattern.compile(pattern);
    private Pattern endR = Pattern.compile(endStr);

    private static final List<String> specialKeys = new ArrayList<String>();

    static {
        specialKeys.add("魅族新闻");
    }

    public class CNewsRes {
        public int status;
        public String message;
        public NewsData data;
    }

    private static String defChannel = "8";
    static {
        CHANNEL_MAP.put("军事","0");
        CHANNEL_MAP.put("财经","1");
        CHANNEL_MAP.put("体育","2");
        CHANNEL_MAP.put("娱乐","3");
        CHANNEL_MAP.put("八卦","3");
        CHANNEL_MAP.put("科技","4");
        CHANNEL_MAP.put("国外","5");
        CHANNEL_MAP.put("国内","6");
        CHANNEL_MAP.put("社会","7");
        CHANNEL_MAP.put("热点","8");
        CHANNEL_MAP.put("热门","8");
        CHANNEL_MAP.put("魅族新闻","9");
    }

    private int getVerbEndIndex(String str) {
        for (String v : verbs) {
            int index = str.indexOf(v);
            if (index >= 0) {
                return  index + v.length();
            }
        }
        return -1;
    }

    private String getQueryWord(String str) {
        Matcher mr = endR.matcher(str);
        str = mr.replaceAll("新闻");
        int index = this.getVerbEndIndex(str);
        Matcher m = r.matcher(str);
        if (index < 0) {
            index = 0;
        }
        if (m.find(index)) {
            return m.group(1);
        }
        return null;
    }

    private String checkSpecialKey(String str) {
        for (String key : specialKeys) {
            if (str.lastIndexOf(key) >= 0) {
                return key;
            }
        }
        return null;
    }

    public String getKeyWord(String question) {
        String keyword = checkSpecialKey(question);
        if (keyword == null)
            keyword = this.getQueryWord(question);
        return keyword;
    }

    @Override
    public String getDomain() {
        return Domain.NEWS;
    }

    @Override
    public boolean check(String deviceID, String sentence) {
        Matcher m = r.matcher(sentence);
        if (m.find())
            return true;
        return false;
    }

    @Override
    public Result doService(String deviceID, String question) {
        if (!check(deviceID, question))
            return null;
        NewsNlpResult nlpResult = new NewsNlpResult();
        String keyWord = getKeyWord(question);
        String channle  = CHANNEL_MAP.get(keyWord);
        logger.info(String.format("news handler, deviceid:%s, question:%s, keyword:%s", deviceID, question, keyWord));
        String searchWord = keyWord == null ? "" : keyWord;

        if (channle != null)
            searchWord = "";
        if (keyWord == null || keyWord.equals(""))
            channle = defChannel;

        if (channle == null)
            channle = "";

        nlpResult.setDomain(this.getDomain());
        nlpResult.setChannel(channle);
        nlpResult.setKeyword(keyWord);
        nlpResult.setSearchWord(searchWord);
        nlpResult.setSentence(question);

        NewsRes newsRes = new NewsRes();
        try {
            String serverUrl = String.format(url,
                    URLEncoder.encode(channle, "utf-8"),
                    URLEncoder.encode(searchWord, "utf-8"),
                    URLEncoder.encode(deviceID, "utf-8")
            );
            CNewsRes cnewRes = HttpUtil.doGet(serverUrl, CNewsRes.class);
            if (cnewRes != null) {
                newsRes.status = cnewRes.status;
                newsRes.message =cnewRes.message;
                newsRes.news = cnewRes.data.news;
            }
        } catch (UnsupportedEncodingException e) {
            newsRes.status = Status.ERROR_ENCODING;
            newsRes.message = e.getMessage();
            logger.info("error get news", e);
        } catch (IOException e) {
            newsRes.status = Status.ERROR_ENCODING;
            newsRes.message = e.getMessage();
            logger.info("error get news", e);
        } finally {
            Result result = new Result();
            result.data = JsonUtil.obj2Json(newsRes);;
            result.nlpResutl = nlpResult;
            return result;
        }
    }
}
