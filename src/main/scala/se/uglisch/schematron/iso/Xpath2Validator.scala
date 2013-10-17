package se.uglisch.schematron.iso

import javax.xml.transform.Source
import se.uglisch.xpathnode.XpathNode
import net.sf.saxon.value.BooleanValue
import net.sf.saxon.om.Sequence

class Xpath2Validator(schema: Schema) {

  def validate(xml: Source): List[String] =
    validatePatterns(XpathNode(xml))

  private def validatePatterns(xmlXpathNode: XpathNode): List[String] =
    (for {
      pattern <- schema.patterns
      rule <- pattern.rules
    } yield validateRule(rule, xmlXpathNode)).flatten

  private def validateRule(rule: Rule, xmlXpathNode: XpathNode): List[String] =
    rule.context match {
      case Some(context) =>
        validateRule(context, rule, xmlXpathNode)
      case None =>
        throw new IllegalStateException("No rule context: " + rule)
    }

  private def validateRule(context: String, rule: Rule, xmlXpathNode: XpathNode): List[String] =
    for {
      assert <- rule.asserts
      contextNode <- xmlXpathNode.evaluate(context)
      assertMessage <- validateAssert(assert, contextNode)
    } yield assertMessage

  private def validateAssert(assert: Assert, contextNode: XpathNode): Option[String] =
    assert.test match {
      case Some(test) =>
        validateAssert(test, assert, contextNode)
      case None =>
        throw new IllegalStateException("No assert test: " + assert)
    }

  private def validateAssert(test: String, assert: Assert, contextNode: XpathNode): Option[String] =
    contextNode.evaluateSingle(test) match {
      case Some(evaluatedNode) =>
        evalUnderlyingValue(evaluatedNode.underlyingValue, assert)
      case None =>
        assertMessage(assert)
    }

  private def evalUnderlyingValue(underlyingValue: Sequence, assert: Assert): Option[String] =
    underlyingValue match {
      case b: BooleanValue =>
        if (!b.getBooleanValue())
          assertMessage(assert)
        else
          None
      case _ =>
        None
    }

  private def assertMessage(assert: Assert): Option[String] =
    assert.text match {
      case Some(text) =>
        Option(text.trim)
      case None =>
        throw new IllegalStateException("No assert message: " + assert)
    }

}