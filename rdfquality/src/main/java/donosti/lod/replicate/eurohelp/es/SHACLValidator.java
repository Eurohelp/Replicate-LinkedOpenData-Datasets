package donosti.lod.replicate.eurohelp.es;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.topbraid.shacl.util.ModelPrinter;
import org.topbraid.shacl.validation.ValidationUtil;

public class SHACLValidator {

	public static void main(String[] args) throws Exception {
		String targetFile = args[0];
		String SHACLFile = args[1];
		String reportCheckingQueryFile = args[2];
		String reportFile = args[3];

		Model model = ModelFactory.createDefaultModel();
		model.read(targetFile);

		Model shacl = ModelFactory.createDefaultModel();
		shacl.read(SHACLFile);

		Resource report = ValidationUtil.validateModel(model, shacl, true);

//		System.out.println(ModelPrinter.get().print(report.getModel()));

		FileWriter out = new FileWriter(reportFile);

		report.getModel().write(out, "TURTLE");

		String reportCheckingQuery = FileUtils.readFileToString(new File(reportCheckingQueryFile));
		Query query = QueryFactory.create(reportCheckingQuery);
		QueryExecution qexec = QueryExecutionFactory.create(query, report.getModel());
		boolean result = qexec.execAsk();
		qexec.close();
		if (result) {
			throw new Exception("SHACL violation: non-conformant RDF");
		}
		else{
			System.out.println("Valid RDF");
		}
	}
}
