package eus.eurohelp.rdfcreator.transformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Preprocess {
	private String initialCsvName;
	private char delimiter;

	public Preprocess(String pInitialName) {
		initialCsvName = pInitialName;
		delimiter = ',';
	}

	public void CSVpreprocess() throws IOException {
		CSV csv = new CSV(initialCsvName, delimiter);
		// Creamos una nueva columna en la que los nombres de las calles estarán
		// separadas por un guion
		List<String> streetName = csv.getColumnValue("Nombre");
		streetName = Transformation.getInstance().urlify(streetName);
		csv.addColumn("NombreParaURI", streetName);
		List<String> prices = csv.getColumnValue("Precios");
		// Por cada fila de precios hay que crear nuevas columnas en las que
		// almacenar los pares tipo
		// precio x min - precio
		List<String> precio15min = new ArrayList<String>();
		List<String> precio30min = new ArrayList<String>();
		List<String> precio60min = new ArrayList<String>();
		List<String> precio90min = new ArrayList<String>();
		List<String> precio120min = new ArrayList<String>();
		List<String> precio180min = new ArrayList<String>();
		List<String> precio300min = new ArrayList<String>();
		List<String> localizacion = new ArrayList<String>();
		for (String string : prices) {
			Map<String, String> data = Transformation.getInstance().getTimePriceData(string);
			precio15min.add(data.get("15 min"));
			precio30min.add(data.get("30 min"));
			precio60min.add(data.get("60 min"));
			precio90min.add(data.get("90 min"));
			precio120min.add(data.get("120 min"));
			precio180min.add(data.get("180 min"));
			precio300min.add(data.get("300 min"));
			localizacion.add("San-sebastian");
		}
		csv.addColumn("Localizacion", localizacion);
		csv.addColumn("Precio15Min", precio15min);
		csv.addColumn("Precio30Min", precio30min);
		csv.addColumn("Precio60Min", precio60min);
		csv.addColumn("Precio90Min", precio90min);
		csv.addColumn("Precio120Min", precio120min);
		csv.addColumn("Precio180Min", precio180min);
		csv.addColumn("Precio300Min", precio300min);
		// Eliminamos la columna precios original
		csv.removeColumn("Precios");
		// Guardamos los cambios
		csv.saveCSV();
	}
}