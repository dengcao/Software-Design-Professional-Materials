package com.mz.nlpservice.db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mz.nlpservice.model.Album;
import com.mz.nlpservice.model.Channel;
import com.mz.nlpservice.model.Song;
import com.mz.nlpservice.util.JsonUtil;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by meizu on 2016/8/5.
 */
public class MusicHelper {
    protected static Logger logger = Logger.getLogger(MusicHelper.class);
    private final static String CHANNEL_COLL_NAME = "channel";
    private final static String ALBUM_COLL_NAME = "album";
    private final static String SONG_COLL_NAME = "song";

    private static MongoCollection<Document> getCollection(String colName) {
        MongoDatabase db = DBFactory.getMusicDBClient();
        return db.getCollection(colName);
    }

    public static List<Channel> findChnnel(int pageNum, int pageSize) {
        MongoCollection<Document> channelColl = getCollection(CHANNEL_COLL_NAME);
        Document sort = new Document("sort", 1);
        MongoCursor<Document> cursor = channelColl.find().sort(sort).skip((pageNum-1)*pageSize).limit(pageSize).iterator();
        List<Channel> res = new LinkedList<Channel>();
        while (cursor.hasNext()) {
            res.add(JsonUtil.json2Obj(cursor.next(), Channel.class));
        }
        return res;
    }

    public static Channel findChnnelByID(int id) {
        MongoCollection<Document> channelColl = getCollection(CHANNEL_COLL_NAME);
        Document query = new Document("_id", id);
        Document doc = channelColl.find(query).first();
        if (doc != null)
            return JsonUtil.json2Obj(doc, Channel.class);
        return null;
    }

    public static List<Album> findAlbumsOfChannel(int channelid) {
        List<Album> res = new ArrayList<Album>();
        MongoCollection<Document> albumColl = getCollection(ALBUM_COLL_NAME);
        Channel channel = findChnnelByID(channelid);
        if (channel == null || channel.getAlbums() == null || channel.getAlbums().size() == 0)
            return res;
        Document query = new Document("_id" , new Document("$in", channel.getAlbums()));
        MongoCursor<Document> cursor = albumColl.find(query).iterator();
        while (cursor.hasNext()) {
            res.add(JsonUtil.json2Obj(cursor.next(), Album.class));
        }
        return res;
    }

    public static List<Album> findAlbumsByName(String name, int pageNum, int pageSize) {
        List<Album> res = new ArrayList<Album>();
        MongoCollection<Document> albumColl = getCollection(ALBUM_COLL_NAME);
        Document query = new Document("name" , new Document("$regex", name));
        MongoCursor<Document> cursor = albumColl.find(query).skip((pageNum-1)*pageSize).limit(pageSize).iterator();
        while (cursor.hasNext()) {
            res.add(JsonUtil.json2Obj(cursor.next(), Album.class));
        }
        return res;
    }

    public static Album findAlbumByID(int albumid) {
        MongoCollection<Document> albumColl = getCollection(ALBUM_COLL_NAME);
        Document query = new Document("_id" , albumid);
        Document doc = albumColl.find(query).first();
        if (doc == null)
            return null;
        return JsonUtil.json2Obj(doc, Album.class);
    }

    public static List<Song> findSongOfAlbum(int albumid) {
        List<Song> res = new ArrayList<Song>();
        Album album = findAlbumByID(albumid);
        if (album == null || album.getSongs() == null || album.getSongs().size() == 0)
            return res;
        Document query = new Document("_id" , new Document("$in", album.getSongs()));
        MongoCollection<Document> songColl = getCollection(SONG_COLL_NAME);
        MongoCursor<Document> cursor = songColl.find(query).iterator();
        while (cursor.hasNext()) {
            res.add(JsonUtil.json2Obj(cursor.next(), Song.class));
        }
        return res;
    }

    public static Song findSongByID(int songid) {
        MongoCollection<Document> songColl = getCollection(SONG_COLL_NAME);
        Document query = new Document("_id" , songid);
        Document doc = songColl.find(query).first();
        if (doc == null)
            return null;
        return JsonUtil.json2Obj(doc, Song.class);
    }

    public static List<Song> findSongByName(String name, int pageNum, int pageSize) {
        List<Song> res = new ArrayList<Song>();
        Document query = new Document("name" , new Document("$regex", name));
        MongoCollection<Document> songColl = getCollection(SONG_COLL_NAME);
        MongoCursor<Document> cursor = songColl.find(query).skip((pageNum - 1) * pageSize)
                .limit(pageSize).iterator();
        while (cursor.hasNext()) {
            res.add(JsonUtil.json2Obj(cursor.next(), Song.class));
        }
        return res;
    }

}
