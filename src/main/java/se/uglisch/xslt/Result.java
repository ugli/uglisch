package se.uglisch.xslt;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.transform.stream.StreamSource;

public class Result extends StreamSource {

	private final char[] data;

	public Result(CharArrayWriter charArrayWriter) {
		this.data = charArrayWriter.toCharArray();
	}

	@Override
	public Reader getReader() {
		return new CharArrayReader(data);
	}

	@Override
	@Deprecated
	public void setReader(Reader reader) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public InputStream getInputStream() {
		return null;
	}

	@Override
	@Deprecated
	public void setInputStream(InputStream inputStream) {
		throw new UnsupportedOperationException();
	}

}
