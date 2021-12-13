package org.vietsearch.essme.controller;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
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
  
    @GetMapping("/search")
    public List<Company> searchCompanies(@RequestParam("text") String text) {
        List<Company> list = companyRepository.findBy(
                TextCriteria.forDefaultLanguage().caseSensitive(false).matchingPhrase(text));

        if (list.isEmpty())
            list = companyRepository.findByNameOrIndustriesStartsWithIgnoreCase(text);
        return list;

    @GetMapping
    public List<Company> getCompanies(@RequestParam(name = "page", defaultValue = "0") int page,
                                      @RequestParam(name = "size", defaultValue = "20") int size,
                                      @RequestParam(name = "sortBy", defaultValue = "name") @Parameter(description = "name | rank") String sortBy,
                                      @RequestParam(name = "lang", defaultValue = "en") String lang, //en, vi, fr, de
                                      @RequestParam(name = "rankBy", defaultValue = "ValueToday") @Parameter(description = "ValueToday | Fortune | Forbes") String rankBy,
                                      @RequestParam(name = "asc", defaultValue = "true") boolean asc) {
        Sort sort = Sort.by("names." + lang);
        if (Objects.equals("rank", sortBy))
            sort = Sort.by("ranks." + rankBy);
        if (!asc)
            sort.descending();
        return companyRepository.findAll(PageRequest.of(page, size, sort)).getContent();
    }

    @GetMapping("/{_id}")
    public Company getById(@PathVariable("_id") String _id) {
        return companyRepository.findById(_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }

    @GetMapping("/{country}/{industry}/{rank}")
    public List<Company> getByCountry(@PathVariable("country") String country,
                                      @PathVariable("industry") String industry,
                                      @PathVariable("rank") String rank,
                                      @RequestParam(name = "asc", defaultValue = "true") boolean asc) {
        return customRepo.getCompanyByCountryIndustryAndRank(country, industry, rank, asc);
    }

    @DeleteMapping("/{_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("_id") String _id) {
        companyRepository.deleteById(_id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company addCompany(@RequestBody Company company) {
        checkExistsByName(company.getName());
        return companyRepository.insert(company);
    }

    @PutMapping("/{_id}")
    @ResponseStatus(HttpStatus.OK)
    public Company updateById(@PathVariable("_id") String _id, @RequestBody Company company) {
        companyRepository.findById(_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        checkExistsByName(company.getName());
        company.set_id(_id);
        return companyRepository.save(company);
    }

    private void checkExistsByName(String name) {
        if (companyRepository.findByNameIgnoreCase(name).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is already used");
        }
    }
}
