package eus.eurohelp.rdfcreator.calidaddelaire;

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
/**
 * 
 * @author dmuchuari
 * @05/12/2017
 */
public class CSVToRDF {
	// En primer lugar se le pasara el archivo de configuración
	// En segundo lugar el path donde se almacenara el RDF generado
	public static void main(String[] args) throws IOException {

		
		// Ejecucion del archivo RML
		try {
			File mapping_file = Paths.get(args[0]).toFile();

			File outputFile = Paths.get(args[1]).toFile();
			RMLDocRetrieval mapDocRetrieval = new RMLDocRetrieval();
			Repository repository = mapDocRetrieval.getMappingDoc(mapping_file.toString(), RDFFormat.TURTLE);
			StdRMLMappingFactory mappingFactory = new StdRMLMappingFactory();
			if (repository == null) {
				System.out.println("No se ha generado RDF");
				throw new Exception("RML CONFIGURATION FILE SYNTAX ERROR->There is some problem with rml configuration file, please check it. Maybe the problem is the sintax");
			}
			RMLMapping mapping = mappingFactory.extractRMLMapping(repository);

			String outputFormat = RDFFormat.TURTLE.toString();
			
				if (!outputFile.toString().toLowerCase().contains("ttl")) {
					int init = outputFile.toString().indexOf(".");
					String newPath = args[1].substring(0, init) + ".ttl";
					outputFile = Paths.get(newPath).toFile();
				}
			
			RMLEngine engine = new StdRMLEngine(outputFile.toString());

			final RMLDataset runningDataset = engine.chooseSesameDataSet("dataset", outputFile.toString(),
					outputFormat);
			engine.runRMLMapping(runningDataset, mapping, "", null, null);

			runningDataset.closeRepository();

			if (outputFile.length() == 0) {
				System.out.println("No se ha generado RDF");
				throw new Exception("ARGUMENTS ERROR->There is some problem with RDF Generation. Please check the program arguments \n");
			}
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
}