package org.vietsearch.essme.model.industry;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Synonyms{

	@JsonProperty("vi")
	private List<Object> vi;

	@JsonProperty("en")
	private List<Object> en;
}