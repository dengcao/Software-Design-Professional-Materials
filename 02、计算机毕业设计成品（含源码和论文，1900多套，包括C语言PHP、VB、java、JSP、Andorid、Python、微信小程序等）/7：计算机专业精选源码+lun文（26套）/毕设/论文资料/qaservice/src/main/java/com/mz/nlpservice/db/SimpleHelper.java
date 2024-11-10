package com.mz.nlpservice.db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mz.nlpservice.model.VoiceKeyword;
import com.mz.nlpservice.util.JsonUtil;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaozhenan on 2016/8/10.
 */
public class SimpleHelper extends  BaseHelper{

    private static SimpleHelper obj;
    private final static String VOICE_KEYWORD_COLL = "voice_keyword";
    private SimpleHelper() {
        super(DBFactory.getMusicDBClient());
    }

    public static synchronized SimpleHelper getInstance() {
        if (obj == null)
            obj = new SimpleHelper();
        return obj;
    }

    public List<VoiceKeyword> findVoiceKeywords(String type) {
        List<VoiceKeyword> res = new ArrayList<>();
        MongoCollection<Document> coll =  this.getCollection(VOICE_KEYWORD_COLL);
        Document query = new Document();
        if (type != null)
            query.put("type", type);

        MongoCursor<Document> cursor =  coll.find(query).iterator();
        while (cursor.hasNext()) {
            res.add(JsonUtil.json2Obj(cursor.next(), VoiceKeyword.class));
        }
        return res;
    }

}
