package se.uglisch.schematron.iso

import org.junit.Test
import org.junit.Assert._
import se.uglisch.resource.Resource

class ValidatorTest {

  @Test
  def xpath2: Unit = {
    val schemaResource = Resource("/se/uglisch/schematron/iso/PurchaseOrder.sch")
    val schema = IsoSchema(schemaResource.asSource.get)
    val xmlResource = Resource("/se/uglisch/schematron/iso/PurchaseOrderValid.xml")
    new IsoValidator(schema).validate(xmlResource.asSource.get)
  }

  @Test
  def defaultBinding: Unit = {
    try {
      val schemaResource = Resource("/se/uglisch/schematron/iso/PurchaseOrder1.sch")
      val schema = IsoSchema(schemaResource.asSource.get)
      val xmlResource = Resource("/se/uglisch/schematron/iso/PurchaseOrderValid.xml")
      new IsoValidator(schema).validate(xmlResource.asSource.get)
    } catch {
      case e: UnsupportedOperationException => assertEquals("queryBinding: xslt", e.getMessage)
    }
  }

}