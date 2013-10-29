package se.uglisch.schematron;

import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import se.uglisch.schematron.iso.IsoSchema;

public class IsoSchematronFactory extends SchemaFactory {

	public final static String SCHEMATRON_NS_URI = "http://purl.oclc.org/dsdl/schematron";

	private ErrorHandler errorHandler;
	private LSResourceResolver resourceResolver;

	@Override
	public boolean isSchemaLanguageSupported(String schemaLanguage) {
		return schemaLanguage != null
				&& schemaLanguage.equals(SCHEMATRON_NS_URI);
	}

	@Override
	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	@Override
	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}

	@Override
	public void setResourceResolver(LSResourceResolver resourceResolver) {
		this.resourceResolver = resourceResolver;
	}

	@Override
	public LSResourceResolver getResourceResolver() {
		return resourceResolver;
	}

	@Override
	public Schema newSchema(Source[] schemas) throws SAXException {
		if (schemas == null || schemas.length == 0)
			throw new IllegalArgumentException("Need schema");
		else if (schemas.length > 1)
			throw new UnsupportedOperationException(
					"Can't handle more than one schema");
		return IsoSchema.apply(schemas[0], errorHandler, resourceResolver);
	}

	@Override
	public Schema newSchema() throws SAXException {
		throw new UnsupportedOperationException();
	}
}
