package eus.eurohelp.rdfcreator.transformation;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author dmuchuari
 * @10/01/2018
 */
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
	public List<String> putDateTimeFormat(List<String> pFecha, List<String> pHora) {
		List<String> newList = new ArrayList<>();
		for (String date : pFecha) {
			for (String hour : pHora) {
				String dateTime = date + "T" + hour + ":00Z";
				newList.add(dateTime);
			}
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
	 * Por cada valor de la lista ejecuta el metodo replace
	 * 
	 * @param pData
	 * @param pOldValue
	 * @param pNewValue
	 * @return
	 */
	public List<String> replace(List<String> pData, String pOldValue, String pNewValue) {
		List<String> newList = new ArrayList<>();
		for (String string : pData) {
			string = string.replaceAll(pOldValue, pNewValue);
			newList.add(string);
		}
		return newList;
	}
}
