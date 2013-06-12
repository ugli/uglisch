package se.uglisch.schematron;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import se.uglisch.XmlSchemaNsUris;

public class SchematronSchemaFactoryTest {

	@Test
	public void test() throws SAXException, IOException {
		try {
			URL schemaUrl = getClass().getResource(
					"/schematron/schematron_validator_command/PurchaseOrder.sch");
			InputStream xmlStream = getClass().getResourceAsStream(
			    "/schematron/schematron_validator_command/PurchaseOrderUnvalid.xml");
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XmlSchemaNsUris.SCHEMATRON_NS_URI);
			Schema schema = schemaFactory.newSchema(schemaUrl);
			schema.newValidator().validate(new StreamSource(xmlStream));
			fail();
		} catch (SAXParseException e) {
			assertEquals("An address should have a state.", e.getMessage());
		}
	}

}
