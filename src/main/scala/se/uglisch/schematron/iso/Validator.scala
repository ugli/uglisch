package se.uglisch.schematron.iso

import org.w3c.dom.ls.LSResourceResolver
import org.xml.sax.ErrorHandler
import org.xml.sax.SAXParseException

import javax.xml.transform.Result
import javax.xml.transform.Source
import se.uglisch.resource.Resource

object Validator {
  def apply(schema: Schema, errorHandler: Option[ErrorHandler], resourceResolver: Option[LSResourceResolver]): Validator =
    new Validator(schema, errorHandler, resourceResolver)

  def apply(schema: Source, errorHandler: Option[ErrorHandler], resourceResolver: Option[LSResourceResolver]): Option[Validator] =
    Schema(schema, errorHandler, resourceResolver) match {
      case op: Some[Schema] => Some(Validator(op.get, errorHandler, resourceResolver))
      case None => None
    }
  def apply(schema: Resource, errorHandler: Option[ErrorHandler], resourceResolver: Option[LSResourceResolver]): Option[Validator] =
    schema.asSource match {
      case op: Some[Source] => Validator(op.get, errorHandler, resourceResolver)
      case None => None
    }
}

class Validator(
  schema: Schema,
  var errorHandler: Option[ErrorHandler],
  var resourceResolver: Option[LSResourceResolver]) extends javax.xml.validation.Validator {

  def getValidationStringList(xml: Source): List[String] = schema.queryBinding match {
    case "xpath2" => new Xpath2Validator(schema).validate(xml)
    case queryBinding => throw new UnsupportedOperationException("queryBinding: " + queryBinding)
  }
  override def validate(source: Source, result: Result): Unit = {
    val errorList = getValidationStringList(source)
    if (!errorList.isEmpty) {
      if (errorHandler.isDefined)
        for (error <- errorList)
          errorHandler.get.error(new SAXParseException(error, null))
      else
        new SAXParseException(errorList.toString, null)
    }
  }
  override def setErrorHandler(errorHandler: ErrorHandler): Unit =
    this.errorHandler = Option(errorHandler)
  override def getErrorHandler: ErrorHandler =
    errorHandler.getOrElse(null)
  override def setResourceResolver(resourceResolver: LSResourceResolver): Unit =
    this.resourceResolver = Option(resourceResolver)
  override def getResourceResolver: LSResourceResolver =
    resourceResolver.getOrElse(null)
  override def reset: Unit = {}

}