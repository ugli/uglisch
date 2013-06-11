package se.uglisch.resource;

import java.io.InputStream;

import javax.xml.transform.stream.StreamSource;

public class Resource extends StreamSource {

	private final String value;

	private Resource(final String value) {
		this.value = value;
		setInputStream(toStream());
	}

	public static Resource apply(final String value) {
		return new Resource(value);
	}

	private InputStream toStream() {
		final InputStream stream = getClass().getResourceAsStream(value);
		if (stream == null) {
			throw new ResourceNotFoundException(value);
		}
		return stream;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Resource other = (Resource) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
