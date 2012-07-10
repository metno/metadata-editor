package no.met.metadataeditor;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
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
    
    
    
    public static void copyResourcesRecursively(URL originUrl, File destination) throws IOException {
        URLConnection urlConnection = originUrl.openConnection();
        if (urlConnection instanceof JarURLConnection) {
            copyJarResourcesRecursively(destination, (JarURLConnection) urlConnection);
        } else {

            // we assume that we are either dealing with files in a Jar or that they are found on the disk
            File originAsFile = new File(originUrl.getFile());
            FileUtils.copyDirectory(originAsFile, destination);            
        }
    }

    public static void copyJarResourcesRecursively(File destination, JarURLConnection jarConnection ) throws IOException {
        JarFile jarFile = jarConnection.getJarFile();

        Enumeration<JarEntry> entries = jarFile.entries();
        while( entries.hasMoreElements() ){
            JarEntry entry = entries.nextElement();

            if (entry.getName().startsWith(jarConnection.getEntryName())) {
                String fileName = StringUtils.removeStart(entry.getName(), jarConnection.getEntryName());
                if (!entry.isDirectory()) {
                    InputStream entryInputStream = null;
                    try {
                        entryInputStream = jarFile.getInputStream(entry);
                        FileUtils.copyInputStreamToFile(entryInputStream, new File(destination, fileName));
                    } finally {
                        entryInputStream.close();
                    }
                } else {
                    File dir = new File(destination, fileName);
                    if( !dir.exists() ){
                        dir.mkdirs();
                    }
                }
            }
        }
    }
    
}
