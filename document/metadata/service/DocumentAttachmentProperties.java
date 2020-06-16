package com.prgx.workbench.claim.document.metadata.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class {@link DocumentAttachmentProperties} contains configurable properties for this module.
 */
@ConfigurationProperties(prefix = "document.attachment")
@Getter
@Setter
public class DocumentAttachmentProperties {

    /** The meta data properties to be removed from objectstore. */
    private List<String> removeMetaDataPropertiesObjectStore;;

    public DocumentAttachmentProperties() {
        this.removeMetaDataPropertiesObjectStore = Arrays
                .asList(DocumentAttachmentConstant.REMOVE_METADATA_PROPERTIES_OBJECTSTORE.split(","));
    }

    /**
     * Gets the remove metadata properties objectstore
     *
     * @return the remove metadata properties objectstore
     */
    public List<String> getRemoveMetaDataPropertiesObjectStore() {
        return removeMetaDataPropertiesObjectStore;
    }

    /**
     * sets the remove metadata properties objectstore
     *
     * @param removeMetaDataPropertiesObjectStore
     *            remove metadata properties objectstore
     */
    public void setRemoveMetaDataPropertiesObjectStore(List<String> removeMetaDataPropertiesObjectStore) {
        this.removeMetaDataPropertiesObjectStore = removeMetaDataPropertiesObjectStore;
    }
}
