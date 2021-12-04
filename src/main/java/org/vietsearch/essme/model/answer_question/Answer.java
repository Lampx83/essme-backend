package org.vietsearch.essme.model.answer_question;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.DateTime;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class Answer {
	public Answer(){
		this._id=new ObjectId().toString();
		this.createdAt=new Date();
		this.updatedAt=new Date();
	}
	@Id
	@JsonProperty(value = "_id", access = JsonProperty.Access.READ_ONLY)
	private String _id;

	@CreatedDate
	@JsonProperty(value = "created_at", access = JsonProperty.Access.READ_ONLY)
	private Date createdAt;

	@JsonProperty("answer")
	@Field("answer")
	@NotNull
	private String answer;

	@JsonProperty("expert_id")
	@Field("expert_id")
	@NotNull
	private String expertId;

	@JsonProperty("vote")
	@Field("vote")
	private int vote;

	@LastModifiedDate
	@JsonProperty(value = "updated_at", access = JsonProperty.Access.READ_ONLY)
	private Date updatedAt;
}