package se.uglisch.schematron.iso

import javax.xml.transform.Source

object IsoValidator {

  def apply(schemaSource: Source): IsoValidator = Schema(schemaSource) match {
    case Some(schema) => new IsoValidator(schema)
    case None => throw new IllegalArgumentException("Schema error")
  }
}

class IsoValidator(schema: Schema) {

  def validate(xml: Source) = schema.queryBinding match {
    case "xpath2" => new Xpath2Validator(schema).validate(xml)
    case queryBinding => throw new UnsupportedOperationException("queryBinding: " + queryBinding)
  }

}