package se.uglisch.schematron;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.transform.Source;

import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmItem;
import se.uglisch.resource.Resource;
import se.uglisch.xslt.Result;
import se.uglisch.xslt.XsltTransform;

public class SchematronValidatorCommand {

	private final Source schema;
	private final Source xml;
	private final Resource xsltInclude;
	private final Resource xsltExpand;
	private final Resource xsltCompile;

	private SchematronValidatorCommand(Source schema, Source xml) {
		this.schema = schema;
		this.xml = xml;
		xsltInclude = Resource.apply("/schematron/code/iso_dsdl_include.xsl");
		xsltExpand = Resource.apply("/schematron/code/iso_abstract_expand.xsl");
		xsltCompile = Resource.apply("/schematron/code/iso_svrl_for_xslt2.xsl");
	}

	public static SchematronValidatorCommand apply(Source schema, Source xml) {
		return new SchematronValidatorCommand(schema, xml);
	}

	public List<String> execute() {
		Source transformedSchema = XsltTransform.apply(xsltInclude, schema, "/schematron/code/").transform();
		transformedSchema = XsltTransform.apply(xsltExpand, transformedSchema, "/schematron/code/").transform();
		transformedSchema = XsltTransform.apply(xsltCompile, transformedSchema, "/schematron/code/").transform();
		Result validation = XsltTransform.apply(transformedSchema, xml).transform();
		return createMessages(validation);

	}

	private static List<String> createMessages(Result result) {
		try {
			Processor processor = new Processor(false);
			XPathCompiler xPathCompiler = processor.newXPathCompiler();
			DocumentBuilder builder = processor.newDocumentBuilder();
			XPathSelector xPathSelector = xPathCompiler.compile("//*:text").load();
			xPathSelector.setContextItem(builder.build(result));
			Set<String> errors = new LinkedHashSet<String>();
			for (XdmItem item : xPathSelector.evaluate()) {
				errors.add(item.getStringValue());
			}
			return new LinkedList<String>(errors);
		} catch (SaxonApiException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws Exception {
		Source schema = Resource.apply("/schematron/purchase_order/PurchaseOrder.sch");
		Source xmlSource = Resource.apply("/schematron/purchase_order/PurchaseOrder.xml");
		System.out.println(SchematronValidatorCommand.apply(schema, xmlSource).execute());
	}

}
