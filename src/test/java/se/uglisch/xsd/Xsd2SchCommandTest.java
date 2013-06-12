package se.uglisch.xsd;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

import org.junit.Test;

import se.uglisch.resource.Resource;
import se.uglisch.xslt.Result;

public class Xsd2SchCommandTest {

	@Test
	public void execute() throws Exception {
		Resource originialXsd = Resource.apply("/xsd/xsd2sch/shiporder.xsd");
		Result schematron = Xsd2SchCommand.apply(originialXsd).execute();
		// IOUtils.copy(schematron.getReader(), System.out);
		assertXMLEqual(Resource.apply("/xsd/xsd2sch/generatedShiporder.sch").reader(), schematron.getReader());
	}

}
