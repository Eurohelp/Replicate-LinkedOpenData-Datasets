package main;

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
import tranformation.Preprocess;

public class CSVToRDF {
	// En primer lugar se le pasara el nombre del csv inicial
	// En segundo lugar se le pasara el path del archivo de configuracion
	// En tercero lugar la ruta donde se quiera almacenar el archivo
	// resultante con la extensi�n deseada
	public static void main(String[] args) throws IOException {

		// Se ejecuta el preprocesado del CSV en el que se realizaran las
		// modificaciones, adiciones y borrados necesarios para generar el RDF
		Preprocess pprocess = new Preprocess(args[0]);
		pprocess.CSVpreprocess();
//		String[] commandLineValues = {"-m", "newdata/txominea.csv", "-o", "resultsMishell.ttl", "-g",
//		"http://opendata.euskadi.eus/catalogo/id/calidad-aire-en-euskadi-2017"};
//		Main.main(commandLineValues); 
		// Ejecuci�n del archivo RML
		try {
			File outputFile = Paths.get(args[2]).toFile();
			File mapping_file = Paths.get(args[1]).toFile();

			RMLDocRetrieval mapDocRetrieval = new RMLDocRetrieval();
			Repository repository = mapDocRetrieval.getMappingDoc(mapping_file.toString(), RDFFormat.TURTLE);
			StdRMLMappingFactory mappingFactory = new StdRMLMappingFactory();
			if(repository ==null) {
				System.err.println("\n||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n"
						+ "|| There is some problem with rml configuration file, please check it. Maybe the problem is the sintax. ||\n"
						+ "||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n");
				System.exit(1);
			}
			RMLMapping mapping = mappingFactory.extractRMLMapping(repository);
			String graphName = "";
			Map<String, String> parameters = null;
			String[] exeTriplesMap = null;

			String outputFormat = "";
			if (args[2].toLowerCase().contains("nquads")) {
				outputFormat = RDFFormat.NQUADS.getName();
			} else if (args[2].toLowerCase().contains("ttl")) {
				outputFormat = RDFFormat.TURTLE.getName();
			} else {
				outputFormat = RDFFormat.RDFXML.getName();
				if (!outputFile.toString().toLowerCase().contains("rdf")) {
					int init = outputFile.toString().indexOf(".");
					String newPath = args[2].substring(0, init) + ".rdf";
					outputFile = Paths.get(newPath).toFile();
				}
			}
			RMLEngine engine = new StdRMLEngine(outputFile.toString());

			final RMLDataset runningDataset = engine.chooseSesameDataSet("dataset", outputFile.toString(),
					outputFormat);
			engine.runRMLMapping(runningDataset, mapping, graphName, parameters, exeTriplesMap);

			runningDataset.closeRepository();

			Thread.sleep(2000);
			System.exit(0);

		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
}