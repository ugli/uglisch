package se.uglisch.schematron;

import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import javax.xml.validation.ValidatorHandler;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;

import se.uglisch.javax.ValidatorHandlerImpl;

public class Schematron extends Schema {

	private final Source source;
	private ErrorHandler errorHandler;
	private LSResourceResolver resourceResolver;

	private Schematron(Source source, ErrorHandler errorHandler, LSResourceResolver resourceResolver) {
		this.source = source;
		this.errorHandler = errorHandler;
		this.resourceResolver = resourceResolver;
	}

	public static Schematron apply(Source source, ErrorHandler errorHandler, LSResourceResolver resourceResolver) {
		return new Schematron(source, errorHandler, resourceResolver);
	}

	@Override
	public Validator newValidator() {
		return SchematronValidator.apply(source, errorHandler, resourceResolver);
	}

	@Override
	public ValidatorHandler newValidatorHandler() {
		return new ValidatorHandlerImpl(errorHandler, resourceResolver);
	}

}
