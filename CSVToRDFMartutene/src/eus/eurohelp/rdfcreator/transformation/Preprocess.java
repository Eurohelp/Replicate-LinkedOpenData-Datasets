package eus.eurohelp.rdfcreator.transformation;

import java.io.IOException;

import java.util.List;
/**
 * 
 * @author dmuchuari
 * @10/01/2018
 */
public class Preprocess {
	public void CSVpreprocess(String pInitialCSV, String pFinalCSV) throws IOException {
		CSV csv = new CSV(pInitialCSV, pFinalCSV, ',');
		// Creamos una nueva columna en la que los nombres de las calles estar�n
		// separadas por un guion
		List<String> dates = csv.getColumnValue("fecha");
		List<String> hours = csv.getColumnValue("hora");
		List<String> datetimes = Transformation.getInstance().putDateTimeFormat(dates, hours);
		csv.addColumn("horafecha", datetimes);
		hours = Transformation.getInstance().urlify(hours);
		csv.addColumn("horaParaUri", hours);
		List<String> level = csv.getColumnValue("nivel");
		level = Transformation.getInstance().replace(level, "m", "");
		csv.addColumn("nivel", level);
		List<String> temperature = csv.getColumnValue("nivel");
		temperature = Transformation.getInstance().replace(temperature, "ºC", "");
		csv.addColumn("temperatura", temperature);
		csv.saveCSV();
	}
}