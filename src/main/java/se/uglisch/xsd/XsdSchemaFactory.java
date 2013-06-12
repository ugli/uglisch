package se.uglisch.xsd;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import se.uglisch.XmlSchemaNsUris;
import se.uglisch.javax.DefaultErrorHandler;
import se.uglisch.javax.ResourceResolverImpl;

public final class XsdSchemaFactory extends SchemaFactory {

	private ErrorHandler errorHandler;
	private LSResourceResolver resourceResolver;
	private final Map<String, Boolean> features;
	private final Map<String, Object> properties;

	public XsdSchemaFactory() {
		errorHandler = new DefaultErrorHandler();
		resourceResolver = new ResourceResolverImpl();
		features = new HashMap<String, Boolean>();
		properties = new HashMap<String, Object>();
	}

	@Override
	public final boolean isSchemaLanguageSupported(String schemaLanguage) {
		return schemaLanguage != null && schemaLanguage.equals(XmlSchemaNsUris.W3C_XML_SCHEMA_NS_URI);
	}

	@Override
	public final void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	@Override
	public final ErrorHandler getErrorHandler() {
		return errorHandler;
	}

	@Override
	public final void setResourceResolver(LSResourceResolver resourceResolver) {
		this.resourceResolver = resourceResolver;
	}

	@Override
	public final LSResourceResolver getResourceResolver() {
		return resourceResolver;
	}

	@Override
	public Schema newSchema(Source[] schemas) {
		return Xsd.apply(schemas, errorHandler, resourceResolver, features, properties);
	}

	@Override
	public final Schema newSchema() throws SAXException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
		return properties.get(name);
	}

	@Override
	public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
		if (features.containsKey(name))
			return features.get(name);
		return false;
	}

	@Override
	public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
		features.put(name, value);
	}

	@Override
	public void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
		properties.put(name, object);
	}

}
