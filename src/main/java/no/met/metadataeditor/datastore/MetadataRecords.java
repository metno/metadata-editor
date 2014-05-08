package no.met.metadataeditor.datastore;

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * Class to represent meta data record
 */
@XmlRootElement(name = "resources")
@XmlSeeAlso(MetadataRecords.WebDavResourceMetadata.class)
public class MetadataRecords<T> {

    private List<T> recordsList;

    @XmlElement(name = "resource", type = WebDavResourceMetadata.class)
    public List<T> getRecords() {
        return recordsList;
    }

    public void setRecords(List<T> records) {
        this.recordsList = records;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    public static class ResourceMetadata {

        private String name;
        private String relativeUrl;
        private Date lastModified;

        public ResourceMetadata() {
        }

        public ResourceMetadata(String name, String relativeUrl, Date lastMdified) {
            this.name = name;
            this.relativeUrl = relativeUrl;
            this.lastModified = lastMdified;
        }

        @XmlAttribute
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @XmlAttribute
        public String getRelativeUrl() {
            return relativeUrl;
        }

        public void setRelativeUrl(String relativeUrl) {
            this.relativeUrl = relativeUrl;
        }

        @XmlAttribute
        @XmlJavaTypeAdapter(DateAdapter.class)
        public Date getLastModified() {
            return lastModified;
        }

        public void setLastModified(Date lastModified) {
            this.lastModified = lastModified;
        }
    }

    @XmlAccessorType(XmlAccessType.NONE)
    public static class WebDavResourceMetadata extends ResourceMetadata {

        private Date creationTime;

        public WebDavResourceMetadata() {
        }

        public WebDavResourceMetadata(Date creationTime, String name, String relativeUrl, Date lastMdified) {
            super(name, relativeUrl, lastMdified);
            this.creationTime = creationTime;
        }

        @XmlAttribute
        @XmlJavaTypeAdapter(DateAdapter.class)
        public Date getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(Date creationTime) {
            this.creationTime = creationTime;
        }
    }

}
