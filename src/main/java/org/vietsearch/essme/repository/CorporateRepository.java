package org.vietsearch.essme.repository;

import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.vietsearch.essme.model.corporate_title.Corporate;

import java.util.List;

public interface CorporateRepository extends MongoRepository<Corporate, String> {
    List<Corporate> findBy(TextCriteria criteria);
    List<Corporate> findByNameStartsWithIgnoreCase(String name);
    Corporate findByNameIgnoreCase(String name);
}
