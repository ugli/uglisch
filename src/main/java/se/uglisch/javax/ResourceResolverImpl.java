package se.uglisch.javax;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public class ResourceResolverImpl implements LSResourceResolver {

	@Override
	public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
		StringBuilder builder = new StringBuilder();
		builder.append("type: ");
		builder.append(type);
		builder.append(",namespaceURI: ");
		builder.append(namespaceURI);
		builder.append(",publicId: ");
		builder.append(publicId);
		builder.append(",systemId: ");
		builder.append(systemId);
		builder.append(",baseURI: ");
		builder.append(baseURI);
		throw new UnsupportedOperationException(builder.toString());
	}

}
