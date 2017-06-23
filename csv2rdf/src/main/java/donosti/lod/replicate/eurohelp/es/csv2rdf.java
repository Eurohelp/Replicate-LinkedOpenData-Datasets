package donosti.lod.replicate.eurohelp.es;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

public class csv2rdf {

	public static void main(String[] args) throws IOException {
		String in_path = args[0]; // Input CSV path
		String out_path = args[1];// Output RDF path
		
		Reader in = new FileReader(in_path); 
		
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);

		Transform transformer = new Transform();


		

		for (CSVRecord record : records) {
			String Nombre = record.get("Nombre");
			

			String parking_uri = LOD_DONOSTI_URI.parking.getUri() + transformer.urlify(Nombre);
			
//			parking rdf:type schema:ParkingFacility
			transformer.addRDFTYPETriple(parking_uri, "http://schema.org/ParkingFacility");
			
//			parking rdfs:label nombre
			transformer.addRDFSLABELTriple(parking_uri, Nombre);

			// System.out.println(record.get("PlazasRotatorias"));
			// System.out.println(record.get("Tipo"));
			// System.out.println(record.get("PlazasResidentes"));
			// System.out.println(record.get("PlazasTotales"));
			// System.out.println(record.get("PlazasResidentesLibres"));
			// System.out.println(record.get("PlazasRotatoriasLibres"));
			// System.out.println(record.get("Precios"));
			// System.out.println(record.get("Latitud"));
			// System.out.println(record.get("Longitud"));
		}

		FileOutputStream out = new FileOutputStream(out_path);
		try {
			Rio.write(transformer.getModel(), out, RDFFormat.RDFXML);
		} finally {
			out.close();
		}
	}
}
