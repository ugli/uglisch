package se.uglisch.xsd;

import java.io.IOException;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.validation.Schema;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import com.sun.org.apache.xerces.internal.jaxp.validation.XMLSchemaFactory;

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
		XMLSchemaFactory schemaFactory = createFactory();
		Schema schema = schemaFactory.newSchema(schemas);
		schema.newValidator().validate(source);
	}

	private XMLSchemaFactory createFactory() throws SAXNotRecognizedException, SAXNotSupportedException {
		XMLSchemaFactory schemaFactory = new XMLSchemaFactory();
		schemaFactory.setResourceResolver(resourceResolver);
		for (String feature : features.keySet())
			schemaFactory.setFeature(feature, features.get(feature));
		for (String property : properties.keySet())
			schemaFactory.setProperty(property, properties.get(property));
		return schemaFactory;
	}

}
