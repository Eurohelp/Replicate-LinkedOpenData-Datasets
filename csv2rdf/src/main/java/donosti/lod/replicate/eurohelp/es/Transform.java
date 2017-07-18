package donosti.lod.replicate.eurohelp.es;

import java.math.BigDecimal;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;

public class Transform {
	
	private ValueFactory vf;
	private Model model;
	private String ctxt;
	public Transform(String context) {
		vf = SimpleValueFactory.getInstance();
		model = new TreeModel();
		ctxt = context;
	}

	public String urlify(String st){
		String result = "";
		result += st.replaceAll("\\(|\\)|\\s|\\/|\\.|:","-");
		return result;	
	}
	
	public void addRDFTYPETriple(String subject, String object){
		model.add(vf.createIRI(subject), RDF.TYPE, vf.createIRI(object), vf.createIRI(ctxt));
	}
	
	public void addRDFSLABELTriple(String subject, String label) {
		model.add(vf.createIRI(subject), RDFS.LABEL, vf.createLiteral(label), vf.createIRI(ctxt));
	}
	
	public void addDataTripleXSDInt(String subject, String prop, int value){
		model.add(vf.createIRI(subject), vf.createIRI(prop), vf.createLiteral(value), vf.createIRI(ctxt));
	}
	
	public void addDataTripleXSDdouble(String subject, String prop, double value){
		model.add(vf.createIRI(subject), vf.createIRI(prop), vf.createLiteral(value), vf.createIRI(ctxt));
	}
	
	public void addDataTripleXSDdecimal(String subject, String prop, BigDecimal value){
		model.add(vf.createIRI(subject), vf.createIRI(prop), vf.createLiteral(value), vf.createIRI(ctxt));
	}
	
	public void addDataTripleXSDString(String subject, String prop, String value){
		model.add(vf.createIRI(subject), vf.createIRI(prop), vf.createLiteral(value), vf.createIRI(ctxt));
	}
	
	public void addTriple(String subject, String prop, String object){
		model.add(vf.createIRI(subject), vf.createIRI(prop), vf.createIRI(object), vf.createIRI(ctxt));
	}
	public Model getModel (){
		return model;
	}
}
