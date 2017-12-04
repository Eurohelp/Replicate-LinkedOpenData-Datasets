package donosti.lod.replicate.eurohelp.es;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileUtils;
import org.topbraid.shacl.validation.ValidationUtil;
import org.topbraid.spin.util.JenaUtil;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;

public class SHACLValidator {
	/**
	 * El primer parametro corresponder� a los datos
	 * El segundo parametro ser� la configuracion de SHACL sobre esos datos 
	 * El tercero la ruta al fichero que contiene la query a hacerse sobre el report
	 * El cuarto a la ruta donde se almacenara el report
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		String targetFile = args[0];
		String SHACLFile = args[1];
		String reportCheckingQueryFile = args[2];
		String reportFile = args[3];

		// Load the data to validate
		Model dataModel = JenaUtil.createMemoryModel();
		FileInputStream fiData = new FileInputStream(targetFile);
		dataModel.read(fiData, "", FileUtils.langTurtle);
		// Load the quality tests
		Model configurationModel = JenaUtil.createMemoryModel();
		configurationModel.read(new FileInputStream(SHACLFile), "", FileUtils.langTurtle);

		// Create a validation report (execute the tests)
		Resource report = ValidationUtil.validateModel(dataModel, configurationModel, true);

		// Write report to disk
		FileWriter out = new FileWriter(reportFile);
		report.getModel().write(out, "TURTLE");

		// Query report to check if data is conformant
		String reportCheckingQuery = FileUtils.readWholeFileAsUTF8(reportCheckingQueryFile);
		Query query = QueryFactory.create(reportCheckingQuery);
		QueryExecution qexec = QueryExecutionFactory.create(query, report.getModel());
		boolean result = qexec.execAsk();
		qexec.close();

		// Data is not conformant
		if (result) {
			System.out.println("Not valid RDF");
			throw new Exception("SHACL violation: non-conformant RDF, see report at " + reportFile);
		}
		// Conformant data
		else {
			System.out.println("Valid RDF");
		}
	}
}