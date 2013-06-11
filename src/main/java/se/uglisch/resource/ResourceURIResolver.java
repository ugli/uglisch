package se.uglisch.resource;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;

public class ResourceURIResolver implements URIResolver {

	private final String resourceBase;

	public ResourceURIResolver(String resourceBase) {
		if (resourceBase == null)
			throw new IllegalArgumentException("Can't be null");
		this.resourceBase = resourceBase;
	}

	@Override
	public Source resolve(String href, String base) throws TransformerException {
		return Resource.apply(resourceBase + href);
	}

}
