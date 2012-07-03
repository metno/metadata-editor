package no.met.metadataeditor.dataTypes;

/**
 * Class that is used to create EditorVariableContent objects more easily
 * without using an XML file as the starting point. This is to simplify test
 * code.
 * 
 */
public class EditorVariableContentFactory {

    public static EditorVariableContent childlessStringAttribute(String value){
        
        EditorVariableContent evc = new EditorVariableContent();
        DataAttributes da = new StringAttributes();
        da.addAttribute("str", value);
        evc.setAttrs(da);
        
        return evc;
        
    }
    
}
