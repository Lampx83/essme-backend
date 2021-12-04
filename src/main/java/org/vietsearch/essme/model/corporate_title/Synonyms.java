package org.vietsearch.essme.model.corporate_title;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Synonyms {

    @JsonProperty("vi")
    private List<String> vi;

    @JsonProperty("en")
    private List<String> en;
}