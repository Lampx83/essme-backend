package org.vietsearch.essme.model.university;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public @Data
class Synonyms{

	@JsonProperty("de")
	private List<Object> de;

	@JsonProperty("vi")
	private List<Object> vi;

	@JsonProperty("en")
	private List<String> en;

	@JsonProperty("fr")
	private List<Object> fr;
}