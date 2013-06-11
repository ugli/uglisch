package se.uglisch.xsd;

import javax.xml.transform.Source;

import se.uglisch.resource.Resource;
import se.uglisch.xslt.Result;
import se.uglisch.xslt.XsltTransform;

public final class Xsd2SchCommand {

	private final Source originialXsd;
	private final Resource includeXslt;
	private final Resource flattenXslt;
	private final Resource expandXlst;
	private final Resource xsd2schXslt;

	private Xsd2SchCommand(Source originialXsd) {
		this.originialXsd = originialXsd;
		this.includeXslt = Resource.apply("/xsd2sch/code/include.xsl");
		this.flattenXslt = Resource.apply("/xsd2sch/code/flatten.xsl");
		this.expandXlst = Resource.apply("/xsd2sch/code/expand.xsl");
		this.xsd2schXslt = Resource.apply("/xsd2sch/code/xsd2sch.xsl");
		// this.compessXslt = Resource.apply("/xsd2sch/code/compress.xsl");
	}

	public static Xsd2SchCommand apply(Source xsd) {
		return new Xsd2SchCommand(xsd);
	}

	public final Result execute() {
		final Result includedXsd = XsltTransform.apply(includeXslt, originialXsd).transform();
		final Result flattenedXsd = XsltTransform.apply(flattenXslt, includedXsd).transform();
		final Result expandedXsd = XsltTransform.apply(expandXlst, flattenedXsd).transform();
		final Result schematron = XsltTransform.apply(xsd2schXslt, expandedXsd).transform();
		// XsltTransform.apply(compessXslt, schematron).transformToCharArray();
		return schematron;
	}

}
