package org.vietsearch.essme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.vietsearch.essme.model.expert.Expert;
import org.vietsearch.essme.repository.ExpertCustomRepositoryImpl;
import org.vietsearch.essme.repository.ExpertRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/experts")
public class ExpertController {
    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    private ExpertCustomRepositoryImpl expertCustomRepository;

    @GetMapping("/search")
    public List<Expert> searchExperts(@RequestParam("what") String what) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingPhrase(what);
        return expertRepository.findBy(criteria);
    }

    @GetMapping
    public List<Expert> getExperts(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "20") int size, @RequestParam(value = "sort", defaultValue = "name") String sortAttr, @RequestParam(value = "desc", defaultValue = "false") boolean desc) {
        Sort sort = Sort.by(sortAttr);
        if (desc)
            sort = sort.descending();

        Page<Expert> expertPage = expertRepository.findAll(PageRequest.of(page, size, sort));
        return expertPage.getContent();
    }

    @GetMapping("/{id}")
    public Expert findById(@PathVariable("id") String id) {
        return expertRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expert not found"));
    }

    @GetMapping("/field")
    public Map<String, Integer> getNumberOfExpertsInEachField(){
        return this.expertCustomRepository.getNumberOfExpertsInEachField();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Expert update(@PathVariable("id") String id, @Valid @RequestBody Expert expert) {
        expertRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expert not found"));
        expert.set_id(id);
        return expertRepository.save(expert);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Expert createUser(@Valid @RequestBody Expert expert) {
        return expertRepository.save(expert);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") String id) {
        if (!expertRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        expertRepository.deleteById(id);
    }
}
