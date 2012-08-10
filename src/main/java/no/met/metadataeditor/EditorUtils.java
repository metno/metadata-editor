package no.met.metadataeditor;

import java.io.IOException;
import java.io.StringWriter;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Container of various utility functions for the editor.
 */
public class EditorUtils {

    /**
     * Convert a JDom document to a formatted XML string.
     * @param doc Document to convert
     * @return The formatted XML string for the document.
     * @throws IOException
     */
    public static String docToString(Document doc) throws IOException {
        XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
        StringWriter writer = new StringWriter();
        xout.output(doc, writer);
        return writer.toString();
    }        
    
    
}
