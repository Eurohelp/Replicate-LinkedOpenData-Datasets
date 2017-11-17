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
	 * 
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
		int cont = 0;
		PrintWriter pw;
		regex.put("_id", pRegex);
		FindIterable<Document> cursor = collection.find(regex);
		MongoCursor<Document> iterator = cursor.iterator();

		while (iterator.hasNext()) {
			pw = new PrintWriter(pPath.replace(".json", "-" + cont + ".json"));
			Document d = iterator.next();
			cont++;
			pw.println(d.toJson());
			pw.close();
		}
	}
}
