package com.sudol.SudolAPI.Repositories;

import com.sudol.SudolAPI.DTOs.OpolskaDTO;
import com.sudol.SudolAPI.Repositories.CustomRepositories.CustomOpolskaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OpolskaRepository extends MongoRepository<OpolskaDTO, String>, CustomOpolskaRepository {
    List<OpolskaDTO> customQuery(String startDate, String endDate, List<String> fields);
    List<OpolskaDTO> customQueryForPublic(List<String> fields, int numberOfRows);
    boolean existsByIdAndChannelId(long id, long channelId);
}
