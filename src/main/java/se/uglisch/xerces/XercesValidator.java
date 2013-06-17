package se.uglisch.xerces;

import java.io.IOException;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

public class XercesValidator {

	private final Source[] schemas;
	private final LSResourceResolver resourceResolver;
	private final Map<String, Boolean> features;
	private final Map<String, Object> properties;
	private final Source source;

	private XercesValidator(Source source, Source[] schemas, LSResourceResolver resourceResolver,
	    Map<String, Boolean> features, Map<String, Object> properties) {
		this.schemas = schemas;
		this.resourceResolver = resourceResolver;
		this.features = features;
		this.properties = properties;
		this.source = source;
	}

	public static XercesValidator apply(Source source, Source[] schemas, LSResourceResolver resourceResolver,
	    Map<String, Boolean> features, Map<String, Object> properties) {
		return new XercesValidator(source, schemas, resourceResolver, features, properties);
	}

	public void validate() throws SAXException, IOException {
		SchemaFactory schemaFactory = XercesSchemaFactory.apply(resourceResolver, features, properties).create();
		Schema schema = schemaFactory.newSchema(schemas);
		schema.newValidator().validate(source);
	}

}
