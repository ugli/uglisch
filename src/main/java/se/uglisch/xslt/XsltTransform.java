package se.uglisch.xslt;

import java.io.CharArrayWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import se.uglisch.resource.ResourceURIResolver;

public class XsltTransform {

	private final TransformerFactory transformerFactory;
	private final Source xsltReource;
	private final Source xmlSource;

	private XsltTransform(Source xsltReource, Source xmlSource, String resourceBase) {
		this.xsltReource = xsltReource;
		this.xmlSource = xmlSource;
		transformerFactory = TransformerFactory.newInstance();
		if (resourceBase != null)
			transformerFactory.setURIResolver(new ResourceURIResolver(resourceBase));
	}

	public static XsltTransform apply(Source xsltReource, Source xmlSource) {
		return new XsltTransform(xsltReource, xmlSource, null);
	}

	public static XsltTransform apply(Source xsltReource, Source xmlSource, String resourceBase) {
		return new XsltTransform(xsltReource, xmlSource, resourceBase);
	}

	public Result transform() {
		try {
			Transformer transformer = transformerFactory.newTransformer(xsltReource);
			CharArrayWriter writer = new CharArrayWriter();
			StreamResult outputTarget = new StreamResult(writer);
			transformer.transform(xmlSource, outputTarget);
			return new Result(writer);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
	}

}
