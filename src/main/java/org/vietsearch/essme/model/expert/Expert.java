package org.vietsearch.essme.model.expert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Document("experts")
public class Expert {

    @Id
    @JsonProperty(value = "_id", access = JsonProperty.Access.READ_ONLY)
    private String _id;

    @JsonProperty("_index")
    @Field("_index")
    private String index;

    @JsonProperty("_source")
    @Field("_source")
    @NotNull
    @Valid
    private Source source;
}