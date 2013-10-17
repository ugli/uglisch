package se.uglisch.schematron.iso

import org.junit.Test
import org.junit.Assert._
import se.uglisch.xpathnode.Resource

class IsoValidatorTest {

  @Test
  def xpath2: Unit = {
    val schemaResource = Resource("/se/uglisch/schematron/iso/PurchaseOrder.sch")
    val xmlResource = Resource("/se/uglisch/schematron/iso/PurchaseOrderValid.xml")
    IsoValidator(schemaResource.asSource.get).validate(xmlResource.asSource.get)
  }

  @Test
  def defaultBinding: Unit = {
    try {
      val schemaResource = Resource("/se/uglisch/schematron/iso/PurchaseOrder1.sch")
      val xmlResource = Resource("/se/uglisch/schematron/iso/PurchaseOrderValid.xml")
      IsoValidator(schemaResource.asSource.get).validate(xmlResource.asSource.get)
    } catch {
      case e: UnsupportedOperationException => assertEquals("queryBinding: xslt", e.getMessage)
    }
  }

}