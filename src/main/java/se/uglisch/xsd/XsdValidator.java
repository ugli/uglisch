package se.uglisch.xsd;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.validation.Validator;

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
			Map<String, Boolean> features, Map<String, Object> properties) {
		this.schemas = schemas;
		this.errorHandler = errorHandler;
		this.resourceResolver = resourceResolver;
		this.features = features;
		this.properties = properties;
	}

	public static Validator apply(Source[] schemas, ErrorHandler errorHandler, LSResourceResolver resourceResolver,
			Map<String, Boolean> features, Map<String, Object> properties) {
		return new XsdValidator(schemas, errorHandler, resourceResolver, features, properties);
	}

	@Override
	public void reset() {
	}

	@Override
	public void validate(Source source, Result result) throws SAXException, IOException {
		try {
			validateWithXerces(source);
		} catch (SAXParseException e) {
			List<String> schematronErrors = validateSchematron(source);
			if (schematronErrors.isEmpty())
				errorHandler.error(e);
			else
				deligateSchematronErrors(schematronErrors, source);

		}
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
