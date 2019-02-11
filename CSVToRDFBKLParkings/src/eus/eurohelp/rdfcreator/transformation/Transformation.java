package eus.eurohelp.rdfcreator.transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Transformation {

	private static Transformation instance = null;

	/**
	 * Constructora
	 * 
	 * @return
	 */
	public static Transformation getInstance() {
		if (instance == null) {
			instance = new Transformation();
		}
		return instance;
	}

	/**
	 * Le da a la fecha el formato adecuado para ser un tipo de dato xsd:Date en
	 * RDF
	 * 
	 * @param pFecha
	 * @return
	 */
	public List<String> putDateTimeFormat(List<String> pFecha) {
		List<String> newList = new ArrayList<>();
		for (String string : pFecha) {
			string = string.replaceAll("/", "-");
			newList.add(string);
		}
		return newList;
	}

	/**
	 * Al pasarle un string con caracteres especiales, le da el formato adecuado
	 * para que se use en la construccion de una uri
	 * 
	 * @param pData
	 * @return
	 */
	public List<String> urlify(List<String> pData) {
		List<String> newList = new ArrayList<>();
		for (String string : pData) {
			string = string.replaceAll("\\(|\\)|\\s|\\/|\\.|:", "-");
			newList.add(string);
		}
		return newList;
	}

	/**
	 * Obtiene los distintos valores de precios para cada fila del csv de
	 * parkings
	 * 
	 * @param pData
	 * @return
	 */
	public Map<String, String> getTimePriceData(String pData) {
		Map<String, String> data = new HashMap<String, String>();
		String[] prices = pData.split(";");
		for (String priceHour : prices) {
			String[] concretePrice = priceHour.split(":");
			data.put(concretePrice[0], concretePrice[1]);
		}
		return data;
	}

}
