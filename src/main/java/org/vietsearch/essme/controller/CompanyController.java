package org.vietsearch.essme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.vietsearch.essme.model.companies.Company;
import org.vietsearch.essme.repository.companies.CompanyCustomRepo;
import org.vietsearch.essme.repository.companies.CompanyCustomRepoImpl;
import org.vietsearch.essme.repository.companies.CompanyRepository;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyCustomRepoImpl customRepo;

    @GetMapping
    public List<Company> getCompanies(@RequestParam(name = "page", defaultValue = "0") int page,
                                      @RequestParam(name = "size", defaultValue = "20") int size,
                                      @RequestParam(name = "sort", defaultValue = "name") String sortAttr,
                                      @RequestParam(name = "lang", defaultValue = "en") String lang, // en, vi, fr, de
                                      @RequestParam(name = "rank", defaultValue = "ValueToday") String rank, // ValueToday, Fortune, Forbes
                                      @RequestParam(name = "asc", defaultValue = "true") boolean asc) {
        Sort sort = null;
        if (Objects.equals(sortAttr, "rank")) {
            sort = Sort.by("ranks." + rank);
        } else {
            sort = Sort.by("names." + lang);
        }
        sort = asc ? sort.ascending() : sort.descending();

        Page<Company> companyPage = companyRepository.findAll(
                PageRequest.of(page, size, sort));

        return companyPage.getContent();
    }

    @GetMapping("/id/{_id}")
    public Company getById(@PathVariable("_id") String _id) {
        return companyRepository.findById(_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }

    @GetMapping("/{country}/{industry}/{rank}")
    public List<Company> getByCountry(@PathVariable("country") String country,
                                      @PathVariable("industry") String industry,
                                      @PathVariable("rank") String rank,
                                      @RequestParam(name = "asc", defaultValue = "true") boolean asc) {
        return customRepo.getCompanyByCountryIndustryAndRank(country, industry, rank, asc);
    }

    @GetMapping("/search")
    public List<Company> searchCompanies(@RequestParam("text") String text) {
        List<Company> list = companyRepository.findBy(
                TextCriteria.forDefaultLanguage().caseSensitive(false).matchingPhrase(text));

        if (list.isEmpty())
            list = companyRepository.findByNameOrIndustriesStartsWithIgnoreCase(text);
        return list;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company addCompany(@RequestBody Company company) {
        if (isNameAlreadyUsed(company.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is already used");
        }
        return companyRepository.insert(company);
    }

    @PutMapping("/{_id}")
    @ResponseStatus(HttpStatus.OK)
    public Company updateById(@PathVariable("_id") String _id, @RequestBody Company company) {
        companyRepository.findById(_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        if (isNameAlreadyUsed(company.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is already used");
        }
        company.set_id(_id);
        return companyRepository.save(company);
    }

    @DeleteMapping("/{_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("_id") String _id) {
        companyRepository.deleteById(_id);
    }

    private boolean isNameAlreadyUsed(String name) {
        return companyRepository.findByNameIgnoreCase(name).isPresent();
    }
}
