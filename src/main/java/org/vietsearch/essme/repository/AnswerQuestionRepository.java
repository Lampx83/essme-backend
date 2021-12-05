package org.vietsearch.essme.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.vietsearch.essme.model.answer_question.Request;

import java.util.List;

public interface AnswerQuestionRepository extends MongoRepository<Request,String> {
    Page<Request> findByTopic(String topic, Pageable pageable);
    List<Request> findBy(TextCriteria criteria);
}
