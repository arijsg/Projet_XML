package fr.univrouen.rss25SB.utils;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class ValidationUtils {
    public static boolean validateXML(Source xml, Source xsd) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsd);
            Validator validator = schema.newValidator();
            validator.validate(xml);
            return true;
        } catch (Exception e) {
            System.err.println("XSD validation error: " + e.getMessage());
            return false;
        }
    }
}
