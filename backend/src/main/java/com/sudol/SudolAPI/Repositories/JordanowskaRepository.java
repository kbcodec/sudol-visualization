package com.sudol.SudolAPI.Repositories;

import com.sudol.SudolAPI.DTOs.JordanowskaDTO;
import com.sudol.SudolAPI.Repositories.CustomRepositories.CustomJordanowskaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface JordanowskaRepository extends MongoRepository<JordanowskaDTO, String>, CustomJordanowskaRepository {
    List<JordanowskaDTO> customQuery(String startDate, String endDate, List<String> fields);
    List<JordanowskaDTO> customQueryForPublic(List<String> fields, int numberOfRows);
    boolean existsByIdAndChannelId(long id, long channelId);
}
