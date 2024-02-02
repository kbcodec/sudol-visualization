package com.sudol.SudolAPI.Repositories.CustomRepositoriesImplementations;

import com.sudol.SudolAPI.DTOs.OpolskaDTO;
import com.sudol.SudolAPI.Repositories.CustomRepositories.CustomOpolskaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class CustomOpolskaRepositoryImpl implements CustomOpolskaRepository {

    private final MongoTemplate mongoTemplate;

    public CustomOpolskaRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    @Override
    public List<OpolskaDTO> customQuery(String startDate, String endDate, List<String> fields) {

        Query query = new Query();
        query.addCriteria(Criteria.where("date").gte(startDate).lte(endDate));
        query.fields().include("date");

        for (String field : fields) {
            query.fields().include(field);
        }

        query.with(Sort.by(Sort.Order.desc("date")));

        return mongoTemplate.find(query, OpolskaDTO.class);
    }
    @Override
    public List<OpolskaDTO> customQueryForPublic(List<String> fields, int numberOfRows) {
        List<String> opolskaMeteo1Values = Arrays.asList("pressure", "air_temperature", "air_humidity", "wind_speed_avg", "wind_direction");
        List<String> opolskaMeteo2Values = Arrays.asList("solar_radiation", "precipitation");
        List<String> opolskaMeteo3Values = Arrays.asList("soil_temperature", "soil_pressure", "soil_humidity");
        List<String> opolskaMeteo4Values = Arrays.asList("water_state");

        List<String> fieldsForQuery1 = getUniqueValuesForQuery(fields, opolskaMeteo1Values);
        List<String> fieldsForQuery2 = getUniqueValuesForQuery(fields, opolskaMeteo2Values);
        List<String> fieldsForQuery3 = getUniqueValuesForQuery(fields, opolskaMeteo3Values);
        List<String> fieldsForQuery4 = getUniqueValuesForQuery(fields, opolskaMeteo4Values);

        List<OpolskaDTO> result = Stream.of(fieldsForQuery1, fieldsForQuery2, fieldsForQuery3, fieldsForQuery4)
                .filter(queryFields -> !queryFields.isEmpty())
                .flatMap(queryFields -> getAggregationsResult(queryFields, numberOfRows).stream())
                .collect(Collectors.toList());

        return result;
    }

    private List<String> getUniqueValuesForQuery(List<String> fields, List<String> allFields) {
        Set<String> fieldsSet = new HashSet<>(fields);
        Set<String> allFieldsSet = new HashSet<>(allFields);

        fieldsSet.retainAll(allFieldsSet);
        return new ArrayList<>(fieldsSet);
    }

    @Override
    public boolean existsByIdAndChannelId(long id, long channelId) {
        Query query = new Query(Criteria.where("id").is(id)
                .and("channel_id").is(channelId));

        return mongoTemplate.exists(query, OpolskaDTO.class);
    }

    private List<OpolskaDTO> getAggregationsResult(List<String> fields, int numberOfRows) {
        List<Criteria> fieldCriteria = new ArrayList<>();
        for (String field : fields) {
            fieldCriteria.add(Criteria.where(field).exists(true));
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(new Criteria().orOperator(fieldCriteria.toArray(new Criteria[0]))),
                Aggregation.sort(Sort.Direction.DESC, "date"),
                Aggregation.limit(numberOfRows)

        );

        AggregationResults<OpolskaDTO> results = mongoTemplate.aggregate(aggregation, "Opolska", OpolskaDTO.class);

        return results.getMappedResults();
    }
}
