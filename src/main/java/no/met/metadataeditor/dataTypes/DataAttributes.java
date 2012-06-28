package no.met.metadataeditor.dataTypes;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Interface class that is only used as a type placeholder in the
 * EditorVariable class.
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")  
public abstract class DataAttributes implements Serializable {

    private static final long serialVersionUID = -4182840533404059153L;

    @JsonIgnore
    public Map<String, DataType> getFields(){
        return getFields(getClass());
    }
    
    private Map<String, DataType> getFields(Class<? extends Object> inClass){
        
        Map<String, DataType> fields = new HashMap<String,DataType>();
        for (Field f : inClass.getDeclaredFields()) {

            if (f.isAnnotationPresent(IsAttribute.class)) {
                
                IsAttribute ia = f.getAnnotation(IsAttribute.class);
                DataType type = ia.value();
                fields.put(f.getName(), type);
            }
        }

        // attribute might be declared in super class so call this function recursively on the super
        // class
        if( inClass.getSuperclass() != null ){
            Map<String,DataType> superFields = getFields(inClass.getSuperclass());
            fields.putAll(superFields);
        }
        
        return fields;             
        
    }

    /**
     *
     * @return a new Instance of the subtype
     */
    public abstract DataAttributes newInstance();

    /**
     * add a attributes value by a string
     * @param string
     * @param value
     */
    public void addAttribute(String attr, String value){
        addAttribute(attr, value, getClass());
    }
    
    private void addAttribute(String attr, String value, Class<? extends Object> inClass){

        boolean isSet = false;
        try {
            for (Field f : inClass.getDeclaredFields()) {

                if (!f.getName().equals(attr)) {
                    continue;
                }

                if (f.isAnnotationPresent(IsAttribute.class)) {
                    f.set(this, value);
                    isSet = true;
                }
            }

            // attribute might be declared in super class so call this function recursively on the super
            // class
            if( !isSet && inClass.getSuperclass() != null ){
                addAttribute(attr, value, inClass.getSuperclass());
            }

        } catch (IllegalAccessException e) {
            System.out.println("Illegal to access the get method of the property: " + e.getMessage());

        } catch (SecurityException e) {
            System.out.println("Security problem to access the get method of the property: " + e.getMessage());

        }         
        
    }
    
    public String getAttribute(String attr) {        
        return getAttribute(attr,getClass());                
    }
    
    private String getAttribute(String attribute, Class<? extends Object> inClass) {
        
        String value = null;
        boolean isFetched = false;
        try {
            for (Field f : inClass.getDeclaredFields()) {

                if (!f.getName().equals(attribute)) {
                    continue;
                }

                if (f.isAnnotationPresent(IsAttribute.class)) {
                    value = "" + f.get(this);
                    if (value.trim().equalsIgnoreCase("null"))
                        value = "";

                    isFetched = true;
                }

            }

            // attribute might be declared in super class so call this function recursively on the super
            // class
            if( !isFetched && inClass.getSuperclass() != null ){
                value = getAttribute(attribute, inClass.getSuperclass());
            }

        } catch (IllegalAccessException e) {
            System.out.println("Illegal to access the get method of the property: " + e.getMessage());

        } catch (SecurityException e) {
            System.out.println("Security problem to access the get method of the property: " + e.getMessage());

        }
        return value;        
        
    }
}
