package org.vietsearch.essme.model.expert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ContactsItem {

    @JsonProperty("type")
    private String type;

    @JsonProperty("value")
    private String value;
}