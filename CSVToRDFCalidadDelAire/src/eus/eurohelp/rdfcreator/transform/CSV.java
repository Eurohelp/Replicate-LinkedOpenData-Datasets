package eus.eurohelp.rdfcreator.transform;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

public class CSV {
	private Map<String, List<String>> csvValoresIniciales;
	private Map<Integer, List<String>> csvValoresFinales;
	private CsvReader csvReader;
	private CsvWriter csvWriter;
	private String[] headers;

	/**
	 * 
	 * @param pCSVInicial
	 *            corresponde a la ruta donde se encontrara el archivo inicial
	 * @param pCSVFinal
	 *            corresponde a la ruta donde se almacenara el archivo final
	 * @param pDelimiter
	 *            corresponde al elemento que se usara para delimitar los datos,
	 *            p.e. ','
	 * @throws IOException
	 */
	public CSV(String pCSVInicial, char pDelimiter) throws IOException {
		csvReader = new CsvReader(pCSVInicial);
		csvWriter = new CsvWriter(new FileWriter("newdata/parkings.csv"), pDelimiter);
		csvValoresIniciales = new HashMap<String, List<String>>();
		csvValoresFinales = new HashMap<Integer, List<String>>();
		getCSVData();
	}

	/**
	 * Metodo "interno" actualiza los valores de las cabeceras del csv
	 */
	private void updateHeaders() {
		headers = new String[csvValoresIniciales.size()];
		int cont = 0;
		for (String key : csvValoresIniciales.keySet()) {
			headers[cont] = key;
			cont++;
		}
	}

	/**
	 * Elimina la columna cuyo nombre se le pasa por parametro
	 * 
	 * @param pColumnName
	 */
	public void removeColumn(String pColumnName) {
		csvValoresIniciales.remove(pColumnName);
		updateHeaders();
	}

	/**
	 * A�ade una nueva columna con sus respectivos valores
	 * 
	 * @param pColumnName
	 *            el nombre de la columna
	 * @param columnValues
	 *            sus valores
	 */
	public void addColumn(String pColumnName, List<String> columnValues) {
		csvValoresIniciales.put(pColumnName, columnValues);
		updateHeaders();
	}

	/**
	 * Obtiene los datos del csv parseandolo a un formato para trabajar
	 * internamente con el
	 * 
	 * @throws IOException
	 */
	private void getCSVData() throws IOException {
		csvReader.readHeaders();
		int cont = 0;
		headers = csvReader.getHeaders();
		boolean firsTime = true;
		List<String> aux;
		while (csvReader.readRecord()) {
			String[] raw = csvReader.getRawRecord().split(",");
			if (cont == csvReader.getHeaderCount()) {
				cont = 0;
			}
			if (firsTime) {
				firsTime = false;
				while (cont < csvReader.getHeaderCount()) {
					aux = new ArrayList<String>();
					aux.add(raw[cont]);
					csvValoresIniciales.put(headers[cont], aux);
					cont++;
				}
			} else {
				while (cont < csvReader.getHeaderCount()) {
					aux = csvValoresIniciales.get(headers[cont]);
					aux.add(raw[cont]);
					csvValoresIniciales.put(headers[cont], (ArrayList<String>) aux);
					cont++;
				}
			}
			cont = 0;
		}
	}

	/**
	 * Metodo construye una nueva estructura para el CSV para su posterior
	 * conversion a un archivo ".csv"
	 * 
	 * @return
	 */
	private Map<Integer, List<String>> constructNewCSV() {
		int cont = 0;
		int numFilas = csvValoresIniciales.get(headers[0]).size();
		int finalValue = headers.length * numFilas;
		List<String> raw;
		Integer rawCont = 0;
		int contadorCabeceras = 0;
		while (cont < finalValue) {
			if (cont % headers.length == 0 && cont != 0) {
				contadorCabeceras += 1;
			}
			for (String cabecera : headers) {
				List<String> columnValues = csvValoresIniciales.get(cabecera);
				if (!csvValoresFinales.containsKey(rawCont)) {
					raw = new ArrayList<String>();
					String value = columnValues.get(contadorCabeceras);
					raw.add(value);
					csvValoresFinales.put(rawCont, raw);
					cont++;
				} else {
					raw = csvValoresFinales.get(rawCont);
					String value = columnValues.get(contadorCabeceras);
					raw.add(value);
					csvValoresFinales.put(rawCont, raw);
					cont++;
				}
			}
			rawCont++;
		}
		return csvValoresFinales;
	}

	/**
	 * Cambia los nombres de las columnas
	 * 
	 * @param pNewValues
	 */
	public void changeHeadersNames(String[] pNewValues) {
		headers = pNewValues;
		updateHeaders();
	}

	/**
	 * Cambia los valores de una columna
	 * 
	 * @param pHeaderName
	 * @param pNewValues
	 * @return
	 */
	public boolean changeHeaderValue(String pHeaderName, List<String> pNewValues) {
		boolean result = false;
		if (csvValoresIniciales.containsKey(pHeaderName)) {
			csvValoresIniciales.put(pHeaderName, pNewValues);
			result = true;
		}
		return result;
	}

	/**
	 * Metodo guarda todos los cambios realizados en el CSV y genera el archivo
	 * ".csv" que tendr� aplicados esos cambios
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean saveCSV() throws IOException {
		boolean result = false;
		// Empezamos escribiendo las cabeceras
		Set<String> headers = csvValoresIniciales.keySet();
		csvWriter.writeRecord(headers.toArray(new String[headers.size()]));
		// Construimos el nuevo CSV
		constructNewCSV();
		Iterator<Entry<Integer, List<String>>> iterator = csvValoresFinales.entrySet().iterator();
		while (iterator.hasNext()) {
			csvWriter.writeRecord(iterator.next().getValue().toArray(new String[headers.size()]));
		}
		csvWriter.endRecord();
		csvWriter.close();
		return result;
	}

	/**
	 * Devuelve los valores de una determinada columna
	 * 
	 * @param columnName
	 * @return
	 */
	public List<String> getColumnValue(String columnName) {
		return csvValoresIniciales.get(columnName);
	}

	/**
	 * Cambia los valores de una columna
	 * 
	 * @param columnName
	 * @param columnValues
	 */
	public void setColumnValue(String columnName, List<String> columnValues) {
		csvValoresIniciales.put(columnName, columnValues);
	}

}