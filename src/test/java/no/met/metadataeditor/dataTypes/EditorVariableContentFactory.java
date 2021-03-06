package no.met.metadataeditor.dataTypes;

import java.util.List;
import java.util.Map;

import no.met.metadataeditor.dataTypes.EditorVariableContent;
import no.met.metadataeditor.dataTypes.attributes.DataAttribute;
import no.met.metadataeditor.dataTypes.attributes.LatLonBBAttribute;
import no.met.metadataeditor.dataTypes.attributes.LatLonBBSingleAttribute;
import no.met.metadataeditor.dataTypes.attributes.StringAttribute;

/**
 * Class that is used to create EditorVariableContent objects more easily
 * without using an XML file as the starting point. This is to simplify test
 * code.
 * 
 */
public class EditorVariableContentFactory {

    public static EditorVariableContent childlessStringAttribute(String value){
        
        EditorVariableContent evc = new EditorVariableContent();
        DataAttribute da = new StringAttribute();
        da.addAttribute("str", value);
        evc.setAttrs(da);
        
        return evc;
        
    }

    public static EditorVariableContent childlessBoundingboxAttribute(String north, String south, String east,
            String west) {

        EditorVariableContent evc = new EditorVariableContent();
        DataAttribute da = new LatLonBBAttribute();
        da.addAttribute("north", north);
        da.addAttribute("south", south);
        da.addAttribute("west", west);
        da.addAttribute("east", east);
        evc.setAttrs(da);
                
        
        return evc;
    }
    
    public static EditorVariableContent childlessSingleBBAttribute(String latLonStr){
        
        EditorVariableContent evc = new EditorVariableContent();
        DataAttribute da = new LatLonBBSingleAttribute();
        da.addAttribute("latLonStr", latLonStr);
        evc.setAttrs(da);
        
        return evc;
        
    }
    
    public static EditorVariableContent stringAttributeWithChildren(String value, Map<String,List<EditorVariableContent>> children){
        
        EditorVariableContent evc = new EditorVariableContent();
        DataAttribute da = new StringAttribute();
        da.addAttribute("str", value);
        evc.setAttrs(da);
        evc.setChildren(children);
        
        return evc;
        
    }    
    
}
