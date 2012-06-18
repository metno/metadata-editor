package no.met.metadataeditor.service;

/**
 * A single value attributed that can be used for most editor variables that
 * only contains a single value.
 */
public class SingleValueData extends EditorData {

    public final String value;

    public SingleValueData(String value) {
        this.value = value;
    }

}
