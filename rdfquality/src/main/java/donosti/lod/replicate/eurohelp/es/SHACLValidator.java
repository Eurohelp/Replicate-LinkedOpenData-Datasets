package donosti.lod.replicate.eurohelp.es;

import java.io.File;
import java.io.FileWriter;

import org.apache.commons.io.FileUtils;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.topbraid.shacl.validation.ValidationUtil;

public class SHACLValidator {

	public static void main(String[] args) throws Exception {
		String targetFile = args[0];
		String namedGraphURI = args [1];
		String SHACLFile = args[2];
		String reportCheckingQueryFile = args[3];
		String reportFile = args[4];
		
		// Load the data to validate
		Model model = RDFDataMgr.loadDataset(targetFile).getNamedModel(namedGraphURI);
		
		// Load the quality tests
		Model shacl = ModelFactory.createDefaultModel();
		shacl.read(SHACLFile);

		// Create a validation report (execute the tests)
		Resource report = ValidationUtil.validateModel(model, shacl, true);

		// Write report to disk
		FileWriter out = new FileWriter(reportFile);
		report.getModel().write(out, "TURTLE");

		// Query report to check if data is conformant
		String reportCheckingQuery = FileUtils.readFileToString(new File(reportCheckingQueryFile));
		Query query = QueryFactory.create(reportCheckingQuery);
		QueryExecution qexec = QueryExecutionFactory.create(query, report.getModel());
		boolean result = qexec.execAsk();
		qexec.close();
		
		// Data is not conformant
		if (result) {
			throw new Exception("SHACL violation: non-conformant RDF, see report at " + reportFile);
		}
		// Conformant data
		else{
			System.out.println("Valid RDF");
		}
	}
}
