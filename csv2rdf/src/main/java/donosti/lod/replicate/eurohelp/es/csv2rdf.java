package donosti.lod.replicate.eurohelp.es;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class csv2rdf {

	public static void main(String[] args) throws IOException {
		Reader in = new FileReader(args[0]);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
		for (CSVRecord record : records) {
			String id = record.get("Nombre");
			System.out.println(id);
		}
	}
}
