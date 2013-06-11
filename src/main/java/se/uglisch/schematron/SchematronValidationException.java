package se.uglisch.schematron;

import javax.xml.transform.Source;

import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

public class SchematronValidationException extends SAXParseException {

	private static final long serialVersionUID = 5059915766889864383L;

	public SchematronValidationException(String message, Source source) {
		super(message, new SchematronLocator(source));
	}

	static class SchematronLocator implements Locator {

		final String systemId;

		public SchematronLocator(Source source) {
			systemId = source.getSystemId();
		}

		@Override
		public String getPublicId() {
			return null;
		}

		@Override
		public String getSystemId() {
			return systemId;
		}

		@Override
		public int getLineNumber() {
			return 0;
		}

		@Override
		public int getColumnNumber() {
			return 0;
		}

	}

}
