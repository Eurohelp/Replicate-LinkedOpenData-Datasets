package eus.eurohelp.rdfcreator.calidaddelaire;

import java.io.IOException;

import eus.eurohelp.rdfcreator.mongodb.MongoDBUtils;

/**
 * 
 * @author dmuchuari @05/12/2017
 */
public class GetJsonData {
	// Se le pasara el nombre de la base de datos y la coleccion
	// A continuación la expresión a buscar, y el path donde queremos que se
	// almacenen los json
	public static void main(String[] args) throws Exception {
		String dataBase = args[0];
		String collection = args[1];
		String patternToFind = args[2];
		String fileGeneratedPath = args[3];
		
		MongoDBUtils mongoDB = new MongoDBUtils(dataBase, collection);		
		mongoDB.getDocument(patternToFind, fileGeneratedPath);
		
	}
}
