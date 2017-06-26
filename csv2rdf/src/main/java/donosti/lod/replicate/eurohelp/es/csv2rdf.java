package donosti.lod.replicate.eurohelp.es;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
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
			System.out.println("--->" + Nombre);
			String parking_uri = LOD_DONOSTI_URI.parking.getUri() + transformer.urlify(Nombre);

			// parking rdf:type schema:ParkingFacility
			transformer.addRDFTYPETriple(parking_uri, LOD_DONOSTI_URI.schema_parking_facility.getUri());

			// parking rdfs:label nombre
			transformer.addRDFSLABELTriple(parking_uri, Nombre);

			// parking plazas_rotatorias PlazasRotatorias
			System.out.println(record.get("PlazasRotatorias"));
			transformer.addDataTripleXSDInt(parking_uri, LOD_DONOSTI_URI.PlazasRotatorias.getUri(),
					Integer.parseInt(record.get("PlazasRotatorias")));

			// parking rdf:type TipoParking
			String tipo_parking = record.get("Tipo");
			System.out.println(tipo_parking);
			if (tipo_parking.equals("Rotatorio")) {
				transformer.addRDFTYPETriple(parking_uri, LOD_DONOSTI_URI.tipo_parking_rotatorio.getUri());
			} else if (tipo_parking.equals("Mixto")) {
				transformer.addRDFTYPETriple(parking_uri, LOD_DONOSTI_URI.tipo_parking_mixto.getUri());
			}

			System.out.println(record.get("PlazasResidentes"));
			// parking plazasresidentes PlazasResidentes
			transformer.addDataTripleXSDInt(parking_uri, LOD_DONOSTI_URI.PlazasResidentes.getUri(),
					Integer.parseInt(record.get("PlazasResidentes")));

			System.out.println(record.get("PlazasTotales"));
			// parking plazastotales PlazasTotales
			transformer.addDataTripleXSDInt(parking_uri, LOD_DONOSTI_URI.PlazasTotales.getUri(),
					Integer.parseInt(record.get("PlazasTotales")));

			System.out.println(record.get("PlazasResidentesLibres"));
			// parking plazasresidenteslibres plazasresidenteslibres
			transformer.addDataTripleXSDInt(parking_uri, LOD_DONOSTI_URI.PlazasResidentesLibres.getUri(),
					Integer.parseInt(record.get("PlazasResidentesLibres")));

			System.out.println(record.get("PlazasRotatoriasLibres"));
			// parking plazasrotatoriaslibres PlazasRotatoriasLibres
			transformer.addDataTripleXSDInt(parking_uri, LOD_DONOSTI_URI.PlazasRotatoriasLibres.getUri(),
					Integer.parseInt(record.get("PlazasRotatoriasLibres")));

			System.out.println(record.get("Precios"));

			System.out.println(record.get("Latitud"));
			// parking wgs84_lat Latitud
			// transformer.addDataTripleXSDdouble(
			// parking_uri,
			// LOD_DONOSTI_URI.lat_wgs84.getUri(),
			// Double.parseDouble(record.get("Latitud")));

			System.out.println(record.get("Longitud"));
			// transformer.addDataTripleXSDdouble(
			// parking_uri,
			// LOD_DONOSTI_URI.long_wgs84.getUri(),
			// Double.parseDouble(record.get("Longitud")));
		}

		FileOutputStream out = new FileOutputStream(out_path);
		try {
			Rio.write(transformer.getModel(), out, RDFFormat.RDFXML);
		} finally {
			out.close();
		}
	}
}
