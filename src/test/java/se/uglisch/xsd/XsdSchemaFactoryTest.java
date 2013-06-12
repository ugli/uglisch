package se.uglisch.xsd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Test;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;

import se.uglisch.resource.Resource;

public class XsdSchemaFactoryTest {

	@Test
	public void newInstance() {
		SchemaFactory newInstance = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		assertEquals(XsdSchemaFactory.class, newInstance.getClass());
	}

	@Test
	public void validateOk() throws Exception {
		SchemaFactory newInstance = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = newInstance.newSchema(Resource.apply("/xsd/xsd_schema_factory/shiporder.xsd"));
		Validator validator = schema.newValidator();
		validator.validate(Resource.apply("/xsd/xsd_schema_factory/shiporderValid.xml"));
	}

	@Test
	public void validateFailure() throws Exception {
		try {
			URL schemaUrl = getClass().getResource("/xsd/xsd_schema_factory/shiporder.xsd");
			InputStream xmlStream = getClass().getResourceAsStream("/xsd/xsd_schema_factory/shiporderUnvalid.xml");
			SchemaFactory newInstance = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = newInstance.newSchema(schemaUrl);
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(xmlStream));
			fail();
		} catch (SAXParseException e) {
			assertEquals("\"quantity\" elements or attributes should have a value of type\"integer\".", e.getMessage());
		}
	}

	@Test
	public void setUnknowProperty() throws SAXNotRecognizedException, SAXNotSupportedException {
		SchemaFactory newInstance = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		newInstance.setProperty("anti-flag", "rules");
	}

	@Test
	public void setUnknowFeature() throws SAXNotRecognizedException, SAXNotSupportedException {
		SchemaFactory newInstance = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		newInstance.setFeature("anti-flag", true);
	}

}
