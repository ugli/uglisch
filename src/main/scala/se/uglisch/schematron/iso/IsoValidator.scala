package se.uglisch.schematron.iso

import org.w3c.dom.ls.LSResourceResolver
import org.xml.sax.ErrorHandler
import org.xml.sax.SAXParseException

import javax.xml.transform.Result
import javax.xml.transform.Source
import javax.xml.validation.Validator

class IsoValidator(
  schematron: IsoSchema,
  var errorHandler: ErrorHandler = null,
  var resourceResolver: LSResourceResolver = null) extends Validator {

  def validationResult(xml: Source): List[String] = schematron.queryBinding match {
    case "xpath2" => Xpath2Validator.validate(schematron, xml)
    case queryBinding => throw new UnsupportedOperationException("queryBinding: " + queryBinding)
  }
  override def validate(source: Source, result: Result): Unit = {
    val errors = validationResult(source)
    if (!errors.isEmpty)
      if (errorHandler != null)
        for (error <- errors)
          errorHandler.error(new SAXParseException(error, null))
      else
        new SAXParseException(errors.toString, null)
  }
  override def setErrorHandler(errorHandler: ErrorHandler): Unit =
    IsoValidator.this.errorHandler = errorHandler
  override def getErrorHandler: ErrorHandler =
    errorHandler
  override def setResourceResolver(resourceResolver: LSResourceResolver): Unit =
    IsoValidator.this.resourceResolver = resourceResolver
  override def getResourceResolver: LSResourceResolver =
    resourceResolver
  override def reset: Unit = {}

}