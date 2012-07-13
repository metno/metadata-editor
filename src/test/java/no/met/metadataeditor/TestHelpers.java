package no.met.metadataeditor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import no.met.metadataeditor.dataTypes.EditorTemplate;
import no.met.metadataeditor.dataTypes.EditorVariable;
import no.met.metadataeditor.dataTypes.EditorVariableContent;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TestHelpers {

    public static String formattedXMLAsString(String filename) {

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

    public static String fileAsString(String filename) {

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
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Logger.getLogger(TestHelpers.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
        return sb.toString();

    }
    
    public static Map<String, List<EditorVariableContent>> getContent(String templateResource, String metadataResource ){
        
        URL templateUrl = TestHelpers.class.getResource(templateResource);
        URL metadataUrl = TestHelpers.class.getResource(metadataResource);
        EditorTemplate et = null;
        Map<String, List<EditorVariableContent>> content = null;
        try {
            et = new EditorTemplate(new InputSource(templateUrl.openStream()));
            content = et.getContent(new InputSource(metadataUrl.openStream()));
            
        } catch (SAXException e) {
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail();
        }
        return content;        
    }     
    
    public static EditorConfiguration getConfiguration(String configurationResource){
        
        String configString = fileAsString(configurationResource);
        EditorConfiguration config = EditorConfigurationFactory.unmarshallConfiguration(configString);
        return config;
        
    }
    
    public static Map<String, EditorVariable> getVariables(String templateResource ){
        
        URL url = TestHelpers.class.getResource(templateResource);
        Map<String, EditorVariable> mse = null;
        EditorTemplate et = null;
        try {
            et = new EditorTemplate(new InputSource(url.openStream()));
            mse = et.getVarMap();
        } catch (SAXException e) {
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }        
        return mse;
    }    


}
