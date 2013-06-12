package se.uglisch.schematron;

import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import se.uglisch.XmlSchemaNsUris;
import se.uglisch.javax.DefaultErrorHandler;
import se.uglisch.javax.ResourceResolverImpl;

public class SchematronSchemaFactory extends SchemaFactory {

	private ErrorHandler errorHandler;
	private LSResourceResolver resourceResolver;

	public SchematronSchemaFactory() {
		errorHandler = new DefaultErrorHandler();
		resourceResolver = new ResourceResolverImpl();
	}

	@Override
	public boolean isSchemaLanguageSupported(String schemaLanguage) {
		return schemaLanguage != null && schemaLanguage.equals(XmlSchemaNsUris.SCHEMATRON_NS_URI);
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
		if (schemas == null || schemas.length == 0)
			throw new IllegalStateException("No schemas");
		if (schemas.length < 1) 
			throw new IllegalStateException("Just one schema is supported");
		return Schematron.apply(schemas[0], errorHandler, resourceResolver);
	}

	@Override
	public final Schema newSchema() throws SAXException {
		throw new UnsupportedOperationException();
	}

}
