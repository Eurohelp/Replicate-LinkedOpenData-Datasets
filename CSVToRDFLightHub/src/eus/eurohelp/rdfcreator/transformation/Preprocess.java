package eus.eurohelp.rdfcreator.transformation;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *  Proceso para convertir del <b>SML_consumo_por_mes_hub.csv</b> original a csv personalizado.
 *  El proceso convierte los nombres de las columna de  Euskera a Ingles
 *  
 *  @author EuroHelp
 *   
 */
public class Preprocess {
	private static final String COLUMNA_EU_HUB="HUB_izena";
	private static final String COLUMNA_EU_URTEA="Urtea";
	private static final String COLUMNA_EU_HILABETEA="Hilabetea";
	private static final String COLUMNA_EU_KILOWATTS="Kontsumoa_KW";
	
	private static final String COLUMNA_EN_ESTACION="Station";
	private static final String COLUMNA_EN_URTEA="Year";
	private static final String COLUMNA_EN_HILABETEA="Month";
	private static final String COLUMNA_EN_HILABETEA_TEXT = "MonthText";
	private static final String COLUMNA_EN_KILOWATTS="Kilowatts";
	private static final String COLUMNA_EN_ENTORNO = "Environment";
	private static final String COLUMNA_EN_PUERTO = "Port";

	private  NumberFormat formatter = NumberFormat.getInstance(Locale.ENGLISH);
	
	
	/**
	 * @param pInitialCSV
	 * @param pFinalCSV
	 * @param pDominio
	 * @throws IOException
	 */
	public void csvPreprocess(String pInitialCSV, String pFinalCSV, String pDominio)
			throws IOException {
		if (null == pDominio || ("").equals(pDominio.trim())) {
			throw new IOException("ARGUMENTS ERROR->No se ha especificado un entorno \n");
		}
		
		String pPuerto=Transformation.getInstance().getPort(pDominio);
		
		
		CSV csv = new CSV(pInitialCSV, pFinalCSV, ',');
		// Creamos una nueva columna en la que los nombres de las estaciones estaran
		// separadas por un guion
		List<String> streetName = csv.getColumnValue(COLUMNA_EU_HUB);
		List<String> localizacion = new ArrayList<>();
		List<String> poligono = new ArrayList<>();
		List<String> ano = new ArrayList<>();
		List<String> mes = new ArrayList<>();
		List<String> mesText = new ArrayList<>();
		List<String> estacion = new ArrayList<>();
		List<String> kilowatt = new ArrayList<>();
		List<String> entorno = new ArrayList<>();
		List<String> puerto = new ArrayList<>();

		
		streetName = Transformation.getInstance().urlify(streetName);
		csv.addColumn("NombreParaURI", streetName);
		List<String> anos = csv.getColumnValue(COLUMNA_EU_URTEA);
		List<String> meses = csv.getColumnValue(COLUMNA_EU_HILABETEA);
		List<String> estaciones = csv.getColumnValue(COLUMNA_EU_HUB);
		List<String> kilowatts = Transformation.getInstance().quitarPuntos(Transformation.getInstance().quitarComillas(csv.getColumnValue(COLUMNA_EU_KILOWATTS)));
		
			
		for (String string : anos) {
			ano.add(string);
			localizacion.add("San-Sebastian");
			poligono.add("Poligono-27");
			entorno.add(Transformation.getInstance().getDomain(pDominio));
			puerto.add(pPuerto);
		}
		for (String string : meses) {
			mes.add(string);
			mesText.add(Month.of(Integer.valueOf(string)).toString());
		
		}
		
		for (String string : estaciones) {
			estacion.add(string);
		
		}
		for (String string : kilowatts) {
			kilowatt.add(formatter.format(Integer.parseInt(string)));
		
		}
		
		// convertimos las columnas a ingles
		csv.addColumn("Localizacion", localizacion);
		csv.addColumn("Poligono", poligono);
		csv.addColumn(COLUMNA_EN_URTEA, ano);
		csv.addColumn(COLUMNA_EN_HILABETEA, mes);
		csv.addColumn(COLUMNA_EN_HILABETEA_TEXT, mesText);
		csv.addColumn(COLUMNA_EN_ESTACION, estacion);
		csv.addColumn(COLUMNA_EN_KILOWATTS, kilowatt);
		csv.addColumn(COLUMNA_EN_ENTORNO, entorno);
		csv.addColumn(COLUMNA_EN_PUERTO, puerto);
	
		// Eliminamos las columnas originales
		csv.removeColumn(COLUMNA_EU_URTEA);
		csv.removeColumn(COLUMNA_EU_HILABETEA);
		csv.removeColumn(COLUMNA_EU_HUB);
		csv.removeColumn(COLUMNA_EU_KILOWATTS);

		// Guardamos los cambios
		csv.saveCSV();
		
	}
}