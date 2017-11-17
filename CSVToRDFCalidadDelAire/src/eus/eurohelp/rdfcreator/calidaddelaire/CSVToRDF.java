package eus.eurohelp.rdfcreator.calidaddelaire;

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

/**
 * 
 * @author dmuchuari 
 * @17/11/2017
 */
public class CSVToRDF {
	// En primer lugar se le pasara el path del archivo de configuracion
	// En segundo lugar la ruta donde se quiera almacenar el archivo
	// resultante con la extensi�n deseada
	// En tercero lugar el nombre del grafo
	public static void main(String[] args) throws IOException {

		// Ejecucion del archivo RML
		try {
			File outputFile = Paths.get(args[1]).toFile();

			RMLDocRetrieval mapDocRetrieval = new RMLDocRetrieval();
			Repository repository = mapDocRetrieval.getMappingDoc(args[0], RDFFormat.TURTLE);

			StdRMLMappingFactory mappingFactory = new StdRMLMappingFactory();
			if (repository == null) {
				System.err.println(
						"\n||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n"
								+ "|| There is some problem with rml configuration file, please check it. Maybe the problem is the sintax. ||\n"
								+ "||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n");
				System.exit(1);
			}
			RMLMapping mapping = mappingFactory.extractRMLMapping(repository);

			String graphName = args[2];
			Map<String, String> parameters = null;
			String[] exeTriplesMap = null;

			String outputFormat = "";
			if (args[1].toLowerCase().contains("nquads")) {
				outputFormat = RDFFormat.NQUADS.getName();
			} else if (args[1].toLowerCase().contains("ttl")) {
				outputFormat = RDFFormat.TURTLE.getName();
			} else {
				outputFormat = RDFFormat.RDFXML.getName();
				if (!outputFile.toString().toLowerCase().contains("rdf")) {
					int init = outputFile.toString().indexOf(".");
					String newPath = args[1].substring(0, init) + ".rdf";
					outputFile = Paths.get(newPath).toFile();
				}
			}
			RMLEngine engine = new StdRMLEngine(outputFile.toString());

			final RMLDataset runningDataset = engine.chooseSesameDataSet("dataset", outputFile.toString(),
					outputFormat);
			engine.runRMLMapping(runningDataset, mapping, graphName, parameters, exeTriplesMap);

			runningDataset.closeRepository();

			Thread.sleep(2000);

		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
}