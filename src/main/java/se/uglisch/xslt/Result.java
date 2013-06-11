package se.uglisch.xslt;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.InputStream;

import javax.xml.transform.stream.StreamSource;

public class Result extends StreamSource {

	private final char[] data;

	public Result(CharArrayWriter charArrayWriter) {
		this.data = charArrayWriter.toCharArray();
		setReader(new CharArrayReader(data));
	}

	public String getData() {
		return String.valueOf(data);
	}

	@Override
	@Deprecated
	public InputStream getInputStream() {
		return null;
	}

}
