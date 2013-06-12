package se.uglisch.xsd;

import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import javax.xml.validation.ValidatorHandler;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;

import se.uglisch.javax.ValidatorHandlerImpl;

public class Xsd extends Schema {

	private final Source[] schemas;
	private final ErrorHandler errorHandler;
	private final LSResourceResolver resourceResolver;
	private final Map<String, Boolean> features;
	private final Map<String, Object> properties;

	private Xsd(Source[] schemas, ErrorHandler errorHandler, LSResourceResolver resourceResolver,
			Map<String, Boolean> features, Map<String, Object> properties) {
		this.schemas = schemas;
		this.errorHandler = errorHandler;
		this.resourceResolver = resourceResolver;
		this.features = features;
		this.properties = properties;
	}

	public static Schema apply(Source[] schemas, ErrorHandler errorHandler, LSResourceResolver resourceResolver,
			Map<String, Boolean> features, Map<String, Object> properties) {
		return new Xsd(schemas, errorHandler, resourceResolver, features, properties);
	}

	@Override
	public Validator newValidator() {
		return XsdValidator.apply(schemas, errorHandler, resourceResolver, features, properties);
	}

	@Override
	public ValidatorHandler newValidatorHandler() {
		return new ValidatorHandlerImpl(errorHandler, resourceResolver);
	}

}
