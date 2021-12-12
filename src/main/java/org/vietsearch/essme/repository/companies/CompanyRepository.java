package org.vietsearch.essme.repository.companies;

import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vietsearch.essme.model.companies.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends MongoRepository<Company, String> {
    List<Company> findBy(TextCriteria textCriteria);

    @Query("{$or: [" +
            "{'name': {'$regex': /^?0/i}}," +
            "{'industries': {'$regex': /^?0/i}}" +
            "]}")
    List<Company> findByNameOrIndustriesStartsWithIgnoreCase(String name);

//    @Query("{country : {$regex : /:#{#country}/, $options : 'i'}, industries : {$regex : /:#{#industry}/, $options: 'i'}}",)
//    List<Company> findByCountryAndIndustryAndRankIgnoreCase(@Param("country") String country,
//                                                            @Param("industry") String industry,
//                                                            @Param("rank") String rank,
//                                                            @Param("asc") int asc);



    Optional<Company> findByNameIgnoreCase(String name);
}
