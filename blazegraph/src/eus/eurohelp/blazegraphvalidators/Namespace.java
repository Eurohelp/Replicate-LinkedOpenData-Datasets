package eus.eurohelp.blazegraphvalidators;

import java.util.Properties;

import org.openrdf.model.Statement;
import org.openrdf.query.GraphQueryResult;
import org.apache.log4j.Logger;
import com.bigdata.rdf.sail.BigdataSail.Options;
import com.bigdata.rdf.sail.webapp.SD;
import com.bigdata.rdf.sail.webapp.client.RemoteRepositoryManager;

/**
 * @author eurohelp
 *
 */
public class Namespace  {
	public void comprobarCrear(String namespace,String urlEntornoBlaze) throws Exception  {
		
		final RemoteRepositoryManager repositoryManager = new RemoteRepositoryManager(urlEntornoBlaze, false /*useLBS*/);
	
		try{	
			final Properties properties = new Properties();
			properties.setProperty(Options.NAMESPACE, namespace);
			properties.setProperty(Options.TEXT_INDEX, "false");
			properties.setProperty(Options.QUADS, "true");
			properties.setProperty(Options.TRUTH_MAINTENANCE,"false");
			properties.setProperty(Options.AXIOMS_CLASS,"com.bigdata.rdf.axioms.NoAxioms");
			properties.setProperty(Options.DEFAULT_STATEMENT_IDENTIFIERS,"false");
			properties.setProperty(Options. STATEMENT_IDENTIFIERS, "false");
			if(!namespaceExists(namespace, repositoryManager)){
				repositoryManager.createRepository(namespace, properties);
				
			}

		} finally {
			repositoryManager.close();
			Thread.currentThread().interrupt();
		}
	}
	
	/**
	 * Comprueba si existe en namespace en el repositorio remoto del blazegraph
	 * @param namespace
	 * @param repo
	 * @return
	 * @throws Exception
	 */
	private static boolean namespaceExists(final String namespace, final RemoteRepositoryManager repo) throws Exception{
		final GraphQueryResult res = repo.getRepositoryDescriptions();
		try{
			while(res.hasNext()){
				final Statement stmt = res.next();
				if (stmt.getPredicate().toString().equals(SD.KB_NAMESPACE.stringValue())) {
					if(namespace.equals(stmt.getObject().stringValue())){
						return true;
					}
				}
			}
		} finally {
			res.close();
		}
		return false;
	}
	
}
