package se.uglisch.schematron;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.xml.transform.Source;

import org.junit.Test;

import se.uglisch.resource.Resource;

public class SchematronValidatorCommandTest {

	@Test
	public void unvalid() {
		Source schema = Resource.apply("/schematron/schematron_validator_command/PurchaseOrder.sch");
		Source xmlSource = Resource.apply("/schematron/schematron_validator_command/PurchaseOrderUnvalid.xml");
		List<String> errors = SchematronValidatorCommand.apply(schema, xmlSource).execute();
		assertEquals(1, errors.size());
		assertEquals("An address should have a state.", errors.get(0));
	}

	@Test
	public void valid() {
		Source schema = Resource.apply("/schematron/schematron_validator_command/PurchaseOrder.sch");
		Source xmlSource = Resource.apply("/schematron/schematron_validator_command/PurchaseOrderValid.xml");
		List<String> errors = SchematronValidatorCommand.apply(schema, xmlSource).execute();
		assertTrue(errors.isEmpty());
	}

	@Test
	public void generated() {
		Source schema = Resource.apply("/xsd/xsd2sch/generatedShiporder.sch");
		Source xmlSource = Resource.apply("/xsd/xsd_schema_factory/shiporderValid.xml");
		List<String> errors = SchematronValidatorCommand.apply(schema, xmlSource).execute();
		assertEquals(
		    "When in a \"item\" element, the element \"title\" should be immediately followed by the element \"note\".",
		    errors.get(0));
	}

}
