package com.mz.nlpservice.module;

import com.mz.nlpservice.core.Context;
import com.mz.nlpservice.model.Album;
import com.mz.nlpservice.model.Domain;
import com.mz.nlpservice.model.MusicRes;
import com.mz.nlpservice.model.Status;
import com.mz.nlpservice.util.HttpUtil;
import com.mz.nlpservice.util.JsonUtil;
import com.mz.nlpservice.util.Verification;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gaozhenan on 2016/12/20.
 */
public class MusicModule implements BaseModule {
    private static Logger logger = Logger.getLogger(MusicModule.class);
    private String pattern = "(.*)时的歌";
    private String pattern1 = "(.*)的歌";
    private static List<String> verbos = new ArrayList<>();
    private List<String> particles = new ArrayList<>();

    private Pattern particleP = Pattern.compile("(.*)[时的|时地](.*)");
    private Pattern endR = Pattern.compile(pattern);
    private Pattern endR1 = Pattern.compile(pattern1);

    private static final String PARTERNER = "meizu";
    private static String url;
    private static String music_server;
    private static String music_uri;
    private static String music_sercret;

    static {
        music_server = Context.getInstance().getProperty("music_server");
        music_uri = Context.getInstance().getProperty("music_uri");
        music_sercret = Context.getInstance().getProperty("music_secret");
        verbos.add("播放");
        verbos.add("我想听");

    }

    class Param {
        List<String> artistOrTags = new ArrayList<>();
        List<String> songName = new ArrayList<>();;
    }

    class CnlpFlags {
        boolean tagFlag;
        boolean artistFlag;
        boolean songNameFlag;
    }

    class CMusicData {
        List<Album> albums;
        CnlpFlags nlpFlag;

    }

    class CMusicNews {
        public int status;
        public String message;
        public CMusicData data;
    }

    @Override
    public String getDomain() {
        return Domain.MUSIC;
    }

    @Override
    public boolean check(String deviceID, String sentence) {
//        if ( !sentence.startsWith(verbo)) {
//            return false;
//        }
        return true;
    }

    private Param pickParam(String sentence) {
        Param param = new Param();
        for (String verbo : verbos) {
            int index = sentence.indexOf(verbo);
            if (index >= 0) {
                sentence = sentence.substring(index+verbo.length());
                break;
            }
        }
        String sentenceNprefix = sentence;
        param.songName = new ArrayList<>();
        param.songName.add(0, sentenceNprefix);
        //remove suffix
        Matcher m = endR.matcher(sentenceNprefix);
        if (!m.find()) {
            m = endR1.matcher(sentenceNprefix);
        }
        if (m.find(0)) {
            String artist = m.group(1);
            int index = artist.indexOf("的");
            if (index > 1) {
                param.artistOrTags.add(0, artist.substring(0, index));
                param.songName.add(0, sentenceNprefix.substring(index+1));
            } else {
                param.artistOrTags.add(0, artist);
            }
        } else {
            m = particleP.matcher(sentenceNprefix);
            if (m.find()) {
                param.artistOrTags.add(0, m.group(1));
                param.songName.add(0, m.group(2));
            }
        }
        return param;
    }

    @Override
    public Result doService(String deviceID, String sentence) {
        Result result = new Result();
        MusicRes res = new MusicRes();
        Param p = this.pickParam(sentence);
        if (p.artistOrTags.size() != 0 || p.songName.size() != 0) {
            MusicNlpResult nlpResult = new MusicNlpResult();
            nlpResult.setDomain(this.getDomain());
            nlpResult.setSentence(sentence);
            nlpResult.setArtistOrTags(p.artistOrTags);
            nlpResult.setSongNames(p.songName);
            try {
                CMusicNews news = fetchMusic(deviceID, JsonUtil.obj2Json(nlpResult));
                if (news.status != 0) {
                    res.setStatus(news.status);
                    res.setMessage(news.message);
                } else {
                    res.setAlbum(news.data.albums);
                    res.setStatus(news.status);
                    res.setMessage(news.message);
                    if (news.data.nlpFlag != null) {
                        nlpResult.setArtistFlag(news.data.nlpFlag.artistFlag);
                        nlpResult.setSongNameFlag(news.data.nlpFlag.songNameFlag);
                        nlpResult.setTagFlag(news.data.nlpFlag.tagFlag);
                    }
                }
            } catch (IOException e) {
                res.setStatus(Status.ERROR_SERVER);
                res.setMessage(e.getMessage());
                logger.error("music module error", e);
            }

            logger.info(String.format("artist or tags:%s, songname:%s, album count:%d",
                    String.join(",", p.artistOrTags),
                    String.join(",", p.songName),
                    res.getAlbum().size()));

            result.nlpResutl = nlpResult;
            result.data = JsonUtil.obj2Json(res);
            return result;
        }
        return null;
    }

    private CMusicNews fetchMusic(String deviceID, String message) throws IOException {
        String url = calculateUrl(deviceID);
        return HttpUtil.doPost(url, message, CMusicNews.class);
    }

    private  String calculateUrl(String deviceID) {
        if (url != null)
            return url;
        String sign = null;
        try {
            long salt = System.currentTimeMillis() /1000;
            String param = String.format("deviceID=%s&salt=%d&partnerID=%s", deviceID, salt, PARTERNER);
            String url  = music_uri + "?" + param;
            sign = Verification.getInstance().calculate_sign(url, music_sercret);
            this.url = music_server + url + "&sign=" + sign;
            logger.info("calculte music url:" + this.url);
            return this.url;
        } catch (NoSuchAlgorithmException e) {
            logger.error("calculate url error.", e);
        } catch (UnsupportedEncodingException e) {
            logger.error("calculate url error.", e);
        }
        return null;
    }

    private static  void say(Param p) {
        String a = "";
        String b = "";
        a = String.join(",", p.artistOrTags);
        b = String.join(",", p.songName);
        System.out.println(String.format("artist:%s, songName:%s", a, b));
    }

    public  static void main(String[] args) {
        long a = System.currentTimeMillis() /1000;
        MusicModule m = new MusicModule();
        String t = "我想听刘德华的忘情水";
        String t1 = "给我播放任贤齐的歌";
        String t2 = "播放跑步时的歌";
        String t3 = "播放张三的歌";
        String t4 = "播放齐秦的张三的歌";
        String t5 = "播放双截棍";
        m.say(m.pickParam(t));
        m.say(m.pickParam(t1));
        m.say(m.pickParam(t2));
        m.say(m.pickParam(t3));
        m.say(m.pickParam(t5));
    }


}
