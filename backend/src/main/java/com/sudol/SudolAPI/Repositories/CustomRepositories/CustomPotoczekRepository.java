package com.sudol.SudolAPI.Repositories.CustomRepositories;

import com.sudol.SudolAPI.DTOs.PotoczekDTO;

import java.util.List;

public interface CustomPotoczekRepository {
    List<PotoczekDTO> customQuery(String startDate, String endDate, List<String> fields);
    List<PotoczekDTO> customQueryForPublic(List<String> fields, int numberOfRows);
    boolean existsByIdAndChannelId(long id, long channelId);
}
