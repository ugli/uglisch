package se.uglisch.xerces;

import java.util.Map;

import javax.xml.validation.SchemaFactory;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class XercesSchemaFactory {

	private final static String[] CLASS_NAMES = {
    // Oracle JDK
    "com.sun.org.apache.xerces.internal.jaxp.validation.XMLSchemaFactory",
    // IBM JDK - J9 ( and Xerces .. :)
    "org.apache.xerces.jaxp.validation.XMLSchemaFactory" };
	
	private final LSResourceResolver resourceResolver;
	private final Map<String, Boolean> features;
	private final Map<String, Object> properties;

	private XercesSchemaFactory(LSResourceResolver resourceResolver, Map<String, Boolean> features,
      Map<String, Object> properties) {
	  this.resourceResolver = resourceResolver;
	  this.features = features;
	  this.properties = properties;
  }

	public static XercesSchemaFactory apply(LSResourceResolver resourceResolver, Map<String, Boolean> features,
      Map<String, Object> properties) {
		return new XercesSchemaFactory(resourceResolver, features, properties);
	}
	
	public SchemaFactory create() throws SAXNotRecognizedException, SAXNotSupportedException {
		for (String className : CLASS_NAMES) {
			try {
				SchemaFactory schemaFactory = (SchemaFactory) Class.forName(className).newInstance();
				setResolver(schemaFactory);
				setFeatures(schemaFactory);
				setProperties(schemaFactory);
				return schemaFactory;
			} catch (IllegalAccessException e) {
			} catch (InstantiationException e) {
			} catch (ClassNotFoundException e) {
			}
		}
		throw new RuntimeException("Couldn't instanciate any of: " + CLASS_NAMES);
	}

	private void setProperties(SchemaFactory schemaFactory) throws SAXNotRecognizedException, SAXNotSupportedException {
		for (String property : properties.keySet())
			schemaFactory.setProperty(property, properties.get(property));
	}

	private void setFeatures(SchemaFactory schemaFactory) throws SAXNotRecognizedException, SAXNotSupportedException {
		for (String feature : features.keySet())
			schemaFactory.setFeature(feature, features.get(feature));
	}

	private void setResolver(SchemaFactory schemaFactory) {
		schemaFactory.setResourceResolver(resourceResolver);
	}
	
	
}
