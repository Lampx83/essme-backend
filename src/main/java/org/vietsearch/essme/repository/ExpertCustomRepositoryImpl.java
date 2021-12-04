package org.vietsearch.essme.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.stereotype.Repository;
import org.vietsearch.essme.model.expert.Expert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class ExpertCustomRepositoryImpl implements ExpertCustomRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public Map<String, Integer> getNumberOfExpertsInEachField(){
       List<Expert> list =  mongoTemplate.aggregate(
                Aggregation.newAggregation(
                        new UnwindOperation(
                                Fields.field("_source.google_scholar_fields.fields")
                        )
                )
                ,
                Expert.class,
                Expert.class
        ).getMappedResults();

       Map<String, Integer> map = new HashMap<>();

       for(Expert expert: list){
           changeMapValue(map, expert.getSource().getGoogleScholarFields().getFields().get(0));
       }
       return map;
    }

    private void changeMapValue(Map map, String key){
        key = toUpperCaseFirstLetters(key);
        if(!map.containsKey(key)){
            map.put(key, Integer.valueOf(1));
        }else{
            Integer oldValue = (Integer) map.get(key);
            map.replace(key, oldValue + 1);
        }
    }

    private String toUpperCaseFirstLetters(String s){
        String[] arr = s.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}
