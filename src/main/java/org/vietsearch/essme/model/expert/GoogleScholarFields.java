package org.vietsearch.essme.model.expert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class GoogleScholarFields {

    @JsonProperty("home_page")
    @Field("home_page")
    private String homePage;

    @JsonProperty("citations")
    private Integer citations;

    @JsonProperty("h_index")
    @Field("h_index")
    private Integer hIndex;

    @JsonProperty("i10index")
    private Integer i10index;

    @JsonProperty("affiliate")
    private String affiliate;

    @JsonProperty("fields")
    @NotNull
    @Size(min = 1)
    private List<String> fields;

    @JsonProperty("research")
    private List<String> research;
}