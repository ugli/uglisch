package se.uglisch.schematron.iso

import org.junit.Test
import org.junit.Assert._
import se.uglisch.resource.Resource

class Xpath2ValidatorTest {

  @Test
  def xpath2: Unit = {
    val schemaResource = Resource("/se/uglisch/schematron/iso/PurchaseOrder.sch")
    val schema = IsoSchema(schemaResource.asSource.get)
    val xmlResource = Resource("/se/uglisch/schematron/iso/PurchaseOrderValid.xml")
    val messages = new Xpath2Validator(schema).validate(xmlResource.asSource.get)
    println(messages)
  }

}