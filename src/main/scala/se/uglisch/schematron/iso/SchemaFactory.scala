package se.uglisch.schematron.iso

import org.w3c.dom.ls.LSResourceResolver
import org.xml.sax.ErrorHandler

import javax.xml.transform.Source

object SchemaFactory {
  val SCHEMATRON_NS_URI = "http://purl.oclc.org/dsdl/schematron"
  def apply() = new SchemaFactory
}

class SchemaFactory(
  var resourceResolver: Option[LSResourceResolver] = None,
  var errorHandler: Option[ErrorHandler] = None) extends javax.xml.validation.SchemaFactory {

  def isSchemaLanguageSupported(schemaLanguage: String): Boolean =
    schemaLanguage != null && schemaLanguage == SchemaFactory.SCHEMATRON_NS_URI

  def setErrorHandler(errorHandler: ErrorHandler): Unit =
    SchemaFactory.this.errorHandler = Option(errorHandler)

  def getErrorHandler: ErrorHandler =
    errorHandler.getOrElse(null)

  def setResourceResolver(resourceResolver: LSResourceResolver): Unit =
    SchemaFactory.this.resourceResolver = Option(resourceResolver)

  def getResourceResolver: LSResourceResolver =
    resourceResolver.getOrElse(null)

  def create(source: Source): Option[Schema] =
    Schema(source, errorHandler, resourceResolver)

  def newSchema(schemas: Array[Source]): Schema = {
    if (schemas == null || schemas.length == 0)
      throw new IllegalStateException("No schemas")
    if (schemas.length < 1)
      throw new IllegalStateException("Just one schema is supported")
    val schemaOption = create(schemas.head)
    if (schemaOption.isDefined)
      schemaOption.get
    throw new IllegalStateException("Problems...")
  }

  def newSchema() =
    throw new UnsupportedOperationException()
}