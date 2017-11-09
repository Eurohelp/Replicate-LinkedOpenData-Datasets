package es.eurohelp.replicate.silk;

import java.io.File;

import de.fuberlin.wiwiss.silk.Silk;

public class Client {

	public static void main(String[] args) {
		//"./data/silk-test.xml"
		File file = new File(args[0]);
		Silk.executeFile(file, null, 8, true);
	}
} 

