package org.vietsearch.essme.repository;

import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.vietsearch.essme.model.expert.Expert;

import java.util.List;

public interface ExpertRepository extends MongoRepository<Expert, String> {
    List<Expert> findBy(TextCriteria criteria);
}
