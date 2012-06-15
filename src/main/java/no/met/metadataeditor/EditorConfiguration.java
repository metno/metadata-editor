package no.met.metadataeditor;

import java.util.ArrayList;
import java.util.List;

import no.met.metadataeditor.widget.EditorWidget;

/**
 * Class used accessing editor configuration that are used to generate the editor UI.
 */
public class EditorConfiguration {
    
    private List<EditorWidget> widgets;

    public EditorConfiguration(){
        widgets = new ArrayList<EditorWidget>();
    }

    public void addWidget(EditorWidget widget){
        widgets.add(widget);
    }
    
    public List<EditorWidget> getWidgets(){
        return widgets;
    }
}
