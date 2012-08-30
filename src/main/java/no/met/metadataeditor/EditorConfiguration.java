package no.met.metadataeditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import no.met.metadataeditor.dataTypes.EditorTemplate;
import no.met.metadataeditor.dataTypes.EditorVariable;
import no.met.metadataeditor.dataTypes.EditorVariableContent;

/**
 * Class used accessing editor configuration that are used to generate the editor UI.
 */
@XmlRootElement(name="editor", namespace="http://www.met.no/schema/metadataeditor/editorConfiguration")
public class EditorConfiguration implements Serializable {

    private static final long serialVersionUID = -6228315858621721527L;

    private List<EditorPage> pages;

    public EditorConfiguration(){
        pages = new ArrayList<EditorPage>();
    }

    /**
     * Add additional configuration from the editor variables to the editor widgets.
     * For instance maxOccurs and minOccurs.
     * @param varMap
     * @return
     */
    public boolean configure(Map<String,EditorVariable> varMap){

        for( EditorPage page : pages ) {
            page.configure(varMap);
        }
        return true;
    }


    public boolean generateEditorWidgetViews(Map<String,List<EditorVariableContent>> contentMap ){

        boolean allPopulated = true;
        for( EditorPage page : pages ) {
            boolean pagePopulated = page.generateEditorWidgetViews(contentMap);
            if( !pagePopulated ){
                allPopulated = false;
            }
        }

        return allPopulated;
    }

    public Map<String, List<EditorVariableContent>> getContent(Map<String, EditorVariable> varMap) {

        Map<String, List<EditorVariableContent>> content = new HashMap<String, List<EditorVariableContent>>();
        for( EditorPage page : pages ){
            content.putAll(page.getContent(varMap));
        }

        return content;
    }


    @XmlElement(name="page", namespace="http://www.met.no/schema/metadataeditor/editorConfiguration")
    public List<EditorPage> getPages() {
        return pages;
    }

    public void setPages(List<EditorPage> pages) {
        this.pages = pages;
    }

    private Map<String, EditorPage> getPageMap(){

        Map<String, EditorPage> pageMap = new HashMap<String,EditorPage>();
        for( EditorPage page : pages ){
            pageMap.put(page.getId(), page);
        }
        return pageMap;

    }

    public EditorPage getPage(String id){
        Map<String,EditorPage> pageMap = getPageMap();
        return pageMap.get(id);
    }


    public void validateVarNames(EditorTemplate editorTemplate) {

        Map<String,EditorVariable> varMap = editorTemplate.getVarMap();

        for( EditorPage page : pages ){
            page.validateVarNames(varMap);
        }
    }


}
