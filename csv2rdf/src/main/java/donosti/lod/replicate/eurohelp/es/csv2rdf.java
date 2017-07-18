package donosti.lod.replicate.eurohelp.es;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

public class csv2rdf {

	public static void main(String[] args) throws IOException {
		String in_path = args[0]; // Input CSV path
		String out_path = args[1];// Output RDF path
		String ctxt = args[2]; // Named Graph URI

		Reader in = new FileReader(in_path);

		Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);

		Transform transformer = new Transform(ctxt);

		// EL CSV es deforme!WTF!A veces tiene plazas rotatorias libres a veces
		// no
		for (CSVRecord record : records) {
			int columnas = record.size();
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

			if (columnas == 10) {
				System.out.println("PlazasRotatoriasLibres " + record.get("PlazasRotatoriasLibres"));
				// parking plazasrotatoriaslibres PlazasRotatoriasLibres
				transformer.addDataTripleXSDInt(parking_uri, LOD_DONOSTI_URI.PlazasRotatoriasLibres.getUri(),
						Integer.parseInt(record.get("PlazasRotatoriasLibres")));

				String precios = record.get("Precios");
				System.out.println("Precios " + precios);
				String[] lista_precio_tiempos = precios.split(";");
				for (String precio_tiempo : lista_precio_tiempos) {
					String tiempo = precio_tiempo.split(":")[0].replace(" min", "");
					String precio = precio_tiempo.split(":")[1];
					System.out.println(precio_tiempo + precio + tiempo);
					String uri_precio_tiempo = LOD_DONOSTI_URI.fare.getUri() + transformer.urlify(Nombre) + "/" + transformer.urlify(precio_tiempo);
					System.out.println("uri_precio_tiempo " + uri_precio_tiempo);
					transformer.addTriple(
							parking_uri, 
							LOD_DONOSTI_URI.schema_offers.getUri(), 
							uri_precio_tiempo);
					
					transformer.addDataTripleXSDdecimal(
							uri_precio_tiempo, 
							LOD_DONOSTI_URI.time_minutes.getUri(), 
							new BigDecimal(tiempo));
					
					transformer.addDataTripleXSDdouble(
							uri_precio_tiempo, 
							LOD_DONOSTI_URI.schema_price.getUri(), 
							Double.parseDouble(precio));
					
					transformer.addDataTripleXSDString(
							uri_precio_tiempo, 
							LOD_DONOSTI_URI.schema_currency.getUri(), 
							"EUR");
				}

				System.out.println("Latitud " + record.get("Latitud"));
				// parking wgs84_lat Latitud
				transformer.addDataTripleXSDdouble(parking_uri, LOD_DONOSTI_URI.lat_wgs84.getUri(),
						Double.parseDouble(record.get("Latitud")));

				System.out.println("Longitud " + record.get("Longitud"));
				transformer.addDataTripleXSDdouble(parking_uri, LOD_DONOSTI_URI.long_wgs84.getUri(),
						Double.parseDouble(record.get("Longitud")));

			} else if (columnas == 11) {
				System.out.println("PlazasRotatoriasLibres " + record.get(7));
				// parking plazasrotatoriaslibres PlazasRotatoriasLibres
				transformer.addDataTripleXSDInt(parking_uri, LOD_DONOSTI_URI.PlazasRotatoriasLibres.getUri(),
						Integer.parseInt(record.get(7)));

//				System.out.println("Precios " + record.get(8));
				String precios = record.get(8);
				System.out.println("Precios " + precios);
				String[] lista_precio_tiempos = precios.split(";");
				for (String precio_tiempo : lista_precio_tiempos) {
					String tiempo = precio_tiempo.split(":")[0].replace(" min", "");
					String precio = precio_tiempo.split(":")[1];
					System.out.println(precio_tiempo + precio + tiempo);
					String uri_precio_tiempo = LOD_DONOSTI_URI.fare.getUri() + transformer.urlify(Nombre) + "/" + transformer.urlify(precio_tiempo);
					System.out.println("uri_precio_tiempo " + uri_precio_tiempo);
					transformer.addTriple(
							parking_uri, 
							LOD_DONOSTI_URI.schema_offers.getUri(), 
							uri_precio_tiempo);
					
					transformer.addDataTripleXSDdecimal(
							uri_precio_tiempo, 
							LOD_DONOSTI_URI.time_minutes.getUri(), 
							new BigDecimal(tiempo));
					
					transformer.addDataTripleXSDdouble(
							uri_precio_tiempo, 
							LOD_DONOSTI_URI.schema_price.getUri(), 
							Double.parseDouble(precio));
					
					transformer.addDataTripleXSDString(
							uri_precio_tiempo, 
							LOD_DONOSTI_URI.schema_currency.getUri(), 
							"EUR");
				}

				System.out.println("Latitud " + record.get(9));
				// parking wgs84_lat Latitud
				transformer.addDataTripleXSDdouble(parking_uri, LOD_DONOSTI_URI.lat_wgs84.getUri(),
						Double.parseDouble(record.get(9)));

				System.out.println("Longitud " + record.get(10));
				transformer.addDataTripleXSDdouble(parking_uri, LOD_DONOSTI_URI.long_wgs84.getUri(),
						Double.parseDouble(record.get(10)));
			}
		}

		FileOutputStream out = new FileOutputStream(out_path);
		try {
			Rio.write(transformer.getModel(), out, RDFFormat.NQUADS);
		} finally {
			out.close();
		}
	}
}
