package com.sudol.SudolAPI.Repositories.CustomRepositories;

import com.sudol.SudolAPI.DTOs.OpolskaDTO;

import java.util.List;

public interface CustomOpolskaRepository {
    List<OpolskaDTO> customQuery(String startDate, String endDate, List<String> fields);
    List<OpolskaDTO> customQueryForPublic(List<String> fields, int numberOfRows);
    boolean existsByIdAndChannelId(long id, long channelId);
}
