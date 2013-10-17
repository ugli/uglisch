package se.uglisch.schematron.xslt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.xml.transform.Source;

import org.junit.Test;

import se.uglisch.resource.Resource;
import se.uglisch.schematron.xslt.ValidatorImpl;

public class ValidatorImplTest {

	@Test
	public void unvalid() {
		Source schema = Resource.apply("/schematron/schematron_validator_command/PurchaseOrder.sch");
		Source xmlSource = Resource.apply("/schematron/schematron_validator_command/PurchaseOrderUnvalid.xml");
		List<String> errors = ValidatorImpl.apply(schema).validate(xmlSource);
		assertEquals(1, errors.size());
		assertEquals("An address should have a state.", errors.get(0));
	}

	@Test
	public void valid() {
		Source schema = Resource.apply("/schematron/schematron_validator_command/PurchaseOrder.sch");
		Source xmlSource = Resource.apply("/schematron/schematron_validator_command/PurchaseOrderValid.xml");
		List<String> errors = ValidatorImpl.apply(schema).validate(xmlSource);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void generated() {
		Source schema = Resource.apply("/xsd/xsd2sch/generatedShiporder.sch");
		Source xmlSource = Resource.apply("/xsd/xsd_schema_factory/shiporderValid.xml");
		List<String> errors = ValidatorImpl.apply(schema).validate(xmlSource);
		assertEquals(
		    "When in a \"item\" element, the element \"title\" should be immediately followed by the element \"note\".",
		    errors.get(0));
	}

}
