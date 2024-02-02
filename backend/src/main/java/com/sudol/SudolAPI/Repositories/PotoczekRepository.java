package com.sudol.SudolAPI.Repositories;

import com.sudol.SudolAPI.DTOs.PotoczekDTO;
import com.sudol.SudolAPI.Repositories.CustomRepositories.CustomPotoczekRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PotoczekRepository extends MongoRepository<PotoczekDTO, String>, CustomPotoczekRepository {
    List<PotoczekDTO> customQuery(String startDate, String endDate, List<String> fields);
    List<PotoczekDTO> customQueryForPublic(List<String> fields, int numberOfRows);
    boolean existsByIdAndChannelId(long id, long channelId);
}
