package donosti.lod.replicate.eurohelp.es;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;

public class Transform {
	
	private ValueFactory vf;
	private Model model;
	public Transform() {
		vf = SimpleValueFactory.getInstance();
		model = new TreeModel();
	}

	public String urlify(String st){
		String result = "";
		result += st.replaceAll("\\(|\\)|\\s|\\/|\\.","-");
		return result;	
	}
	
	public void addRDFTYPETriple(String subject, String object){
		model.add(vf.createIRI(subject), RDF.TYPE, vf.createIRI(object));
	}
	
	public void addRDFSLABELTriple(String subject, String label) {
		model.add(vf.createIRI(subject), RDFS.LABEL, vf.createLiteral(label));
	}
	
	public Model getModel (){
		return model;
	}
}
