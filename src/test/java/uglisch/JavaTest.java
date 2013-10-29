package uglisch;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import se.uglisch.schematron.iso.IsoSchema;

public class JavaTest {

	public static void main(String[] args) throws SAXException {
		SchemaFactory sf = SchemaFactory
				.newInstance("http://purl.oclc.org/dsdl/schematron");
		Source schemaSource = new StreamSource(
				JavaTest.class
						.getResourceAsStream("/se/uglisch/schematron/iso/PurchaseOrder.sch"));
		IsoSchema schema = (IsoSchema) sf.newSchema(schemaSource);
		System.out.println(schema.queryBinding());

	}
}
