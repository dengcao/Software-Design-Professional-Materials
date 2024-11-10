package com.mz.nlpservice.module;

import com.mz.nlpservice.core.Context;
import com.mz.nlpservice.model.Album;
import com.mz.nlpservice.model.Movie;
import com.mz.nlpservice.model.MovieRes;
import com.mz.nlpservice.model.Status;
import com.mz.nlpservice.util.HttpUtil;
import com.mz.nlpservice.util.JsonUtil;
import org.apache.log4j.Logger;
import org.apache.xerces.impl.xpath.regex.Match;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gaozhenan on 2017/1/17.
 */
public class MovieModule extends AbstractBaseModule {
    private static Logger logger = Logger.getLogger(MovieModule.class);
    private static final String DOMAIN = "movie";

    private static String movie_server;
    private static String movie_uri;
    private static String movie_sercret;

    static {
        movie_server = Context.getInstance().getProperty("movie_server");
        movie_uri = Context.getInstance().getProperty("movie_uri");
        movie_sercret = Context.getInstance().getProperty("movie_secret");
    }

    class CMovies {
        public int status;
        public String message;
        public List<Movie> data;
    }


    static class MovieRule {
        private static HashSet<String> tags = new HashSet<>();
        private static List<Pattern> tagsRes = new ArrayList<>();
        private static Pattern filmPattern;
        private static Pattern actorPattern;

        static {
            tags.add("恐怖");
            tags.add("喜剧");
            tags.add("伦理");
            tags.add("爱情");
            tags.add("恐怖");
            tags.add("惊悚");
            tags.add("悬疑");
            tags.add("侦探");
            tags.add("科幻");
            tags.add("纪录");
            tags.add("故事");
            tags.add("文艺");
            tags.add("动作");
            tags.add("武侠");
            tags.add("战争");
            tags.add("奇幻");
            tags.add("犯罪");
            tags.add("剧情");
            tags.add("警匪");
            tags.add("灾难");
            // 类型 地区 (周星驰的)
            tagsRes.add(Pattern.compile("(我想看|来个|播放)(?<cat>.*)片"));
            tagsRes.add(Pattern.compile("(我想看|来个|播放)(?<cat>.*)电影"));
            //演员 地区
            actorPattern = Pattern.compile("(我想看|来个|播放)?(?<actor>.*)(的|地)(?<name>.*)");
        }
        private static MovieNlpResutl dealSentce(String sentence) {
            MovieNlpResutl res = new MovieNlpResutl();
            Matcher m = null;
            for (Pattern p : tagsRes) {
                m = p.matcher(sentence);
                if (m.find()) {
                    String cat = m.group("cat");
                    if (tags.contains(cat)) {
                        res.cat = cat;
                        return res;
                    }
                    m = actorPattern.matcher(cat);
                    if (m.find()) {
                        String actorOrArea = m.group("actor");
                        if (tags.contains(actorOrArea)) {
                            res.cat = actorOrArea;
                        } else {
                            res.actorOrArea = actorOrArea;
                        }
                        String name =  m.group("name");
                        if (tags.contains(name)) {
                            res.cat = name;
                        }
                        return res;
                    }
                }
            }
            m = actorPattern.matcher(sentence);
            if (m.find()) {
                String actor = m.group("actor");
                String name = m.group("name");
                res.actor = actor;
                res.name = name;
                return res;
            }
            return null;
        }
    }

    @Override
    public String getDomain() {
        return this.DOMAIN;
    }

    @Override
    public Result doService(String deviceID, String keyWord) {
        Result result = new Result();
        MovieRes movieRes = new MovieRes();
        MovieNlpResutl nlpRes = MovieRule.dealSentce(keyWord);
        if (nlpRes != null) {
            try {
                CMovies res= fetchMovie(deviceID, JsonUtil.obj2Json(nlpRes));
                movieRes.status = res.status;
                movieRes.message = res.message;
                if (res.data != null)
                    movieRes.movies = res.data;
                else
                    movieRes.movies = new ArrayList<>();
            } catch (IOException e) {
                logger.error("movie module error", e);
                movieRes.status = Status.ERROR_SERVER;
                movieRes.message = e.getMessage();
            }

            logger.info(String.format("movie nlpresult:%s, movie count:%d",
                    nlpRes.toString(),
                    movieRes.getMovies().size()));

            result.nlpResutl = nlpRes;
            result.data = JsonUtil.obj2Json(movieRes);
            return result;
        }
        return null;
    }

    @Override
    public boolean check(String deviceID, String sentence) {
        return true;
    }

    private MovieModule.CMovies fetchMovie(String deviceID, String message) throws IOException {
        String url = calculateUrl(movie_server, movie_uri, deviceID, movie_sercret);
        return HttpUtil.doPost(url, message, MovieModule.CMovies.class);
    }
}
