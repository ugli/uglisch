package se.uglisch.schematron.iso

import org.junit.Test
import org.junit.Assert._
import se.uglisch.xpathnode.Resource
import se.uglisch.xpathnode.XpathNode

class SchemaTest {

  @Test
  def xpathQueryBinding: Unit = {
    val sourceResource = Resource("/se/uglisch/schematron/iso/PurchaseOrder.sch")

    val schema = Schema(sourceResource.asSource.get).get
    assertEquals("xpath2", schema.queryBinding)
    assertEquals("http://purl.oclc.org/dsdl/schematron", schema.schematronNamespace)
    assertEquals("Schema for Purchase Order Example", schema.title.get)
    assertEquals(3, schema.patterns.size)
  }

  @Test
  def defaultQueryBinding: Unit = {
    val sourceResource = Resource("/se/uglisch/schematron/iso/PurchaseOrder1.sch")

    val schema = Schema(sourceResource.asSource.get).get
    assertEquals("xslt", schema.queryBinding)
    assertEquals("http://purl.oclc.org/dsdl/schematron", schema.schematronNamespace)
    assertEquals("Schema for Purchase Order Example", schema.title.get)
    assertEquals(3, schema.patterns.size)
  }

  @Test
  def assertTest: Unit = {
    val sourceResource = Resource("/se/uglisch/schematron/iso/PurchaseOrder.sch")

    val schema = Schema(sourceResource.asSource.get).get
    val pattern = schema.patterns.head
    val rule = pattern.rules.head
    assertEquals("name| city | state | zip", rule.context.get)
    val assert = rule.asserts.head
    assertEquals("parent::*/street", assert.test.get)

  }

}