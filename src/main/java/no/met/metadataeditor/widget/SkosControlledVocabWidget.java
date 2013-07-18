package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

/**
 * List selection widget backed by a SKOS controlled vocabulary.
 * 
 * The format of the SKOS controlled vocabulary is assumed to a skos:Collection
 * with the concepts as skos:member/skos:Concept
 */
public class SkosControlledVocabWidget extends EditorWidget {

    private static final long serialVersionUID = 5498501003130078695L;

    @Override
    public Map<String, String> getDefaultValue() {
        Map<String, String> defaultValue = new HashMap<>();
        defaultValue.put("listElement", "");
        return defaultValue;
    }

}
