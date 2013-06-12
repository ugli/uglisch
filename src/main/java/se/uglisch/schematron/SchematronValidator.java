package se.uglisch.schematron;

import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.validation.Validator;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

public class SchematronValidator extends Validator {

	private Source schemaSource;
	private ErrorHandler errorHandler;
	private LSResourceResolver resourceResolver;

	private SchematronValidator(Source schemaSource, ErrorHandler errorHandler, LSResourceResolver resourceResolver) {
		this.schemaSource = schemaSource;
		this.errorHandler = errorHandler;
		this.resourceResolver = resourceResolver;
	}

	public static SchematronValidator apply(Source schemaSource, ErrorHandler errorHandler,
	    LSResourceResolver resourceResolver) {
		return new SchematronValidator(schemaSource, errorHandler, resourceResolver);
	}

	@Override
	public void reset() {
	}

	@Override
	public void validate(Source xmlSource, Result result) throws SAXException {
		List<String> errors = SchematronValidatorCommand.apply(schemaSource, xmlSource).execute();
		for (String error : errors) {
			errorHandler.error(new SchematronValidationException(error, xmlSource));
		}
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
