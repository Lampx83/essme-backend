package org.vietsearch.essme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.vietsearch.essme.model.answer_question.Answer;
import org.vietsearch.essme.model.answer_question.Question;
import org.vietsearch.essme.repository.AnswerQuestionRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class AnswerQuestionController {
    @Autowired
    private AnswerQuestionRepository questionRepository;

    @GetMapping("/{id}")
    public Question getQuestionbyId(@PathVariable("id") String id) {return questionRepository.findById(id).get();}

    @GetMapping
    public Page<Question> getQuestions(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "20") int size, @RequestParam(value = "sort", defaultValue = "createdAt") String sortAttr, @RequestParam(value = "desc", defaultValue = "false") boolean desc) {
        Sort sort = Sort.by(sortAttr);
        if (desc)
            sort = sort.descending();

        Page<Question> questionPage = questionRepository.findAll(PageRequest.of(page, size, sort));
        return questionPage;
    }

    @GetMapping("/search")
    public List<Question> searchQuestions(@RequestParam("text") String text) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingPhrase(text);
        return questionRepository.findBy(criteria);
    }

    @GetMapping("/topic/{topic}")
    public Page<Question> getQuestionsbyTopic(@PathVariable("topic") String topic, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "20") int size, @RequestParam(value = "sort", defaultValue = "createdAt") String sortAttr, @RequestParam(value = "desc", defaultValue = "false") boolean desc) {
        Sort sort = Sort.by(sortAttr);
        if (desc)
            sort = sort.descending();

        Page<Question> questionPage = questionRepository.findByTopic(topic,PageRequest.of(page, size, sort));
        return questionPage;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Question addQuestion(@Valid @RequestBody Question question) {
        questionRepository.save(question);
        return questionRepository.save(question);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Question updateQuestion(@PathVariable("id") String id, @Valid @RequestBody Question question){
        if (questionRepository.existsById(id)) {
            question.set_id(id);
            questionRepository.save(question);
            return question;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found", null);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteQuestion(@PathVariable("id") String id){
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
            return "Deleted";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found", null);
        }
    }

    @PostMapping("/{questionId}/answers")
    @ResponseStatus(HttpStatus.CREATED)
    public Question addAnswer(@PathVariable("questionId") String questionId, @Valid @RequestBody Answer answer) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found", null));
        if(question.getAnswers()==null)
            question.setAnswers(new ArrayList<>());
        question.getAnswers().add(answer);
        return questionRepository.save(question);
    }

    @GetMapping("/{questionId}/answers")
    public List<Answer> getAnswers(@PathVariable("questionId") String questionId){
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found", null));
        return question.getAnswers();
    }

    @GetMapping("/{questionId}/answers/{answerId}")
    public Answer getAnswerbyId(@PathVariable("questionId") String questionId, @PathVariable("answerId") String answerId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found", null));
        if(question.getAnswers()!=null) {
            List<Answer> answerList = question.getAnswers();
            for (Answer answer : answerList) {
                if (answer.get_id().equals(answerId)) {
                    return answer;
                }
            }
        }
        return null;
    }

    @PutMapping("/{questionId}/answers/{answerId}")
    @ResponseStatus(HttpStatus.OK)
    public Answer updateAnswer(@PathVariable("questionId") String questionId,@PathVariable("answerId") String answerId,@Valid @RequestBody Answer answer){
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found", null));
        if(question.getAnswers()!=null) {
            for (Answer answer1 : question.getAnswers()) {
                if (answer1.get_id().equals(answerId)) {
                    answer1.setExpertId(answer.getExpertId());
                    answer1.setAnswer(answer.getAnswer());
                    answer1.setUpdatedAt(new Date());
                    answer1.setVote(answer.getVote());
                    questionRepository.save(question);
                    return answer1;
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found", null);
    }

    @DeleteMapping("/{questionId}/answers/{answerId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteAnswer(@PathVariable("questionId") String questionId,@PathVariable("answerId") String answerId){
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found", null));
        if(question.getAnswers()!=null) {
            for (Answer answer1 : question.getAnswers()) {
                if (answer1.get_id().equals(answerId)) {
                    question.getAnswers().remove(answer1);
                    questionRepository.save(question);
                    return "Deleted";
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found", null);
    }
}
