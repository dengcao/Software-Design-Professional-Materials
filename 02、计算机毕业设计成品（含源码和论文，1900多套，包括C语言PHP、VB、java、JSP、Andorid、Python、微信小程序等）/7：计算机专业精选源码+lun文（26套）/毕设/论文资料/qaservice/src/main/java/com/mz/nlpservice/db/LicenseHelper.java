package com.mz.nlpservice.db;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mz.nlpservice.model.Device;
import com.mz.nlpservice.model.License;
import com.mz.nlpservice.util.JsonUtil;
import org.bson.Document;

public class LicenseHelper {
	
    private static String LICENSE_COLL = "license";
	private static String DEVICE_COLL = "license_device";
	
	public static License findLicense(String parterid) {
		MongoDatabase db = DBFactory.getMusicDBClient();
		MongoCollection<Document> licenseColl = db.getCollection(LICENSE_COLL);
		BasicDBObject query = new BasicDBObject();
		query.put("_id", parterid);
		Document obj = licenseColl.find(query).first();
		if (obj == null)
			return null;
		
		return (License) JsonUtil.json2Obj(obj, License.class);
	}
	
	public static Device findDevice(String parterid, String deviceid) {
		MongoDatabase db = DBFactory.getMusicDBClient();
		MongoCollection<Document> deviceColl = db.getCollection(DEVICE_COLL);
		BasicDBObject query = new BasicDBObject();
		query.put("license", parterid);
		query.put("device", deviceid);
		Document obj = deviceColl.find(query).first();
		if (obj == null)
			return null;
		
		return (Device)JsonUtil.json2Obj(obj, Device.class);
	}

}
