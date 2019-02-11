package eus.eurohelp.rdfcreator.transformation;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Proceso para convertir del <b>BKL_dBizi_usuarios_por_mes_estacion.csv</b>
 * original a csv personalizado. El proceso convierte los nombres de las columna
 * de Euskera a Ingles
 * 
 * @author EuroHelp
 * 
 */
public class Preprocess {
	private static final String COLUMNA_EU_ESTACION = "Estazioa";
	private static final String COLUMNA_EU_LOCALIZACION = "Localizacion";
	private static final String COLUMNA_EU_URTEA = "Urtea";
	private static final String COLUMNA_EU_HILABETEA = "Hilabetea";
	private static final String COLUMNA_EU_IBILBIDE = "Ibilbide_Kopurua";
	private static final String COLUMNA_EU_ERABILTZAILE = "Erabiltzaile_Kopurua";
	private static final String COLUMNA_EU_BATEZ = "Batez_Besteko_Iraupena";

	private static final String COLUMNA_EN_ESTACION = "Station";
	private static final String COLUMNA_EN_URTEA = "Year";
	private static final String COLUMNA_EN_HILABETEA = "Month";
	private static final String COLUMNA_EN_HILABETEA_TEXT = "MonthText";
	private static final String COLUMNA_EN_IBILBIDE = "Number_of_routes";
	private static final String COLUMNA_EN_ERABILTZAILE = "Users_quantity";
	private static final String COLUMNA_EN_BATEZ = "Average_duration";
	private static final String COLUMNA_EN_ENTORNO = "Environment";
	private static final String COLUMNA_EN_PUERTO = "Port";

	private static final String COLUMNA_NOMBREPARAURI = "NombreParaURI";
	private static final String LOCALHOST = "localhost";
	private static final String TWOPOINT = ":";
	private static final String PORT_8080 = "8080";

	private NumberFormat formatter = NumberFormat.getInstance(Locale.ENGLISH);
	

	private CSV csv;

	public void csvPreprocess(String pInitialCSV, String pFinalCSV, String pDominio)
			throws IOException {
		if (null == pDominio || ("").equals(pDominio.trim())) {
			throw new IOException("ARGUMENTS ERROR->No se ha especificado un entorno \n");
		}
		
		String pPuerto="";
		String pDomain=new String(pDominio);
		if(pDominio.trim().contains(LOCALHOST)){
			String[] dominio=pDominio.split(TWOPOINT);
			pPuerto=PORT_8080;
			if(dominio.length>1){
				pDomain=dominio[0];
				pPuerto=dominio[1];
			}
		}
		
		csv = new CSV(pInitialCSV, pFinalCSV, ',');
		// Creamos una nueva columna en la que los nombres de las estaciones
		// estaranseparadas por un guion
		
		List<String> streetName = csv.getColumnValue(COLUMNA_EU_ESTACION);

		streetName = Transformation.getInstance().urlify(streetName);
		csv.addColumn(COLUMNA_NOMBREPARAURI, streetName);

		procesarColumnaAnoDominioPuerto(pDomain, pPuerto);
		procesarColumnaMes();
		procesarColumnaEstaciones();
		procesarColumnaIbilbideKopuruak();
		procesarColumnaErabiltzileKopuruak();
		procesarColumnaBatezBestekoIraupenak();

		deleteColumnCSV(csv);
		// Guardamos los cambios
		csv.saveCSV();
	}

	private void procesarColumnaAnoDominioPuerto(String pDominio, String pPuerto) {
		List<String> ano = new ArrayList<>();
		List<String> localizacion = new ArrayList<>();
		List<String> entorno = new ArrayList<>();
		List<String> puerto = new ArrayList<>();

		for (String string : csv.getColumnValue(COLUMNA_EU_URTEA)) {
			ano.add(string);
			localizacion.add("San-Sebastian");
			entorno.add(pDominio);
			puerto.add(pPuerto);
		}
		csv.addColumn(COLUMNA_EN_URTEA, ano);
		csv.addColumn(COLUMNA_EN_ENTORNO, entorno);
		csv.addColumn(COLUMNA_EN_PUERTO, puerto);
		csv.addColumn(COLUMNA_EU_LOCALIZACION, localizacion);
	}

	private void procesarColumnaMes() {

		List<String> mes = new ArrayList<>();
		List<String> mesText = new ArrayList<>();

		for (String string : csv.getColumnValue(COLUMNA_EU_HILABETEA)) {
			mes.add(string);
			mesText.add(Month.of(Integer.valueOf(string)).toString());

		}
		csv.addColumn(COLUMNA_EN_HILABETEA, mes);
		csv.addColumn(COLUMNA_EN_HILABETEA_TEXT, mesText);
	}

	private void procesarColumnaEstaciones() {
		List<String> estacion = new ArrayList<>();
		for (String string : csv.getColumnValue(COLUMNA_EU_ESTACION)) {
			estacion.add(string);
		}
		csv.addColumn(COLUMNA_EN_ESTACION, estacion);
	}

	private void procesarColumnaIbilbideKopuruak() {
		List<String> ibilbideKopurua = new ArrayList<>();

		for (String string : Transformation.getInstance().quitarPuntos(csv.getColumnValue(COLUMNA_EU_IBILBIDE))) {
			ibilbideKopurua.add(formatter.format(Integer.parseInt(string)));
		}

		csv.addColumn(COLUMNA_EN_IBILBIDE, ibilbideKopurua);

	}

	private void procesarColumnaErabiltzileKopuruak() {
		List<String> erabiltzileKopurua = new ArrayList<>();

		for (String string : Transformation.getInstance().quitarPuntos(csv.getColumnValue(COLUMNA_EU_ERABILTZAILE))) {
			erabiltzileKopurua.add(formatter.format(Integer.parseInt(string)));

		}

		csv.addColumn(COLUMNA_EN_ERABILTZAILE, erabiltzileKopurua);

	}

	private void procesarColumnaBatezBestekoIraupenak() {
		List<String> batezBestekoIraupena = new ArrayList<>();
		for (String string : Transformation.getInstance()
				.quitarPuntos(Transformation.getInstance().quitarComillas(csv.getColumnValue(COLUMNA_EU_BATEZ)))) {
			batezBestekoIraupena.add(formatter.format(Integer.parseInt(string)));
		}
		csv.addColumn(COLUMNA_EN_BATEZ, batezBestekoIraupena);
	}

	/*
	 * Elimina las columnas que no se van a usar.
	 */
	private void deleteColumnCSV(CSV csv) {
		// Eliminamos las columnas originales
		csv.removeColumn(COLUMNA_EU_URTEA);
		csv.removeColumn(COLUMNA_EU_HILABETEA);
		csv.removeColumn(COLUMNA_EU_ESTACION);
		csv.removeColumn(COLUMNA_EU_IBILBIDE);
		csv.removeColumn(COLUMNA_EU_ERABILTZAILE);
		csv.removeColumn(COLUMNA_EU_BATEZ);
	}
}