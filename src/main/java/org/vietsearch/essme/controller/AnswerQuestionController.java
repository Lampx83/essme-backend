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
import org.vietsearch.essme.model.answer_question.Request;
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
    public Request getQuestionbyId(@PathVariable("id") String id) {return questionRepository.findById(id).get();}

    @GetMapping
    public Page<Request> getQuestions(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "20") int size, @RequestParam(value = "sort", defaultValue = "createdAt") String sortAttr, @RequestParam(value = "desc", defaultValue = "false") boolean desc) {
        Sort sort = Sort.by(sortAttr);
        if (desc)
            sort = sort.descending();

        Page<Request> questionPage = questionRepository.findAll(PageRequest.of(page, size, sort));
        return questionPage;
    }

    @GetMapping("/search")
    public List<Request> searchQuestions(@RequestParam("text") String text) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingPhrase(text);
        return questionRepository.findBy(criteria);
    }

    @GetMapping("/topic/{topic}")
    public Page<Request> getQuestionsbyTopic(@PathVariable("topic") String topic, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "20") int size, @RequestParam(value = "sort", defaultValue = "createdAt") String sortAttr, @RequestParam(value = "desc", defaultValue = "false") boolean desc) {
        Sort sort = Sort.by(sortAttr);
        if (desc)
            sort = sort.descending();

        Page<Request> questionPage = questionRepository.findByTopic(topic,PageRequest.of(page, size, sort));
        return questionPage;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Request addQuestion(@Valid @RequestBody Request request) {
        questionRepository.save(request);
        return questionRepository.save(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Request updateQuestion(@PathVariable("id") String id, @Valid @RequestBody Request request){
        if (questionRepository.existsById(id)) {
            request.set_id(id);
            questionRepository.save(request);
            return request;
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
    public Request addAnswer(@PathVariable("questionId") String questionId, @Valid @RequestBody Answer answer) {
        Request request = questionRepository.findById(questionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found", null));
        if(request.getAnswers()==null)
            request.setAnswers(new ArrayList<>());
        request.getAnswers().add(answer);
        return questionRepository.save(request);
    }

    @GetMapping("/{questionId}/answers")
    public List<Answer> getAnswers(@PathVariable("questionId") String questionId){
        Request request = questionRepository.findById(questionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found", null));
        return request.getAnswers();
    }

    @GetMapping("/{questionId}/answers/{answerId}")
    public Answer getAnswerbyId(@PathVariable("questionId") String questionId, @PathVariable("answerId") String answerId) {
        Request request = questionRepository.findById(questionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found", null));
        if(request.getAnswers()!=null) {
            List<Answer> answerList = request.getAnswers();
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
        Request request = questionRepository.findById(questionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found", null));
        if(request.getAnswers()!=null) {
            for (Answer answer1 : request.getAnswers()) {
                if (answer1.get_id().equals(answerId)) {
                    answer1.setExpertId(answer.getExpertId());
                    answer1.setAnswer(answer.getAnswer());
                    answer1.setUpdatedAt(new Date());
                    answer1.setVote(answer.getVote());
                    questionRepository.save(request);
                    return answer1;
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found", null);
    }

    @DeleteMapping("/{questionId}/answers/{answerId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteAnswer(@PathVariable("questionId") String questionId,@PathVariable("answerId") String answerId){
        Request request = questionRepository.findById(questionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found", null));
        if(request.getAnswers()!=null) {
            for (Answer answer1 : request.getAnswers()) {
                if (answer1.get_id().equals(answerId)) {
                    request.getAnswers().remove(answer1);
                    questionRepository.save(request);
                    return "Deleted";
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found", null);
    }
}
