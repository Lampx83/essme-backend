package org.vietsearch.essme.repository.direct_request;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.vietsearch.essme.model.request_response.DirectRequest;
import java.util.List;

public interface DirectRequestRepository extends MongoRepository<DirectRequest, String> {
    List<DirectRequest> findDirectRequestByExpertId(String id);
    List<DirectRequest> findDirectRequestByCustomerId(String id);
}
