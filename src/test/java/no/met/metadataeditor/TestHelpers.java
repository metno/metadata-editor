package no.met.metadataeditor;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

public class TestHelpers {

    public static String formattedXMLAsString(String filename){

        String output = "";
        try {
            URL fileUrl = TestHelpers.class.getResource(filename);
            SAXBuilder builder = new SAXBuilder();            
            Document doc = builder.build(new InputSource(fileUrl.openStream()));
            XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
            StringWriter writer = new StringWriter();
            xout.output(doc, writer);
            output = writer.toString();
            
        } catch (JDOMException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return output;        
        
    }
    
    public static String fileAsString(String filename){
        
        URL fileUrl = TestHelpers.class.getResource(filename);
        StringBuilder sb = new StringBuilder();  
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(fileUrl.openStream()));            
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new EditorException(e.getMessage());
        } finally {
            if( br != null ) {
                try {
                    br.close();
                } catch (IOException e) {
                    Logger.getLogger(TestHelpers.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
        return sb.toString();        
        
    }
    
}
