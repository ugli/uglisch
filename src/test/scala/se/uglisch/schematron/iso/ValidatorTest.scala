package se.uglisch.schematron.iso

import org.junit.Test
import org.junit.Assert._
import se.uglisch.resource.Resource
import javax.xml.transform.stream.StreamSource
import java.io.File
import org.xml.sax.ErrorHandler

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

  @Test
  def usingFiles: Unit = {
    val schemaFile = new File("src/test/resources/se/uglisch/schematron/iso/PurchaseOrder.sch")
    val schema = IsoSchema(new StreamSource(schemaFile))
    val xmlFile = new File("src/test/resources/se/uglisch/schematron/iso/PurchaseOrderUnvalid.xml")
    val valdator = new IsoValidator(schema)
    valdator.setErrorHandler(null)
    val errors = valdator.validationResult(new StreamSource(xmlFile))
    assertEquals(2, errors.size)
    assertEquals("A purchase order element should have a shipping address. There should be a shipTo element contained by the PurchaseOrder element.", errors.head)
  }

}