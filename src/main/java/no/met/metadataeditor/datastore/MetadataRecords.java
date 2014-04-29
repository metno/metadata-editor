package no.met.metadataeditor.datastore;

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 
 * Class to represent meta data record
 */
@XmlRootElement(name = "resources")
public class MetadataRecords {
    private List<ResourceMetadata> recordsList;

    @XmlElement(name = "resource")
    public List<ResourceMetadata> getRecords() {
        return recordsList;
    }

    public void setRecords(List<ResourceMetadata> records) {
        this.recordsList = records;
    }
    
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
    
    public static class WebDavResourceMetadata extends ResourceMetadata {
            private Date creationTime;

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
