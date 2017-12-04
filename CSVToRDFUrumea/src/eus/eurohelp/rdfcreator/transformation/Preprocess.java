package eus.eurohelp.rdfcreator.transformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Preprocess {

	public void CSVpreprocess(String pInitialCSV, String pFinalCSV) throws IOException {
		CSV csv = new CSV(pInitialCSV, pFinalCSV, ',');
		// Creamos una nueva columna en la que los nombres de las calles estarán
		// separadas por un guion
		List<String> dates = csv.getColumnValue("eguna");
		List<String> hours = csv.getColumnValue("ordua");
		List<String> datetimes = Transformation.getInstance().putDateTimeFormat(dates, hours);
		csv.addColumn("horafecha", datetimes);
		hours = Transformation.getInstance().urlify(hours);
		csv.addColumn("horaParaUri", hours);
		List<String> level = csv.getColumnValue("maila");
		level = Transformation.getInstance().replace(level, "m", "");
		csv.addColumn("maila", level);
		csv.saveCSV();
	}
}