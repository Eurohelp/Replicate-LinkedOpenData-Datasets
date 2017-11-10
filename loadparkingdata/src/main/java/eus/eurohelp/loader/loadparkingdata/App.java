package eus.eurohelp.loader.loadparkingdata;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;

public class App {
	public static void main(String[] args) {
		System.out.println("Inicio LoadDataParkings load");
		// String downloadURL =
		// "http://www.donostia.eus/info/ciudadano/camaras_trafico.nsf/dameParkings?OpenAgent&idioma=cas";
		String downloadURL = args[0];
		String file_name = args[1];
		String downloadPath = args[2]; // "ODEfiles_mobility_parking";
		String downloadFileName = args[3]; // "parkings.json";
		String charSet = StandardCharsets.ISO_8859_1.name();

		FileManagement fm = new FileManagement(downloadURL, downloadPath, downloadFileName, charSet);

		// Descarga de archivo
		fm.downloadFile();

		// Se obtiene los objetos incidencias
		List<JSONObject> listParkings = fm.getContentOtherMobilityParking();

		System.out.println(
				"Nombre,PlazasRotatorias,Tipo,PlazasResidentes,PlazasTotales,PlazasResidentesLibres,PlazasRotatoriasLibres,Precios,Latitud,Longitud");
		String csv = "Nombre,PlazasRotatorias,Tipo,PlazasResidentes,PlazasTotales,PlazasResidentesLibres,PlazasRotatoriasLibres,Precios,Latitud,Longitud\n";
		
//		System.out.println(
//				"Nombre,PlazasRotatorias,Tipo,PlazasResidentes,PlazasTotales,PlazasResidentesLibres,Precios,Latitud,Longitud");
//		String csv = "Nombre,PlazasRotatorias,Tipo,PlazasResidentes,PlazasTotales,PlazasResidentesLibres,Precios,Latitud,Longitud\n";
		
		for (JSONObject parking : listParkings) {
			if (!(parking.get("Nombre").toString().contains("Estac"))) {
				
				System.out.print(parking.getOrDefault("Nombre", "-") + ",");
				csv += parking.getOrDefault("Nombre", "-") + ",";
				System.out.print(parking.getOrDefault("PlazasRotatorias", "-") + ",");
				csv += parking.getOrDefault("PlazasRotatorias", "-") + ",";
				System.out.print(parking.getOrDefault("Tipo", "-") + ",");
				csv += parking.getOrDefault("Tipo", "-") + ",";
				System.out.print(parking.getOrDefault("PlazasResidentes", "-") + ",");
				csv += parking.getOrDefault("PlazasResidentes", "-") + ",";
				System.out.print(parking.getOrDefault("PlazasTotales", "-") + ",");
				csv += parking.getOrDefault("PlazasTotales", "-") + ",";
				System.out.print(parking.getOrDefault("PlazasResidentesLibres", "-") + ",");
				csv += parking.getOrDefault("PlazasResidentesLibres", "-") + ",";
				
				Pattern pattern = Pattern.compile("\\d{1,3}(?![0-9%]{1,3})");
				Matcher matcher = pattern.matcher((String) parking.getOrDefault("Datos", "-"));
				String rotativePlacesFree = "0";
				if (matcher.find()) {
					rotativePlacesFree = matcher.group();
					System.out.print(rotativePlacesFree + ",");
				}
				csv += rotativePlacesFree + ",";

				String preciosParkings = (String) parking.getOrDefault("Precios", "-");

				preciosParkings = preciosParkings.replaceAll(",", ".");
				preciosParkings = preciosParkings.replaceAll("Precios", "");
				preciosParkings = preciosParkings.replaceAll("</td></tr><tr><td class=\"azul\">", ";");
//				preciosParkings = preciosParkings.replaceAll("</td></tr></table>", ",");
				preciosParkings = preciosParkings.replaceAll(
						"<br/>|<strong>|</strong>|<table>|</table>|<tr>|</tr>|<td>|</td>|<td class=\"azul\">", "");
				
				preciosParkings = preciosParkings.replaceAll(" &#8364;", "");

				if (preciosParkings.isEmpty()) {
					System.out.print("-,");
					csv += "-,";
				} else {
					System.out.print(preciosParkings);
					csv += preciosParkings + ",";
				}

				System.out.print(parking.getOrDefault("Latitud", "-") + ",");
				csv += parking.getOrDefault("Latitud", "-") + ",";
				System.out.print(parking.getOrDefault("Longitud", "-"));
				csv += parking.getOrDefault("Longitud", "-") + "\n";

				System.out.println();
			}
		}

		File file = new File(file_name);
		try {
			FileUtils.writeStringToFile(file, csv);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
