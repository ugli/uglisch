package se.uglisch.xsd;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Test;
import org.xml.sax.SAXException;

import se.uglisch.resource.Resource;

public class XsdSchemaFactoryTest {

	@Test
	public void newInstance() {
		SchemaFactory newInstance = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		assertEquals(XsdSchemaFactory.class, newInstance.getClass());
	}

	@Test
	public void validate() throws IOException {
		try {
			SchemaFactory newInstance = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = newInstance.newSchema(Resource.apply("/xsd/xsd_schema_factory/shiporder.xsd"));
			Validator validator = schema.newValidator();
			validator.validate(Resource.apply("/xsd/xsd_schema_factory/shiporderValid.xml"));
		} catch (SAXException e) {
			assertEquals(
					"When in a \"item\" element, the element \"title\" should be immediately followed by the element \"note\".",
					e.getMessage());
		}
	}

}
