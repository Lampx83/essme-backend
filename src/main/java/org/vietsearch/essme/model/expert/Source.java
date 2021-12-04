package org.vietsearch.essme.model.expert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class Source {

    @JsonProperty("thumbnail")
    private Object thumbnail;

    @JsonProperty("types")
    private List<String> types;

    @JsonProperty("address")
    private Address address;

    @JsonProperty("indexed")
    private Long indexed;

    @JsonProperty("google_scholar_fields")
    @Field("google_scholar_fields")
    @Valid
    private GoogleScholarFields googleScholarFields;

    @JsonProperty("source")
    private String source;

    @JsonProperty("title")
    private String title;

    @JsonProperty("source_url")
    @Field("source_url")
    private String sourceUrl;

    @JsonProperty("created_datetime")
    @Field("created_datetime")
    private String createdDatetime;

    @JsonProperty("crawl_by_background_job")
    @Field("crawl_by_background_job")
    private Boolean crawlByBackgroundJob;

    @JsonProperty("job_id")
    @Field("job_id")
    private String jobId;

    @JsonProperty("name")
    @NotBlank
    private String name;

    @JsonProperty("source_id")
    @Field("source_id")
    private String sourceId;

    @JsonProperty("id")
    private String id;

    @JsonProperty("contacts")
    private List<ContactsItem> contacts;
}