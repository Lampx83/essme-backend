package org.vietsearch.essme.model.university;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Ranks{

	@JsonProperty("the")
	private int the;

	@JsonProperty("qs")
	private int qs;

	@JsonProperty("arwu")
	private int arwu;
}