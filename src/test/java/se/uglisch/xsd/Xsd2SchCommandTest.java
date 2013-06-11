package se.uglisch.xsd;

import org.custommonkey.xmlunit.XMLTestCase;
import org.junit.Test;

import se.uglisch.resource.Resource;
import se.uglisch.xslt.Result;

public class Xsd2SchCommandTest extends XMLTestCase {

	@Test
	public void execute() throws Exception {
		Resource originialXsd = Resource.apply("/xsd/xsd2sch/shiporder.xsd");
		Result schematron = Xsd2SchCommand.apply(originialXsd).execute();
		assertXMLEqual(Resource.apply("/xsd/xsd2sch/generatedShiporder.sch").reader(), schematron.getReader());
	}

}
