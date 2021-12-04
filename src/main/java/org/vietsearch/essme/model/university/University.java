package org.vietsearch.essme.model.university;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Document("universities")
public class University{

	@Id
	@JsonProperty(value = "_id", access = JsonProperty.Access.READ_ONLY)
	private String _id;

	@JsonProperty("country")
	private String country;

	@JsonProperty("website")
	private String website;

	@JsonProperty("types")
	private List<String> types;

	@JsonProperty("last_updated")
	@Field("last_updated")
	private String lastUpdated;

	@JsonProperty("ranks")
	private Ranks ranks;

	@JsonProperty("names")
	@NotNull
	@Valid
	private Names names;

	@JsonProperty("synonyms")
	private Synonyms synonyms;

	@JsonProperty("name")
	@NotBlank
	private String name;

	@JsonProperty("id")
	private String id;

	@JsonProperty("source")
	private String source;
}
