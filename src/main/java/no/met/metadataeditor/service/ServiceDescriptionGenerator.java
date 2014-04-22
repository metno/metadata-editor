package no.met.metadataeditor.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Automatically generate web service documentation by reading the annotations
 * for a class.
 */
public class ServiceDescriptionGenerator {

    private static final Logger logger = Logger.getLogger(ServiceDescriptionGenerator.class.getName());

    /**
     * Create a description of the web service offered by a class in XML. The
     * documentation is created by looking at the web service annotations.
     *
     * @param c
     *            The class to generate documentation for.
     * @return A XML document object.
     * @throws ParserConfigurationException 
     */
    public static Document getXMLServiceDescription(Class<? extends Object> c) throws ParserConfigurationException {

        List<Method> serviceMethods = getServiceMethods(c);

        DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = df.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element rootElement = doc.createElement("services");
        doc.appendChild(rootElement);
        for (Method m : serviceMethods) {

            Element service = doc.createElement("service");
            MethodInfo mi = getMethodInfo(m);

            Attr pathAttr = doc.createAttribute("path");
            pathAttr.setValue(mi.path);
            service.setAttributeNode(pathAttr);

            Attr methodsAttr = doc.createAttribute("methods");
            methodsAttr.setValue(mi.methods);
            service.setAttributeNode(methodsAttr);            
            
            if (mi.produces != null) {
                
                Attr producesAttr = doc.createAttribute("returmMimeType");
                producesAttr.setValue(StringUtils.join(mi.produces, ','));
                service.setAttributeNode(producesAttr);
            }

            if (mi.description != null) {
                Attr descriptionAttr = doc.createAttribute("description");
                descriptionAttr.setValue(mi.description);
                service.setAttributeNode(descriptionAttr);                
            }

            List<ParameterInfo> params = getParameters(m);
            for (ParameterInfo pi : params) {
                Element param = doc.createElement("parameter");

                Attr nameAttr = doc.createAttribute("name");
                nameAttr.setValue(pi.name);
                param.setAttributeNode(nameAttr);                

                Attr typeAttr = doc.createAttribute("type");
                typeAttr.setValue(pi.type);
                param.setAttributeNode(typeAttr);                
                
                
                if (pi.defaultValue != null) {

                    Attr defaultValueAttr = doc.createAttribute("defaultValue");
                    defaultValueAttr.setValue(pi.defaultValue);
                    param.setAttributeNode(defaultValueAttr);                
                }
                service.appendChild(param);
            }
            
            rootElement.appendChild(service);
        }

        return doc;
    }

    private static MethodInfo getMethodInfo(Method m) {

        MethodInfo mi = new MethodInfo();

        Path path = m.getAnnotation(Path.class);
        mi.path = path.value();

        Produces produces = m.getAnnotation(Produces.class);
        if (produces != null) {
            mi.produces = produces.value();
        }

        ServiceDescription description = m.getAnnotation(ServiceDescription.class);
        if (description != null) {
            mi.description = description.value();
        }
        
        mi.methods = getSupportedMethods(m);
        
        return mi;

    }

    /**
     * @param c The class to search for methods in.
     * @return A list of method that is used to offer web services in the class.
     */
    private static List<Method> getServiceMethods(Class<? extends Object> c) {

        List<Method> serviceMethods = new ArrayList<>();

        for (Method m : c.getMethods()) {

            logger.info(m.getName());
            if (m.isAnnotationPresent(Path.class)) {
                serviceMethods.add(m);
            }
        }

        return serviceMethods;
    }

    /**
     * @param method The method to get information about.
     * @return Information about all the parameters to web service call to the method.
     */
    private static List<ParameterInfo> getParameters(Method method) {

        List<ParameterInfo> parameters = new ArrayList<>();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        for (Annotation[] annotationForParam : paramAnnotations) {

            ParameterInfo pi = new ParameterInfo();
            for (Annotation a : annotationForParam) {
                if (a instanceof QueryParam) {
                    pi.name = ((QueryParam) a).value();
                    pi.name = "query";
                } else if( a instanceof FormParam ) {
                    pi.name = ((FormParam) a).value();
                    pi.type = "form";
                } else if( a instanceof PathParam ) {
                    pi.name = ((PathParam) a).value();
                    pi.type = "path";
                } else if (a instanceof DefaultValue) {

                    pi.defaultValue = ((DefaultValue) a).value();
                }
            }

            if (pi.name != null) {
                parameters.add(pi);
            }
        }

        return parameters;
    }
    
    private static String getSupportedMethods(Method m){
        
        List<String> methods = new ArrayList<>();

        GET get = m.getAnnotation(GET.class);
        if( get != null ){
            methods.add("GET");
        }        

        POST post = m.getAnnotation(POST.class);
        if( post != null ){
            methods.add("POST");
        }        

        PUT put = m.getAnnotation(PUT.class);
        if( put != null ){
            methods.add("PUT");
        }        

        DELETE delete = m.getAnnotation(DELETE.class);
        if( delete != null ){
            methods.add("DELETE");
        }        
        
        return StringUtils.join(methods, ",");
        
    }

    private static class ParameterInfo {

        String name;
        String defaultValue;
        String type;

    }

    private static class MethodInfo {

        String path;
        String produces[];
        String description;
        String methods;

    }

}
