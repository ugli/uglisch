package se.uglisch.resource;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -344604156274891237L;

	public ResourceNotFoundException(String value) {
		super("Resource not found: " + value);
	}

}
