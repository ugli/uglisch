package se.uglisch.resource

import java.io.InputStream
import java.io.Reader
import java.io.StringReader
import java.io.StringWriter

import org.apache.commons.io.IOUtils

import javax.xml.transform.Source
import javax.xml.transform.stream.StreamSource

case class Resource(path: String) {

  def asStream: Option[InputStream] =
    Option(getClass.getResourceAsStream(path))

  def asSource: Option[Source] = asStream match {
    case Some(stream) => Option(new ResourceSource(stream))
    case None => None
  }
}

class ResourceSource(stream: InputStream) extends StreamSource {

  val data = {
    val writer = new StringWriter
    IOUtils.copy(stream, writer)
    writer.toString
  }

  override def getReader: Reader =
    new StringReader(data)

}



