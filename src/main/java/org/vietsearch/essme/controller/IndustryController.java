package org.vietsearch.essme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.vietsearch.essme.model.industry.Industry;
import org.vietsearch.essme.repository.IndustryRepository;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/industries")
public class IndustryController {

    @Autowired
    private IndustryRepository industryRepository;

    @GetMapping("/search")
    public List<Industry> searchExperts(@RequestParam("name") String name) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingPhrase(name);
        List<Industry> list = industryRepository.findBy(criteria);
        if (list.isEmpty())
            return industryRepository.findByNameStartsWithIgnoreCase(name);
        return list;
    }

    @GetMapping
    public List<Industry> getIndustries(@RequestParam(value = "limit", defaultValue = "-1") int limit) {
        if (limit == -1) {
            return industryRepository.findAll();
        }
        return industryRepository.findAll(PageRequest.of(0, limit)).getContent();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Industry createIndustry(@RequestBody Industry industry) {
        checkExistsByName(industry.getName());
        return industryRepository.save(industry);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") String id) {
        if (industryRepository.existsById(id)) {
            industryRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Industry not found");
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Industry update(@PathVariable("id") String id, @Valid @RequestBody Industry industry) {
        industryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Industry not found"));
        checkExistsByName(industry.getName());
        industry.set_id(id);
        return industryRepository.save(industry);
    }

    private void checkExistsByName(String name) {
        if (industryRepository.findByNameIgnoreCase(name) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Industry already exists");
        }
    }
}
