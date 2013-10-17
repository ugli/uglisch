package se.uglisch.schematron.iso

import org.junit.Test
import org.junit.Assert._
import se.uglisch.xpathnode.Resource

class Xpath2ValidatorTest {

  @Test
  def xpath2: Unit = {
    val schemaResource = Resource("/se/uglisch/schematron/iso/PurchaseOrder.sch")
    val schema = Schema(schemaResource.asSource.get).get
    val xmlResource = Resource("/se/uglisch/schematron/iso/PurchaseOrderValid.xml")
    val messages = new Xpath2Validator(schema).validate(xmlResource.asSource.get)
    println(messages)
  }

}