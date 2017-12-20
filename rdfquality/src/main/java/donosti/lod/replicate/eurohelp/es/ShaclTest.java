package donosti.lod.replicate.eurohelp.es;

import static org.junit.Assert.*;

import org.junit.Test;
import org.aksw.rdfunit.io.reader.RdfModelReader;
import org.aksw.rdfunit.io.reader.RdfReader;
import org.aksw.rdfunit.io.reader.RdfReaderException;
import org.aksw.rdfunit.io.reader.RdfReaderFactory;
import org.aksw.rdfunit.junit.RdfUnitJunitRunner;
import org.aksw.rdfunit.junit.Schema;
import org.aksw.rdfunit.junit.TestInput;
import org.junit.runner.RunWith;

@RunWith(RdfUnitJunitRunner.class)
@Schema(uri = "calidadaire.ttl")
public class ShaclTest {

	@TestInput
	 public RdfReader getInputData() throws RdfReaderException {
      return new RdfModelReader(
              RdfReaderFactory.createResourceReader(
                      "shacl-calidad-aire.ttl").read());
  }
}
