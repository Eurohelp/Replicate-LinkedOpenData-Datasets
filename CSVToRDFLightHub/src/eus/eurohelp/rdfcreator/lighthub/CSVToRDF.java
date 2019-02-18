package eus.eurohelp.rdfcreator.lighthub;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import org.openrdf.repository.Repository;
import org.openrdf.rio.RDFFormat;

import be.ugent.mmlab.rml.core.RMLEngine;
import be.ugent.mmlab.rml.core.StdRMLEngine;
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
	// resultante con la extension deseada

	private static final String EXTENSION_TURTLE_FILE = "ttl";
	public static void main(String[] args) throws IOException, InterruptedException  {
		
        // Se ejecuta el preprocesado del CSV en el que se realizaran las
        // modificaciones, adiciones y borrados necesarios para generar el RDF

		Preprocess pprocess = new Preprocess();
		pprocess.csvPreprocess(args[0], args[1], args[4]);
		
		// Ejecucion del archivo RML
		try {
			File outputFile = Paths.get(args[3]).toFile();
			File mappingFile = Paths.get(args[2]).toFile();
			RMLDocRetrieval mapDocRetrieval = new RMLDocRetrieval();
			Repository repository = mapDocRetrieval.getMappingDoc(mappingFile.toString(), RDFFormat.TURTLE);
			StdRMLMappingFactory mappingFactory = new StdRMLMappingFactory();
			if (repository == null) {
				System.out.println("No se ha generado RDF");
				throw new IOException(
						"RML CONFIGURATION FILE SYNTAX ERROR->There is some problem with rml configuration file, please check it. Maybe the problem is the sintax");
			}
			RMLMapping mapping = mappingFactory.extractRMLMapping(repository);
			String graphName = "";
			Map<String, String> parameters = null;
			String[] exeTriplesMap = null;

			String outputFormat = "";
			if (args[3].toLowerCase().contains(EXTENSION_TURTLE_FILE)) {
				outputFormat = RDFFormat.TURTLE.getName();
				if (!outputFile.toString().toLowerCase().contains(EXTENSION_TURTLE_FILE)) {
					int init = outputFile.toString().indexOf('.');
					String newPath = args[2].substring(0, init) + "." + EXTENSION_TURTLE_FILE;
					outputFile = Paths.get(newPath).toFile();
				}
			}
			RMLEngine engine = new StdRMLEngine(outputFile.toString());

			final RMLDataset runningDataset = engine.chooseSesameDataSet("dataset", outputFile.toString(),
					outputFormat);
			engine.runRMLMapping(runningDataset, mapping, graphName, parameters, exeTriplesMap);

			runningDataset.closeRepository();

			if (outputFile.length() == 0) {
				System.out.println("No se ha generado RDF");
				throw new IOException(
						"ARGUMENTS ERROR->There is some problem with RDF Generation. Please check the program arguments \n");
			}

			Thread.sleep(2000);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}
}