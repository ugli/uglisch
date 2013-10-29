package se.uglisch.schematron.iso

import org.w3c.dom.ls.LSResourceResolver
import org.xml.sax.ErrorHandler

import javax.xml.transform.Source
import javax.xml.validation.Schema
import se.uglisch.xpathnode.XpathNode

object IsoSchema {
  def apply(
    SchematronSource: Source,
    errorHandler: ErrorHandler = null,
    resourceResolver: LSResourceResolver = null): IsoSchema =

    XpathNode.apply(SchematronSource).evaluateSingle("//*:schema") match {
      case Some(node) => new IsoSchema(node, errorHandler, resourceResolver)
      case None => throw new IllegalArgumentException("Couldn't find element schema in source")
    }
}

/**
 * <pre>
 * # Element declarations
 * schema = element schema {
 *     attribute id { xsd:ID }?,
 *     rich,
 *     attribute schemaVersion { non-empty-string }?,
 *     attribute defaultPhase { xsd:IDREF }?,
 *     attribute queryBinding { non-empty-string }?,
 *     (foreign
 *      & inclusion*
 *      &  (title?,
 *        ns*,
 *        p*,
 *        let*,
 *        phase*,
 *        pattern+,
 *        p*,
 *        diagnostics?))
 * }
 * </pre>
 */
class IsoSchema(
  xpathNode: XpathNode,
  errorHandler: ErrorHandler,
  resourceResolver: LSResourceResolver) extends Schema {

  lazy val schematronNamespace = xpathNode.namespace.get
  lazy val id = xpathNode.attribute("id")
  lazy val schemaVersion = xpathNode.attribute("schemaVersion")
  lazy val defaultPhase = xpathNode.attribute("defaultPhase")
  lazy val queryBinding = xpathNode.attribute("queryBinding") match {
    case Some(binding) => binding
    case None => "xslt"
  }
  lazy val title = xpathNode.evaluateSingle("*:title") match {
    case Some(title) => title.text
    case None => None
  }
  def patterns: List[Pattern] =
    xpathNode.evaluate("*:pattern").map(new Pattern(_)).toList
  def includes: List[Include] =
    xpathNode.evaluate("*:include").map(new Include(_)).toList

  def newValidator(): IsoValidator =
    new IsoValidator(IsoSchema.this, errorHandler, resourceResolver)

  override def newValidatorHandler() =
    null
}

class Include(xpathNode: XpathNode)

/**
 * <pre>
 * pattern = element pattern {
 *     rich,
 *     (foreign & inclusion* &
 *      ( (attribute abstract { "true" }, attribute id { xsd:ID },
 *              title?, (p*, let*, rule*))
 *      |  (attribute abstract { "false" }?, attribute id { xsd:ID }?,
 *              title?, (p*, let*, rule*))
 *      | (attribute abstract { "false" }?, attribute is-a { xsd:IDREF },
 * 			 attribute id { xsd:ID }?, title?, (p*, param*))
 * 	   )
 *    )
 * }
 * </pre>
 */
class Pattern(xpathNode: XpathNode) {
  def rules: List[Rule] =
    xpathNode.evaluate("*:rule").map(new Rule(_)).toList
}

/**
 * <pre>
 * rule = element rule {
 *     attribute flag { flagValue }?,
 *     rich,
 *     linkable,
 *     (foreign & inclusion*
 *      & ((attribute abstract { "true" },
 *             attribute id { xsd:ID }, let*, (assert | report | extends)+)
 * }
 * | (attribute context { pathValue },
 *     attribute id { xsd:ID }?,
 *    attribute abstract { "false" }?,
 *    let*, (assert | report | extends)+)))
 * </pre>
 */
class Rule(xpathNode: XpathNode) {
  lazy val context = xpathNode.attribute("context")
  def asserts: List[Assert] =
    xpathNode.evaluate("*:assert").map(new Assert(_)).toList
  override def toString =
    xpathNode.toString
}

/**
 * <pre>
 * assert = element assert {
 *     attribute test { exprValue },
 *     attribute flag { flagValue }?,
 *     attribute id { xsd:ID }?,
 *     attribute diagnostics { xsd:IDREFS }?,
 *     rich,
 *     linkable,
 *     (foreign & (text | name | value-of | emph | dir | span)*)
 * }
 * </pre>
 */
class Assert(xpathNode: XpathNode) {
  lazy val test = xpathNode.attribute("test")
  lazy val text = xpathNode.text
  override def toString =
    xpathNode.toString
}