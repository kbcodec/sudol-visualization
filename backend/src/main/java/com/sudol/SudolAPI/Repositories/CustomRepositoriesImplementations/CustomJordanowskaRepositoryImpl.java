package com.sudol.SudolAPI.Repositories.CustomRepositoriesImplementations;

import com.sudol.SudolAPI.DTOs.JordanowskaDTO;
import com.sudol.SudolAPI.Repositories.CustomRepositories.CustomJordanowskaRepository;
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
public class CustomJordanowskaRepositoryImpl implements CustomJordanowskaRepository {

    private final MongoTemplate mongoTemplate;

    public CustomJordanowskaRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    @Override
    public List<JordanowskaDTO> customQuery(String startDate, String endDate, List<String> fields) {

        Query query = new Query();
        query.addCriteria(Criteria.where("date").gte(startDate).lte(endDate));
        query.fields().include("date");

        for (String field : fields) {
            query.fields().include(field);
        }

        query.with(Sort.by(Sort.Order.desc("date")));

        return mongoTemplate.find(query, JordanowskaDTO.class);
    }

    @Override
    public List<JordanowskaDTO> customQueryForPublic(List<String> fields, int numberOfRows) {
        List<String> jordanowskaMeteo1Values = Arrays.asList("pressure", "air_temperature", "air_humidity", "wind_speed_avg", "wind_direction");
        List<String> jordanowskaMeteo2Values = Arrays.asList("solar_radiation", "precipitation");
        List<String> jordanowskaMeteo3Values = Arrays.asList("soil_temperature", "soil_pressure", "soil_humidity");
        List<String> jordanowskaMeteo4Values = Arrays.asList("water_state");

        List<String> fieldsForQuery1 = getUniqueValuesForQuery(fields, jordanowskaMeteo1Values);
        List<String> fieldsForQuery2 = getUniqueValuesForQuery(fields, jordanowskaMeteo2Values);
        List<String> fieldsForQuery3 = getUniqueValuesForQuery(fields, jordanowskaMeteo3Values);
        List<String> fieldsForQuery4 = getUniqueValuesForQuery(fields, jordanowskaMeteo4Values);

        List<JordanowskaDTO> result = Stream.of(fieldsForQuery1, fieldsForQuery2, fieldsForQuery3, fieldsForQuery4)
                .filter(queryFields -> !queryFields.isEmpty())
                .flatMap(queryFields -> getAggregationsResult(queryFields, numberOfRows).stream())
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public boolean existsByIdAndChannelId(long id, long channelId) {
        Query query = new Query(Criteria.where("id").is(id)
                .and("channel_id").is(channelId));

        return mongoTemplate.exists(query, JordanowskaDTO.class);
    }

    private List<String> getUniqueValuesForQuery(List<String> fields, List<String> allFields) {
        Set<String> fieldsSet = new HashSet<>(fields);
        Set<String> allFieldsSet = new HashSet<>(allFields);

        fieldsSet.retainAll(allFieldsSet);
        return new ArrayList<>(fieldsSet);
    }

    private List<JordanowskaDTO> getAggregationsResult(List<String> fields, int numberOfRows) {
        List<Criteria> fieldCriteria = new ArrayList<>();
        for (String field : fields) {
            fieldCriteria.add(Criteria.where(field).exists(true));
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(new Criteria().orOperator(fieldCriteria.toArray(new Criteria[0]))),
                Aggregation.sort(Sort.Direction.DESC, "date"),
                Aggregation.limit(numberOfRows)

        );

        AggregationResults<JordanowskaDTO> results = mongoTemplate.aggregate(aggregation, "Jordanowska", JordanowskaDTO.class);

        return results.getMappedResults();
    }


}
