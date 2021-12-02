package org.vietsearch.essme.model.industry;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Names{

	@JsonProperty("vi")
	@NotBlank
	private String vi;

	@JsonProperty("en")
	@NotBlank
	private String en;
}