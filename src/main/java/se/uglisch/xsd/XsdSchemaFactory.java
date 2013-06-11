package se.uglisch.xsd;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.validation.Schema;

import se.uglisch.XmlSchemaNsUris;
import se.uglisch.schematron.SchematronSchemaFactory;

public final class XsdSchemaFactory extends SchematronSchemaFactory {

	@Override
	public final boolean isSchemaLanguageSupported(String schemaLanguage) {
		return schemaLanguage != null && schemaLanguage.equals(XmlSchemaNsUris.W3C_XML_SCHEMA_NS_URI);
	}

	@Override
	public final Schema newSchema(Source[] xsdSchemas) {
		if (xsdSchemas == null || xsdSchemas.length == 0) {
			throw new IllegalStateException("No schemas");
		}
		List<Source> schSchmeas = new ArrayList<Source>();
		for (Source xsdSchema : xsdSchemas) {
			Source schSchema = Xsd2SchCommand.apply(xsdSchema).execute();
			schSchmeas.add(schSchema);
		}
		return super.newSchema(schSchmeas.toArray(new Source[] {}));
	}

}
