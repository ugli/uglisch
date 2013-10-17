package se.uglisch.xpathnode

import org.junit.Test

class XpathNodeTest {

  @Test
  def abc: Unit = {
    val sourceResource = Resource("/se/uglisch/xpathnode/PurchaseOrderValid.xml")
    val xpathNode = XpathNode(sourceResource.asSource.get)
    println(xpathNode.evaluate("//Item").map(_.attribute("pno").get))
  }

}