package se.uglisch.javax;

import javax.xml.validation.TypeInfoProvider;
import javax.xml.validation.ValidatorHandler;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class ValidatorHandlerImpl extends ValidatorHandler {

	private ErrorHandler errorHandler;
	private LSResourceResolver resourceResolver;
	private ContentHandler receiver;

	public ValidatorHandlerImpl(ErrorHandler errorHandler, LSResourceResolver resourceResolver) {
		this.errorHandler = errorHandler;
		this.resourceResolver = resourceResolver;
		this.receiver = this;
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		if (receiver == this)
			throw new UnsupportedOperationException();
		receiver.setDocumentLocator(locator);
	}

	@Override
	public void startDocument() throws SAXException {
		if (receiver == this)
			throw new UnsupportedOperationException();
		receiver.startDocument();
	}

	@Override
	public void endDocument() throws SAXException {
		if (receiver == this)
			throw new UnsupportedOperationException();
		receiver.endDocument();
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		if (receiver == this)
			throw new UnsupportedOperationException();
		receiver.startPrefixMapping(prefix, uri);
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		if (receiver == this)
			throw new UnsupportedOperationException();
		receiver.endPrefixMapping(prefix);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		if (receiver == this)
			throw new UnsupportedOperationException();
		receiver.startElement(uri, localName, qName, atts);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (receiver == this)
			throw new UnsupportedOperationException();
		receiver.endElement(uri, localName, qName);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (receiver == this)
			throw new UnsupportedOperationException();
		receiver.characters(ch, start, length);
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		if (receiver == this)
			throw new UnsupportedOperationException();
		receiver.ignorableWhitespace(ch, start, length);
	}

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		if (receiver == this)
			throw new UnsupportedOperationException();
		receiver.processingInstruction(target, data);
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		if (receiver == this)
			throw new UnsupportedOperationException();
		receiver.skippedEntity(name);
	}

	@Override
	public void setContentHandler(ContentHandler receiver) {
		this.receiver = receiver;
	}

	@Override
	public ContentHandler getContentHandler() {
		return receiver;
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
	public TypeInfoProvider getTypeInfoProvider() {
		throw new UnsupportedOperationException();
	}

}
