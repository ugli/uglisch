package se.uglisch.xpathnode

import javax.xml.transform.Source
import net.sf.saxon.s9api.XdmItem
import net.sf.saxon.s9api.Processor
import scala.collection.JavaConversions._
import net.sf.saxon.tree.tiny.TinyElementImpl
import net.sf.saxon.s9api.XPathSelector
import net.sf.saxon.om.Sequence
import net.sf.saxon.s9api.XPathExecutable
import scala.collection.mutable.HashMap
import scala.collection.mutable.SynchronizedMap

object XpathNode {

  val processor = new Processor(false)
  val xPathCompiler = processor.newXPathCompiler
  val documentBuilder = processor.newDocumentBuilder

  def apply(source: Source): XpathNode =
    new XpathNode(documentBuilder.build(source))

}

object XPathExecutableFactory {

  private val xpathCache = new HashMap[String, XPathExecutable] with SynchronizedMap[String, XPathExecutable]

  def create(expr: String): XPathExecutable = {
    if (!xpathCache.contains(expr))
      xpathCache(expr) = XpathNode.xPathCompiler.compile(expr)
    xpathCache(expr)
  }

}

class XpathNode(xdmItem: XdmItem) {

  lazy val text: Option[String] =
    Option(xdmItem.getStringValue())

  lazy val namespace: Option[String] =
    if (xdmItem.getUnderlyingValue.isInstanceOf[TinyElementImpl])
      Option(xdmItem.getUnderlyingValue.asInstanceOf[TinyElementImpl].getURI)
    else
      None

  lazy val underlyingValue: Sequence = xdmItem.getUnderlyingValue

  private def selector(expr: String): XPathSelector = {
    val selector = XPathExecutableFactory.create(expr).load
    selector.setContextItem(xdmItem)
    selector
  }

  def evaluateSingle(expr: String): Option[XpathNode] =
    Option(selector(expr).evaluateSingle) match {
      case Some(item) => Option(new XpathNode(item))
      case None => None
    }

  def evaluate(expr: String): List[XpathNode] =
    selector(expr).evaluate.map(new XpathNode(_)).toList

  def attribute(name: String): Option[String] =
    evaluateSingle("@" + name) match {
      case Some(attr) => attr.text
      case None => None
    }

  override def toString =
    xdmItem.toString

}