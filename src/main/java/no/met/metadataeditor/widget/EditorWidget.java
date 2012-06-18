package no.met.metadataeditor.widget;

/**
 * Class for representing widgets. This is a pure data class and should never
 * contain any logic.
 */
public class EditorWidget {

    // the variable name that is used for this field. Must correspond with the
    // same name in the template
    public final String variableName;

    public final String widgetType;

    public EditorWidget(String variableName, String widgetType) {
        this.variableName = variableName;
        this.widgetType = widgetType;        
    }

    public String getVariableName() {
        return variableName;
    }

    public String getWidgetType() {
        return widgetType;
    }
}
