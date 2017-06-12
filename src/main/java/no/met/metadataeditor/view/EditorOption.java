package no.met.metadataeditor.view;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

@ManagedBean
@SessionScoped
public class EditorOption implements Serializable {
    private static final long serialVersionUID = 6066792134476570620L;    
    private boolean includeBreadCrumb = true;
    private boolean embedded;

    public EditorOption() {
    }

    public boolean isIncludeBreadCrumb() {
        return includeBreadCrumb;
    }

    public void setIncludeBreadCrumb(boolean includeBreadCrumb) {
        this.includeBreadCrumb = includeBreadCrumb;
    }

    public boolean isEmbedded() {
        return embedded;
    }

    public void setEmbedded(boolean embedded) {
        this.embedded = embedded;
    }
}
