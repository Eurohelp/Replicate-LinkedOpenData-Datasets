package eus.eurohelp.rdfcreator.parkings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import org.openrdf.repository.Repository;
import org.openrdf.rio.RDFFormat;

import be.ugent.mmlab.rml.core.RMLEngine;
import be.ugent.mmlab.rml.core.StdRMLEngine;
import be.ugent.mmlab.rml.main.Main;
import be.ugent.mmlab.rml.mapdochandler.extraction.std.StdRMLMappingFactory;
import be.ugent.mmlab.rml.mapdochandler.retrieval.RMLDocRetrieval;
import be.ugent.mmlab.rml.model.RMLMapping;
import be.ugent.mmlab.rml.model.dataset.RMLDataset;
import eus.eurohelp.rdfcreator.transformation.Preprocess;

public class CSVToRDF {
	// En primer lugar se le pasara el nombre del csv inicial
	// En segundo lugar del csv tras aplicarle los cambios
	// En tercero lugar se le pasara el path del archivo de configuracion
	// En cuarto lugar la ruta donde se quiera almacenar el archivo
	// resultante con la extensi�n deseada
	public static void main(String[] args) throws IOException {

		// Se ejecuta el preprocesado del CSV en el que se realizaran las
		// modificaciones, adiciones y borrados necesarios para generar el RDF
		Preprocess pprocess = new Preprocess();
		pprocess.CSVpreprocess(args[0], args[1]);
		String[] commandLineValues = { "-m", args[2], "-o", args[3], "-g",
				"http://opendata.euskadi.eus/catalogo/id/calidad-aire-en-euskadi-2017" };
		Main.main(commandLineValues);
		// // Ejecuci�n del archivo RML
		// try {
		// File outputFile = Paths.get(args[3]).toFile();
		// File mapping_file = Paths.get(args[2]).toFile();
		//
		// RMLDocRetrieval mapDocRetrieval = new RMLDocRetrieval();
		// Repository repository =
		// mapDocRetrieval.getMappingDoc(mapping_file.toString(),
		// RDFFormat.TURTLE);
		// StdRMLMappingFactory mappingFactory = new StdRMLMappingFactory();
		// if(repository ==null) {
		// System.err.println("\n||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n"
		// + "|| There is some problem with rml configuration file, please check
		// it. Maybe the problem is the sintax. ||\n"
		// +
		// "||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n");
		// System.exit(1);
		// }
		// RMLMapping mapping = mappingFactory.extractRMLMapping(repository);
		// String graphName = "";
		// Map<String, String> parameters = null;
		// String[] exeTriplesMap = null;
		//
		// String outputFormat = "";
		// if (args[3].toLowerCase().contains("ttl")) {
		// outputFormat = RDFFormat.TURTLE.getName();
		// if (!outputFile.toString().toLowerCase().contains("ttl")) {
		// int init = outputFile.toString().indexOf(".");
		// String newPath = args[2].substring(0, init) + ".ttl";
		// outputFile = Paths.get(newPath).toFile();
		// }
		// }
		// RMLEngine engine = new StdRMLEngine(outputFile.toString());
		//
		// final RMLDataset runningDataset =
		// engine.chooseSesameDataSet("dataset", outputFile.toString(),
		// outputFormat);
		// engine.runRMLMapping(runningDataset, mapping, graphName, parameters,
		// exeTriplesMap);
		//
		// runningDataset.closeRepository();
		//
		// Thread.sleep(2000);
		// System.exit(0);
		//
		// } catch (Exception e) {
		// e.printStackTrace(System.err);
		// }
	}
}