package se.uglisch.xsd;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import se.uglisch.schematron.SchematronValidationException;
import se.uglisch.schematron.SchematronValidatorCommand;

public class XsdValidator extends Validator {

	private final Source[] schemas;
	private ErrorHandler errorHandler;
	private LSResourceResolver resourceResolver;
	private final Map<String, Boolean> features;
	private Map<String, Object> properties;

	private XsdValidator(Source[] schemas, ErrorHandler errorHandler, LSResourceResolver resourceResolver,
	    Map<String, Boolean> features, Map<String, Object> properties) throws IOException {
		this.schemas = wrapSources(schemas);
		this.errorHandler = errorHandler;
		this.resourceResolver = resourceResolver;
		this.features = features;
		this.properties = properties;
	}

	public static Validator apply(Source[] schemas, ErrorHandler errorHandler, LSResourceResolver resourceResolver,
	    Map<String, Boolean> features, Map<String, Object> properties) {
		try {
			return new XsdValidator(schemas, errorHandler, resourceResolver, features, properties);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void reset() {
	}

	@Override
	public void validate(Source source, Result result) throws SAXException, IOException {
		Source wrapedSource = wrapSource(source);
		try {
			validateWithXerces(wrapedSource);
		} catch (SAXParseException e) {
			List<String> schematronErrors = validateSchematron(wrapedSource);
			if (schematronErrors.isEmpty())
				errorHandler.error(e);
			else
				deligateSchematronErrors(schematronErrors, source);
		}
	}

	private Source[] wrapSources(Source[] sources) throws IOException {
		List<Source> wrapedSources = new LinkedList<Source>();
		for (Source source : sources) {
			wrapedSources.add(wrapSource(source));
		}
		return wrapedSources.toArray(new Source[] {});
	}

	// TODO close streams
	private Source wrapSource(Source source) throws IOException {
		if (source instanceof StreamSource) {
			StreamSource streamSource = (StreamSource) source;
			InputStream inputStream = streamSource.getInputStream();
			Reader reader = streamSource.getReader();
			String systemId = streamSource.getSystemId();
			CharArrayWriter charArrayWriter = new CharArrayWriter();
			if (inputStream != null)
				IOUtils.copy(inputStream, charArrayWriter);
			else if (reader != null)
				IOUtils.copy(reader, charArrayWriter);
			else if (systemId != null)
				IOUtils.copy(new URL(systemId).openStream(), charArrayWriter);
			else
				throw new IllegalStateException("Nothing to read from");
			return new se.uglisch.xslt.Result(charArrayWriter);
		}
		throw new IllegalStateException("Unknown type: " + source.getClass());
	}

	private List<String> validateSchematron(Source source) {
		if (schemas == null || schemas.length == 0)
			throw new IllegalStateException("No schemas");
		if (schemas.length < 1)
			throw new IllegalStateException("Just one schema is supported");
		Source schematronSource = Xsd2SchCommand.apply(schemas[0]).execute();
		return SchematronValidatorCommand.apply(schematronSource, source).execute();
	}

	private void deligateSchematronErrors(List<String> schematronErrors, Source source) throws SAXException {
		for (String message : schematronErrors)
			errorHandler.error(new SchematronValidationException(message, source));
	}

	private void validateWithXerces(Source source) throws SAXException, IOException {
		XercesValidator.apply(source, schemas, resourceResolver, features, properties).validate();
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

}
