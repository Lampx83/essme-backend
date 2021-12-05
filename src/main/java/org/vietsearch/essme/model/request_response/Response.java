package org.vietsearch.essme.model.request_response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class Response {
    @Id
    @JsonProperty(value = "_id", access = JsonProperty.Access.READ_ONLY)
    private String _id;

    @CreatedDate
    @JsonProperty(value = "created_at", access = JsonProperty.Access.READ_ONLY)
    private Date createdAt;

    @JsonProperty("content")
    @Field("content")
    @NotBlank(message = "{vi=\"nội dung trống\", en=\"content is empty\"}")
    private String content;

    @JsonProperty("expert_id")
    @Field("expert_id")
    @NotNull
    private String expertId;

    @LastModifiedDate
    @JsonProperty(value = "updated_at", access = JsonProperty.Access.READ_ONLY)
    private Date updatedAt;
}
