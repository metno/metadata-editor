package no.met.metadataeditor.dataTypes;

import java.util.List;

import org.jdom2.Content;

/**
 * A simple tree node that is used as part of the EditorTemplate writing.
 */
public class TemplateNode {

    /**
     * The children of this node. Can be an empty list.
     */
    public List<TemplateNode> children;
    
    /**
     * The xml content of the node
     */
    public Content xmlNode;

}
