package eus.eurohelp.loader.loadparkingdata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Clase para gestionar los datos en crudo de varias fuentes de datos
 *
 * @author acarenas Created at 21 de oct. de 2016
 */
public class FileManagement {

    private String downloadURL;
    private String downloadPath;
    private String downloadFileName;
    private String unzipPath;
    private String differentLinesPathNew;
    private String differentLinesPathOld;
    private String charSet;

    /**
     * 
     * Constructor for FileManagement
     *
     */
    public FileManagement(String downloadURL, String downloadPath, String unzipPath, String differentLinesPathNew,
	    String differentLinesPathOld, String downloadFileName, String charSet) {
	this.downloadURL = downloadURL;
	this.downloadPath = downloadPath;
	this.unzipPath = unzipPath;
	this.differentLinesPathNew = differentLinesPathNew;
	this.differentLinesPathOld = differentLinesPathOld;
	this.downloadFileName = downloadFileName;
	this.charSet = charSet;
    }

    /**
     * 
     * Constructor for FileManagement ODE mobility traffic
     *
     */
    public FileManagement(String downloadURL, String downloadPath, String downloadFileName, String charSet) {
	this.downloadURL = downloadURL;
	this.downloadPath = downloadPath;
	this.downloadFileName = downloadFileName;
	this.charSet = charSet;
    }

    /**
     * Método que obtiene los datos de los aparcamientos a partir de la fuente
     * de datos de donostia Debido a CLRFs en archivo se opta por JSONsimple, el
     * json es pequeño por lo que no sería muy ineficiente remplazar esos
     * caracteres y usar jackson.
     * 
     * @return List<GeolocationIncidence>
     *
     * @author acarenas
     */
    public List<JSONObject> getContentOtherMobilityParking() {

	List<JSONObject> listParkings = new ArrayList<JSONObject>();
	JSONParser parser = new JSONParser();
	JSONArray jsonArray;
	try {
	    File file = new File(this.downloadPath + File.separator + this.downloadFileName);
	    InputStream is = new FileInputStream(file);
	    Reader reader = new InputStreamReader(is, this.charSet);
	    jsonArray = (JSONArray) parser.parse(reader);
	    for (int i = 0; i < jsonArray.size(); i++) {
		listParkings.add((JSONObject) jsonArray.get(i));
	    }
	} catch (FileNotFoundException e) {
	    System.out.println("El archivo " + this.downloadPath + File.separator + this.downloadFileName + " no existe");
	} catch (IOException e) {
	    System.out.println("El archivo " + this.downloadPath + File.separator + this.downloadFileName
		    + " no se ha podido acceder.");
	} catch (ParseException e) {
	    System.out.println("El archivo JSON no se ha podido parsear.");
	}

	return listParkings;
    }

    /**
     * Descarga un archivo en una ruta
     * 
     * @param fileUrl
     *            String
     * @param fileName
     *            String
     *
     * @author acarenas
     */
    public void downloadFile() {
	try {
	    FileUtils.copyURLToFile(new URL(this.downloadURL),
		    new File(this.downloadPath + File.separator + this.downloadFileName));
	    System.out.println(this.downloadURL);
	    System.out.println(this.downloadPath + File.separator + this.downloadFileName);
	} catch (MalformedURLException e) {
	    System.out.println("La URL no es correcta");
	} catch (IOException e) {
	    System.out.println("El archivo no se ha descargado correctamente");
	}
    }

}
