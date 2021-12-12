package org.vietsearch.essme.controller;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.vietsearch.essme.model.academic_disciplines.Discipline;
import org.vietsearch.essme.repository.academic_disciplines.DisciplineCustomRepoImpl;
import org.vietsearch.essme.repository.academic_disciplines.DisciplineRepository;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/discipline")
public class DisciplineController {
    @Autowired
    private DisciplineRepository disciplineRepository;
    @Autowired
    private DisciplineCustomRepoImpl disciplineCustomRepoIpml;

    @GetMapping
    public List<Discipline> getDisciplines(@RequestParam(name = "page", defaultValue = "0") int page,
                                           @RequestParam(name = "size", defaultValue = "20") int size,
                                           @RequestParam(name = "sortBy", defaultValue = "name") @Parameter(description = "name | level") String sortBy,
                                           @RequestParam(name = "lang", defaultValue = "en") String lang,
                                           @RequestParam(name = "asc", defaultValue = "true") boolean asc) {
        Sort sort = Sort.by("names." + lang);
        if ("level".equals(sortBy))
            sort = Sort.by("level");
        if (!asc) sort.descending();
        return disciplineRepository.findAll(PageRequest.of(page, size, sort)).getContent();
    }

    @GetMapping("/{_id}")
    public Discipline getDisciplineById(@PathVariable("_id") String _id) {
        return disciplineRepository.findById(_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline not found"));
    }

    @GetMapping("/search")
    public List<Discipline> searchDisciplines(@RequestParam("text") String text) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().caseSensitive(false).matchingPhrase(text);
        List<Discipline> list = disciplineRepository.findBy(criteria);
        if (list.isEmpty()) {
            list = disciplineRepository.findByNamesOrSynonymsStartsWithIgnoreCase(text);
        }
        return list;
    }

    @GetMapping("/parent")
    public Object parent(@RequestParam(name = "name", required = false) String name) {
        if (!Objects.equals(name, null)) {
            return disciplineCustomRepoIpml.findByParentId(name);
        } else {
            return disciplineCustomRepoIpml.getNumberOfDisciplinesInEachParent();
        }
    }

    @DeleteMapping("/{_id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteById(@PathVariable("_id") String _id) {
        disciplineRepository.deleteById(_id);
    }

    @PutMapping("/{_id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Discipline updateById(@PathVariable("_id") String _id, @RequestBody Discipline discipline) {
        disciplineRepository.findById(_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline not found"));
        checkExistsByName(discipline.getName());
        discipline.set_id(_id);
        return disciplineRepository.save(discipline);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Discipline addDiscipline(@RequestBody Discipline discipline) {
        checkExistsByName(discipline.getName());
        return disciplineRepository.insert(discipline);
    }

    private void checkExistsByName(String name) {
        if (disciplineRepository.findByNameIgnoreCase(name).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name " + name + " is already used");
    }
}
