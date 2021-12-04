package org.vietsearch.essme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.vietsearch.essme.model.university.University;
import org.vietsearch.essme.repository.UniversityRepository;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/universities")
public class UniversityController {
    @Autowired
    private UniversityRepository universityRepository;

    @GetMapping("/search")
    public List<University> searchUniversities(@RequestParam("name") String name) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingPhrase(name);
        List<University> list = universityRepository.findBy(criteria);
        if (list.isEmpty())
            return universityRepository.findByNameStartsWithIgnoreCase(name);
        return list;
    }

    @GetMapping
    public List<University> getUniversities(@RequestParam(value = "limit", defaultValue = "-1") int limit) {
        if (limit == -1) {
            return universityRepository.findAll();
        }
        return universityRepository.findAll(PageRequest.of(0, limit)).getContent();
    }

    @GetMapping("/{id}")
    public University findById(@PathVariable("id") String id) {
        return universityRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "University not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public University createUniversity(@Valid @RequestBody University university) {
        checkExistsByName(university.getName());
        return universityRepository.save(university);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") String id) {
        if (universityRepository.existsById(id)) {
            universityRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "University not found");
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public University update(@PathVariable("id") String id, @Valid @RequestBody University university) {
        universityRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "University not found"));
        checkExistsByName(university.getName());
        university.set_id(id);
        return universityRepository.save(university);
    }

    private void checkExistsByName(String name) {
        if (universityRepository.findByNameIgnoreCase(name) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "University already exists");
        }
    }
}
