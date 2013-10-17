package se.uglisch.xpathnode

import java.io.InputStream
import javax.xml.transform.Source
import javax.xml.transform.stream.StreamSource
import org.apache.commons.io.IOUtils
import java.io.StringWriter
import java.io.Reader
import java.io.StringReader

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