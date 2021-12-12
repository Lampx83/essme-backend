package org.vietsearch.essme.repository.academic_disciplines;

import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.vietsearch.essme.model.academic_disciplines.Discipline;

import java.util.List;
import java.util.Optional;

public interface DisciplineRepository extends MongoRepository<Discipline, String> {
    List<Discipline> findBy(TextCriteria criteria);

    @Query("{$or:[" +
            "{'names.en': {'$regex': /^?0/i} }," +
            "{'names.vi': {'$regex': /^?0/i} }," +
            "{'synonyms.vi': {'$regex': /^?0/i} }," +
            "{'synonyms.en': {'$regex': /^?0/i} }" +
            "]}")
    List<Discipline> findByNamesOrSynonymsStartsWithIgnoreCase(String text);

    List<Discipline> findByParentIdStartsWithIgnoreCase(String text);

    Optional<Discipline> findByNameIgnoreCase(String text);
}
