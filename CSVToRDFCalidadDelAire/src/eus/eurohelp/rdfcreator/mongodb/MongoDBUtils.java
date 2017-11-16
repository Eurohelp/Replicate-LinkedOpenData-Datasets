package eus.eurohelp.rdfcreator.mongodb;

import java.io.IOException;
import java.io.PrintWriter;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

/**
 * 
 * @author dmuchuari @14/11/2017
 */

public class MongoDBUtils {

	private MongoDatabase dataBase;
	private MongoClient mongoClient;
	private MongoCollection<Document> collection;

	/**
	 * Constructor 
	 * @param pDataBase
	 * @param pCollection
	 * @throws IOException
	 */
	public MongoDBUtils(String pDataBase, String pCollection) throws IOException {
		String mongoPath = PropertiesManager.getINSTANCE().getProperty("mongodb-client");
		String mongoPort = PropertiesManager.getINSTANCE().getProperty("mongodb-port");
		mongoClient = new MongoClient(mongoPath, Integer.parseInt(mongoPort));
		dataBase = mongoClient.getDatabase(pDataBase);
		collection = dataBase.getCollection(pCollection);
	}

	/**
	 * Obtiene los documento de la coleccion "iot_environment_measurements" que
	 * cumplan la expresion regular por id
	 * 
	 * @param pRegex
	 * @param pPath
	 * @throws IOException
	 */
	public void getDocument(String pRegex, String pPath) throws IOException {
		BasicDBObject regex = new BasicDBObject();
		PrintWriter pw = new PrintWriter(pPath);
		regex.put("_id", pRegex);
		FindIterable<Document> cursor = collection.find(regex);
		MongoCursor<Document> iterator = cursor.iterator();
		while (iterator.hasNext()) {
			Document d = iterator.next();
			pw.println(d.toJson());
			pw.close();
		}
	}

	public static void main(String[] args) throws IOException {
		MongoDBUtils s = new MongoDBUtils("dashboard", "iot_environment_measurements");
		s.getDocument("all:all:environment_airquality:201709:all", "data/enviroment_airquality_201709.json");
	}
}
