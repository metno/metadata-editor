package no.met.metadataeditor;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import no.met.metadataeditor.widget.EditorWidget;

public class EditorPage {

    private String name;

    private List<EditorWidget> widgets;

    public EditorPage() {

    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name="widget", namespace="http://www.met.no/schema/metadataeditor/editorConfiguration")
    public List<EditorWidget> getWidgets() {
        return widgets;
    }

    public void setWidgets(List<EditorWidget> widgets) {
        this.widgets = widgets;
    }
}
