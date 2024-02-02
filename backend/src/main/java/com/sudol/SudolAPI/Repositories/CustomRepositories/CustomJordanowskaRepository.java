package com.sudol.SudolAPI.Repositories.CustomRepositories;

import com.sudol.SudolAPI.DTOs.JordanowskaDTO;

import java.util.List;

public interface CustomJordanowskaRepository {
    List<JordanowskaDTO> customQuery(String startDate, String endDate, List<String> fields);

    List<JordanowskaDTO> customQueryForPublic(List<String> fields, int numberOfRows);
    boolean existsByIdAndChannelId(long id, long channelId);
}
