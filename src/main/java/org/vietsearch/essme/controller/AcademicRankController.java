package org.vietsearch.essme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;
import org.vietsearch.essme.model.academic_rank.AcademicRank;
import org.vietsearch.essme.repository.academic_rank.AcademicRankRepository;

import java.util.List;

@RestController
@RequestMapping("/api/academic-rank")
public class AcademicRankController {
    @Autowired
    private AcademicRankRepository rankRepository;

    @GetMapping
    public List<AcademicRank> getAcademicRanks(@RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "asc", defaultValue = "true") boolean asc) {
        Sort sort = Sort.by("name");
        sort = asc ? sort.ascending() : sort.descending();

        Page<AcademicRank> rankPage = rankRepository.findAll(
                PageRequest.of(page, size, sort));

        return rankPage.getContent();
    }

    @GetMapping("/{_id}")
    public AcademicRank getById(@PathVariable("_id") String _id) {
        return rankRepository.findById(_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rank not found"));
    }

    @GetMapping("/search")
    public List<AcademicRank> searchAcademicRank(@RequestParam(name = "text") String text) {
        List<AcademicRank> list = rankRepository.findBy(
                TextCriteria.forDefaultLanguage().caseSensitive(false).matchingPhrase(text));
        if (list.isEmpty())
            list = rankRepository.findByNameOrSynonymsStartsWithIgnoreCase(text);
        return list;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AcademicRank addAcademicRank(@RequestBody AcademicRank academicRank) {
        if (isNameAlreadyUsed(academicRank.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is already used");
        }
        return rankRepository.insert(academicRank);
    }

    @PutMapping("/{_id}")
    @ResponseStatus(HttpStatus.OK)
    public AcademicRank updateById(@PathVariable("_id") String _id,
            @RequestBody AcademicRank academicRank) {
        rankRepository.findById(_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rank not found"));
        if (isNameAlreadyUsed(academicRank.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is already used");
        }
        academicRank.set_id(_id);
        return rankRepository.save(academicRank);
    }

    @DeleteMapping("/{_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("_id") String _id) {
        rankRepository.deleteById(_id);
    }

    private boolean isNameAlreadyUsed(String name) {
        return rankRepository.findByNameIgnoreCase(name).isPresent();
    }
}
