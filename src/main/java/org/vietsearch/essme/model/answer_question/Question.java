package org.vietsearch.essme.model.answer_question;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.DateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

@Data
@Document("answer_question")
public class Question {

	@CreatedDate
	@JsonProperty(value = "created_at", access = JsonProperty.Access.READ_ONLY)
	private Date createdAt;

	@JsonProperty("Description")
	@Field("Description")
	@NotNull
	private String description;

	@JsonProperty("Customer_id")
	@Field("Customer_id")
	@NotNull
	private String customerId;

	@JsonProperty("answers")
	private List<Answer> answers;

	@JsonProperty("Title")
	@Field("Title")
	private String title;

	@Id
	@JsonProperty(value = "_id", access = JsonProperty.Access.READ_ONLY)
	private String _id;

	@JsonProperty("Topic")
	@Field("Topic")
	private List<String> topic;

	@JsonProperty("vote")
	@Field("vote")
	private int vote;

	@LastModifiedDate
	@JsonProperty(value = "updated_at", access = JsonProperty.Access.READ_ONLY)
	private Date updatedAt;
}